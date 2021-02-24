package com.vzoom.apocalypse.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vzoom.apocalypse.api.dto.FeedbackContext;
import com.vzoom.apocalypse.api.repository.FeedbackMapper;
import com.vzoom.apocalypse.api.repository.PropertyMapper;
import com.vzoom.apocalypse.api.service.FeedbackService;
import com.vzoom.apocalypse.api.service.InvokeService;
import com.vzoom.apocalypse.api.service.rules.CommonRulesHandler;
import com.vzoom.apocalypse.api.strategy.ReadFeedbackFileStrategy;
import com.vzoom.apocalypse.api.strategy.context.ReadFileContext;
import com.vzoom.apocalypse.common.cache.CommonCache;
import com.vzoom.apocalypse.common.config.Builder;
import com.vzoom.apocalypse.common.entity.ApocalypseFeedback;
import com.vzoom.apocalypse.common.entity.ApocalypseProperty;
import com.vzoom.apocalypse.common.enums.CommonEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private CommonRulesHandler commonRulesHandler;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private InvokeService invokeService;

    /**
     * 读取反馈文件，入库记录
     *
     * @param area
     */
    @Override
    public void readFeedbackFile(String area) {

        //根据配置的策略 获取文件内容
        List<ApocalypseProperty> properties = CommonCache.PROPERTY_CACHE_LIST.stream().filter(x -> x.getArea().equals(area)).collect(Collectors.toList());

        if(properties.isEmpty()){
            log.info("当前地区数据库中 缺失对应配置信息：{}",area);
            return;
        }

        for (int i = 0; i < properties.size(); i++) {
            ApocalypseProperty property = properties.get(i);
            log.info("当前配置信息：{}",property);

            //获取文件内容，放入Context中
            log.info("开始读取原始字段");
            List<FeedbackContext> feedbackContextList = new ArrayList<>();
            try {
                feedbackContextList = readFeedbackFileByStrategy(property);

            }catch (Exception e){
                log.error("当前配置地区：{}，产品id:{} 读取原始反馈文件内容报错，继续下一个地区",property.getArea(),property.getProduct_id());
                continue;
            }

            //调用反馈引擎，处理原始字段
            log.info("调用反馈引擎，处理原始字段");
            if(feedbackContextList.isEmpty()){
                log.info("原始文件无内容记录");
                continue;
            }

            for (FeedbackContext feedbackContext : feedbackContextList) {
                try{
                    //根据规则，调用反馈引擎，处理原始字段，并组装成税局需要的报文
                    commonRulesHandler.HandleRules(feedbackContext);

                }catch (Exception e){
                    e.printStackTrace();

                }
            }

        }

    }



    /**
     * 推送反馈内容到datagrid，解析内容
     * @param area
     * @return
     * @throws Exception
     */
    @Override
    public void pushFeedbackInfo(String area) throws Exception {

        //查询出当前地区所有没有反馈的数据
        QueryWrapper<ApocalypseFeedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resp_code",CommonEnum.FEEDBACK_STATUS_1111.getCode());

        //依次推送数据给到datagrid


        //更新数据库内容



/*        InvokeRequest request = invokeService.assembleRequestObject(collectContext);
        log.info("[DataInvokeService] 调用 janus-s 请求报文: {}", JSON.toJSONString(request));
        InvokeResponse response = janusSStub.invoke(request);

        log.info("[DataInvokeService] 调用 janus-s 返回报文: {}", JSON.toJSONString(response));*/

    }


    /**
     * 根据不同的策略获取文件内容
     * 放入字段：id，original_text、area
     * @param property
     * @return
     */
    private List<FeedbackContext> readFeedbackFileByStrategy(ApocalypseProperty property) throws Exception {

        List<FeedbackContext> feedbackContexts = new ArrayList<>();

        try {
            //获取对应的读取策略
            ReadFileContext fileContext = new ReadFileContext((ReadFeedbackFileStrategy) applicationContext.getBean(Class.forName(CommonEnum.getMessageByCode(property.getFeedback_strategy()))));

            //获取文件内容的每一行
            List<String> fileContextList = fileContext.contextInterface(property.getFeedback_file_path());

            //放入全局Context
            for (String context : fileContextList) {
                //生成唯一ID，入库ID
                String systemId = UUID.randomUUID().toString().replaceAll("-", "");
                FeedbackContext feedbackContext = Builder.of(FeedbackContext::new)
                        .with(FeedbackContext::setId, systemId)
                        .with(FeedbackContext::setArea, property.getArea())
                        .with(FeedbackContext::setOriginal_text, context)
                        .with(FeedbackContext::setFeedback_file_path, property.getFeedback_file_path())
                        .with(FeedbackContext::setFeedback_strategy, property.getFeedback_strategy())
                        .with(FeedbackContext::setFeedback_frequency, property.getFeedback_frequency())
                        .with(FeedbackContext::setProduct_id, property.getProduct_id())
                        .build();

                feedbackContexts.add(feedbackContext);
            }


/*            QueryWrapper<ApocalypseFeedback> wrapper = new QueryWrapper<>();
            LambdaUpdateWrapper<ApocalypseFeedback> updateWrapper = new LambdaUpdateWrapper<>();

            //先判断MD5值，如果不存在 则 入库原始内容
            for (String context : fileContextList) {
                wrapper.eq("MD5",Md5Util.getMd5(context));
                Integer i = 0;
                if((i = feedbackMapper.selectCount(wrapper)) != null && i > 0){
                    //存在相同的MD5值，不入库，修改update时间
                    updateWrapper.eq(ApocalypseFeedback::getMd5,Md5Util.getMd5(context)); //限定条件
                    ApocalypseFeedback apocalypseFeedback = new ApocalypseFeedback();
                    apocalypseFeedback.setUpdate_time(new Date());
                    feedbackMapper.update(apocalypseFeedback,updateWrapper);
                }else{

                    //组装ApocalypseFeedback 数据
                    Builder.of(ApocalypseFeedback :: new)
                            .with(ApocalypseFeedback::setId, UUID.randomUUID().toString().replaceAll("-",""))
                            .with(ApocalypseFeedback::setLrsj,new Date())
                            .with(ApocalypseFeedback::setNsrsbh,)
                            .with(ApocalypseFeedback::setOriginal_text,)
                            .build();
                    ApocalypseFeedback apocalypseFeedback = new ApocalypseFeedback();
                    feedbackMapper.insert();
                }

            }*/


        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取文件策略报错 {}，请检查数据库表apocalypse_property 配置项：FEEDBACK_STRATEGY",e.getMessage() + e);
            throw e;
        }


        return feedbackContexts;
    }


}
