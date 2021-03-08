package com.vzoom.apocalypse.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vzoom.apocalypse.common.dto.FeedbackContext;
import com.vzoom.apocalypse.api.factory.PushFeedbackFactory;
import com.vzoom.apocalypse.api.service.FeedbackService;
import com.vzoom.apocalypse.api.service.rules.CommonRulesHandler;
import com.vzoom.apocalypse.api.strategy.ReadFeedbackFileStrategy;
import com.vzoom.apocalypse.api.strategy.context.ReadFileContext;
import com.vzoom.apocalypse.common.cache.CommonCache;
import com.vzoom.apocalypse.common.config.Builder;
import com.vzoom.apocalypse.common.constants.Constants;
import com.vzoom.apocalypse.common.dto.InvokePostloanRequest;
import com.vzoom.apocalypse.common.dto.PostloanBaseBodyDTO;
import com.vzoom.apocalypse.common.dto.PostloanResponse;
import com.vzoom.apocalypse.common.entity.ApocalypseAfterLoan;
import com.vzoom.apocalypse.common.entity.ApocalypseFeedback;
import com.vzoom.apocalypse.common.entity.ApocalypseProperty;
import com.vzoom.apocalypse.common.enums.CommonEnum;
import com.vzoom.apocalypse.common.enums.CommonResponseInfo;
import com.vzoom.apocalypse.common.repositories.BankFeedbackMapper;
import com.vzoom.apocalypse.common.repositories.FeedbackMapper;
import com.vzoom.apocalypse.common.repositories.PostloanMapper;
import com.vzoom.apocalypse.common.service.InvokeService;
import com.vzoom.apocalypse.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {


    @Autowired
    private CommonRulesHandler commonRulesHandler;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private PostloanMapper postloanMapper;

    @Autowired
    private BankFeedbackMapper bankFeedbackMapper;

    @Value("${datagrid.appid}")
    private String appid;

    @Value("${datagrid.appKey}")
    private String appKey;

    @Value("${datagrid.dsType}")
    private String dsType;

    @Value("${invoke.datagrid.postloan.url}")
    private String invokeUrl;

    /**
     * 读取反馈文件，入库记录
     *
     * @param area
     */
    @Override
    public void readFeedbackFile(String area) {
        log.info("当前读取反馈文件的地区：{}",area);

        //根据配置的策略 获取文件内容
        ApocalypseProperty property = CommonCache.PROPERTY_CACHE_MAP.get(area);

        log.info("当前配置信息：{}", property);

        //获取文件内容，放入Context中
        log.info("开始读取原始字段");
        List<FeedbackContext> feedbackContextList = new ArrayList<>();
        try {
            feedbackContextList = readFeedbackFileByStrategy(property);

            if (CollectionUtils.isEmpty(feedbackContextList)) {
                log.info("原始文件无内容记录");
                return;
            }

        } catch (Exception e) {
            log.error("当前配置地区：{}，产品id:{} 读取原始反馈文件内容报错，继续下一个地区", property.getArea(), property.getProductId());
            return;
        }


        log.info("调用反馈引擎，处理原始字段");
        for (FeedbackContext feedbackContext : feedbackContextList) {
            try {
                feedbackContext.setSource(CommonEnum.FEEDBACK_SOURCE_SCHEDULE.getCode());
                //根据规则，调用反馈引擎，处理原始字段，并组装成税局需要的报文
                commonRulesHandler.HandleRules(feedbackContext);

            } catch (Exception e) {
                log.error("反馈引擎调用出错：{}",e.getMessage()+e);
                e.printStackTrace();

            }
        }


    }


    /**
     * 推送反馈内容到datagrid，解析内容
     *
     * @param area
     * @return
     * @throws Exception
     */
    @Override
    public void pushFeedbackInfo(String area) throws Exception {
        QueryWrapper<ApocalypseFeedback> queryWrapper = new QueryWrapper<>();
        List<ApocalypseFeedback> feedbackList = new ArrayList<>();

        ApocalypseProperty property = CommonCache.PROPERTY_CACHE_MAP.get(area);
        if (!StringUtils.isEmpty(property.getGetFeedbackSql())) {
            //不使用默认方式获取反馈数据，而是执行SQL
            String feedbackSql = property.getGetFeedbackSql();
            feedbackList = bankFeedbackMapper.selectFeedbackList(feedbackSql);

            //TODO 自行实现获取反馈数据的名单

        } else {
            //默认查询出当前地区所有没有做过反馈的所有数据
            queryWrapper.eq("resp_code", CommonEnum.FEEDBACK_STATUS_1111.getCode());

            feedbackList = feedbackMapper.selectList(queryWrapper);
        }

        //根据实现的区域，取具体的实现类
        InvokeService pushFeedbackInvoke = PushFeedbackFactory.getPushFeedbackInvoke(area);
        if (null == pushFeedbackInvoke) {
            log.error("当前区域没有实现对应的反馈推送接口：{}", area);
            return;
        }

        log.info("当前区域 {} 反馈数据推送开始", area);
        pushFeedbackInvoke.pushFeedbackRequest(feedbackList);

    }

    /**
     * 读取贷后名单
     *
     * @param area
     * @throws Exception
     */
    @Override
    public void readAndPushPostloanInfo(String area) throws Exception {

        ApocalypseProperty property = CommonCache.PROPERTY_CACHE_MAP.get(area);
        List<ApocalypseFeedback> feedbackList;
        List<ApocalypseAfterLoan> apocalypseAfterLoans;
        log.info("贷后数据入库，入库区域:{}", area);
        if (!StringUtils.isEmpty(property.getGetPostloanSql())) {
            //执行SQL，获取需要取贷后的贷后名单
/*            String feedbackSql = property.getGetPostloanSql();
            feedbackList = bankFeedbackMapper.selectFeedbackList(feedbackSql);*/

            //自定义获取贷后数据的规则
            InvokeService pushFeedbackInvoke = PushFeedbackFactory.getPushFeedbackInvoke(area);
            if (null == pushFeedbackInvoke) {
                log.error("当前区域没有实现对应的贷后名单拼装接口：{}，使用默认推送方案;", area);
                return;
            }

            //获取所有的反馈数据，在实现类中处理
            QueryWrapper<ApocalypseFeedback> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("AREA", area);
            List<ApocalypseFeedback> list = feedbackMapper.selectList(queryWrapper);

            feedbackList = pushFeedbackInvoke.getPostloanList(area, list);

        } else {

            QueryWrapper<ApocalypseFeedback> queryWrapper = new QueryWrapper<>();
            //默认查询出当前地区所有反馈成功的数据，去重后得到贷后名单
            queryWrapper.eq("resp_code", CommonEnum.FEEDBACK_STATUS_0000.getCode());

            feedbackList = feedbackMapper.selectList(queryWrapper);
        }

        //贷后名单入库
        apocalypseAfterLoans = savePostloanList(feedbackList, area);


        //通知datagrid取数，更新工单号
        pushPostloanInfo(apocalypseAfterLoans, area);

    }

    /**
     * 重取贷后名单中状态为9999的数据
     *
     * @param area
     */
    @Override
    public void rePushPostloanInfo(String area) {

        //找到推送失败的所有数据
        QueryWrapper<ApocalypseAfterLoan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("AREA", area).eq("FETCH_STATUS",CommonEnum.POSTLOAN_PUSH_STATUS_9999.getCode());
        List<ApocalypseAfterLoan> apocalypseAfterLoans = postloanMapper.selectList(queryWrapper);

        //推送数据
        try {

            pushPostloanInfo(apocalypseAfterLoans, area);

        }catch (Exception e){
            log.error("重推数据异常");

        }
    }

    /**
     * 推送贷后名单到datagrid
     *
     * @param apocalypseAfterLoans
     * @throws Exception
     */
    private void pushPostloanInfo(List<ApocalypseAfterLoan> apocalypseAfterLoans, String area) throws Exception {

        InvokeService pushFeedbackInvoke = PushFeedbackFactory.getPushFeedbackInvoke(area);
        if (null == pushFeedbackInvoke) {
            log.error("当前区域没有实现对应的贷后名单拼装接口：{}，使用默认推送方案;", area);
            return;
        }

        String ministryCode = CommonCache.PROPERTY_CACHE_MAP.get(area).getMinistryCode();

        for (ApocalypseAfterLoan apocalypseAfterLoan : apocalypseAfterLoans) {

            InvokePostloanRequest invokePostloanRequest = new InvokePostloanRequest<>();
            if (null == pushFeedbackInvoke.assemblePostlaonRequest(apocalypseAfterLoan)) {
                //默认方案组装参数
                invokePostloanRequest.setAppId(appid);
                invokePostloanRequest.setAppKey(appKey);
                invokePostloanRequest.setBizCode(CommonEnum.DATAGRID_BIZCODE_1.getCode());
                invokePostloanRequest.setDsType(dsType);
                invokePostloanRequest.setNsrsbh(apocalypseAfterLoan.getNsrsbh());
                invokePostloanRequest.setMinistryCode(ministryCode);
                invokePostloanRequest.setBody(new PostloanBaseBodyDTO(apocalypseAfterLoan.getNsrsbh()));
            } else {
                //组装参数，地区个性化
                invokePostloanRequest = pushFeedbackInvoke.assemblePostlaonRequest(apocalypseAfterLoan);

            }

            //推送数据
            log.info("调用 datagrid 请求报文: {}", JSON.toJSONString(invokePostloanRequest));
            String response = HttpClientUtil.postJson(invokeUrl, JSON.toJSONString(invokePostloanRequest));
            log.info("调用 datagrid 返回报文: {}", response);

            //入库
            PostloanResponse postloanResponse = JSONObject.toJavaObject(JSON.parseObject(response), PostloanResponse.class);
            ApocalypseAfterLoan afterLoan = new ApocalypseAfterLoan();
            afterLoan.setId(apocalypseAfterLoan.getId());

            if (!CommonResponseInfo.SUCCESS.getCode().equals(postloanResponse.getResponseCode())) {
                //更新状态
                afterLoan.setFetchStatus(CommonEnum.POSTLOAN_PUSH_STATUS_9999.getCode());
                postloanMapper.updateById(afterLoan);

            } else {
                //将tieket更新到贷后名单表
                String ticket = postloanResponse.getData().getTicket();
                afterLoan.setTicket(ticket);
                afterLoan.setFetchStatus(CommonEnum.POSTLOAN_PUSH_STATUS_0000.getCode());
                postloanMapper.updateById(afterLoan);

            }

        }


    }


    /**
     * 贷后名单入库
     *
     * @param feedbackList
     * @param area
     * @return
     */
    private List<ApocalypseAfterLoan> savePostloanList(List<ApocalypseFeedback> feedbackList, String area) {

        ApocalypseProperty property = CommonCache.PROPERTY_CACHE_MAP.get(area);

        QueryWrapper<ApocalypseAfterLoan> queryAfterloanWrapper = new QueryWrapper<>();
        List<ApocalypseAfterLoan> postloanList = new ArrayList<>();

        if (CollectionUtils.isEmpty(feedbackList)) {
            return postloanList;
        }
        String batchNum = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));

        for (ApocalypseFeedback feedback : feedbackList) {

            ApocalypseAfterLoan dto = new ApocalypseAfterLoan();
            dto.setNsrsbh(feedback.getNsrsbh());
            dto.setArea(area);
            dto.setProductId(property.getProductId() == null ? Constants.DEFAULT_PRODUCT_ID : property.getProductId());
            dto.setBatchNumber(batchNum);

            //避免重复入库
            queryAfterloanWrapper.eq("NSRSBH", feedback.getNsrsbh())
                    .eq("AREA_SIGN", area)
                    .eq("PRODUCT_ID", property.getProductId() == null ? Constants.DEFAULT_PRODUCT_ID : property.getProductId())
                    .eq("BATCH_NUMBER", batchNum);
            Integer count = postloanMapper.selectCount(queryAfterloanWrapper);
            if (count == null || count >= 1) {
                log.info("数据库已存在税号{} 的 {}条记录", feedback.getNsrsbh(), count);
                continue;
            }

            dto.setFetchStatus(CommonEnum.POSTLOAN_PUSH_STATUS_1111.getCode());
            dto.setFetchTimes(0);
            dto.setCompanyName("");
            dto.setRemark(feedback.getMd5());

            try {
                //入库
                postloanMapper.insert(dto);
                postloanList.add(dto);

            } catch (Exception e) {
                log.error("数据库异常，贷后名单入库失败,税号:{}", feedback.getNsrsbh());
            }
        }

        return postloanList;
    }


    /**
     * 根据不同的策略获取文件内容
     * 放入字段：id，original_text、area
     *
     * @param property
     * @return
     */
    private List<FeedbackContext> readFeedbackFileByStrategy(ApocalypseProperty property) throws Exception {

        List<FeedbackContext> feedbackContexts = new ArrayList<>();

        try {
            //获取对应的读取策略
            ReadFileContext fileContext = new ReadFileContext((ReadFeedbackFileStrategy) applicationContext.getBean(Class.forName(CommonEnum.getMessageByCode(property.getFeedbackStrategy()))));

            //获取文件内容的每一行
            List<String> fileContextList = fileContext.contextInterface(property.getFeedbackFilePath());

            //放入全局Context
            for (String context : fileContextList) {
                if(StringUtils.isEmpty(context)){
                    continue;
                }

                //生成唯一ID，入库ID
                String systemId = UUID.randomUUID().toString().replaceAll("-", "");
                FeedbackContext feedbackContext = Builder.of(FeedbackContext::new)
                        .with(FeedbackContext::setId, systemId)
                        .with(FeedbackContext::setArea, property.getArea())
                        .with(FeedbackContext::setMinistryCode, property.getMinistryCode())
                        .with(FeedbackContext::setOriginalText, context)
                        .with(FeedbackContext::setFeedbackFilePath, property.getFeedbackFilePath())
                        .with(FeedbackContext::setFeedbackStrategy, property.getFeedbackStrategy())
                        .with(FeedbackContext::setFeedbackFrequency, property.getFeedbackFrequency())
                        .with(FeedbackContext::setProductId, property.getProductId())
                        .build();

                feedbackContexts.add(feedbackContext);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取文件策略报错 {}，请检查数据库表apocalypse_property 配置项：FEEDBACK_STRATEGY", e.getMessage() + e);
            throw e;
        }


        return feedbackContexts;
    }


}
