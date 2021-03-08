package com.vzoom.apocalypse.jiangsu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vzoom.apocalypse.common.dto.InvokePostloanRequest;
import com.vzoom.apocalypse.common.entity.ApocalypseAfterLoan;
import com.vzoom.apocalypse.common.enums.CommonEnum;
import com.vzoom.apocalypse.common.enums.CommonResponseInfo;
import com.vzoom.apocalypse.common.repositories.*;
import com.vzoom.apocalypse.common.dto.InvokeRequest;
import com.vzoom.apocalypse.common.dto.InvokeResponse;
import com.vzoom.apocalypse.common.service.InvokeService;
import com.vzoom.apocalypse.common.cache.CommonCache;
import com.vzoom.apocalypse.common.entity.ApocalypseFeedback;
import com.vzoom.apocalypse.common.entity.ApocalypseProperty;
import com.vzoom.apocalypse.common.utils.ConvertUtils;
import com.vzoom.apocalypse.common.utils.DateUtil;
import com.vzoom.apocalypse.common.utils.HttpClientUtil;
import com.vzoom.apocalypse.jiangsu.dto.JiangsuFeedbackDTO;
import com.vzoom.apocalypse.jiangsu.dto.JiangsuFeedbackDownloadDTO;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 自行实现组装报文和推送的逻辑
 * 这块需要定制化，各自形成jar包
 * @Author:wangyh
 */
@Slf4j
@Service
public class JiangsuFeedbackServiceImpl implements InvokeService {

    @Autowired
    private ConvertUtils convertUtils;

    @Value("${jiangsu.sftp.username}")
    private String username;

    @Value("${jiangsu.sftp.password}")
    private String password;

    @Value("${invoke.datagrid.feedback.url}")
    private String invokeUrl;

    @Value("${jiangsu.feedback.request.path}")
    private String feedbackPathPrefix;

    @Value("${jiangsu.feedback.response.path}")
    private String feedbackPathResPrefix;

    @Value("${jiangsu.feedback.response.path}")
    private String feedbackResponsePath;

    @Autowired
    private FeedbackMapper feedbackMapper;


    @Override
    public String getArea() {
        return "jiangsu";
    }

    /**
     * 推送反馈数据给到datagrid
     * @param feedbackList
     */
    @Override
    public InvokeResponse pushFeedbackRequest(List<ApocalypseFeedback> feedbackList) {

        JiangsuFeedbackDTO feedbackDTO = new JiangsuFeedbackDTO();
        //判空
        if(CollectionUtils.isEmpty(feedbackList)){
            log.info("当前区域 反馈数据为空:{}",getArea());
            return null;
        }

        ApocalypseFeedback tempFeedback = new ApocalypseFeedback();
        StringBuilder sb = new StringBuilder();
        Map<String, String> bankTaxParamMap = CommonCache.BANK_TAX_PARAM_MAP.get(getArea());
        sb.append("{\"bankId\":\"").append(bankTaxParamMap.get("bankid")).append("\",\"content\":[");


        for (ApocalypseFeedback apocalypseFeedback : feedbackList) {
            //江苏的反馈是一次反馈所有的数据，需要先处理requestData
            if(null != JSONObject.parseObject(apocalypseFeedback.getTreatedJson())){
                sb.append(apocalypseFeedback.getTreatedJson()).append(",");
            }

        }
        String treatedContent = sb.toString().substring(0, sb.length() - 1) + "]}";

        String nowDate = LocalDate.now().format(DateTimeFormatter.ofPattern(DateUtil.DATE_FMT_0));
        String ftpFilePath = feedbackPathPrefix + nowDate + "/" ;
        String fileName = nowDate + "_" + new Random().nextInt(10000) + ".json";

        feedbackDTO.setContent(treatedContent);
        feedbackDTO.setFilename(fileName);
        feedbackDTO.setIsUpload("1");
        feedbackDTO.setPath(ftpFilePath);

        JSONObject jsonObject = JSON.parseObject(feedbackDTO.toString());
        log.info("jiangsu feedback jsonObject = {}",jsonObject);

        //组装参数
        tempFeedback.setId(feedbackList.get(0).getId());
        tempFeedback.setArea(feedbackList.get(0).getArea());
        tempFeedback.setTreatedXml(feedbackDTO.toString());
        InvokeRequest invokeRequest = assembleFeedbackRequestObject(tempFeedback);

        //推送数据
        log.info("[JiangsuFeedbackService] 调用 datagrid 请求报文: {}", JSON.toJSONString(invokeRequest));
        String response = HttpClientUtil.postJson(invokeUrl, JSON.toJSONString(invokeRequest));
        log.info("[JiangsuFeedbackService] 调用 datagrid 返回报文: {}", response);

        String responseCode = "";
        String responseMsg = "";
        //提取参数{"responseCode":"000000","responseMsg":"成功","data":null}
        if(CommonResponseInfo.SUCCESS.getCode().equals(JSONObject.parseObject(response).get("responseCode"))){
            responseCode = CommonEnum.FEEDBACK_STATUS_2222.getCode();
            responseMsg = CommonEnum.FEEDBACK_STATUS_2222.getMessage();
        }else{
            responseCode = String.valueOf(JSONObject.parseObject(response).get("responseCode"));
            responseMsg = String.valueOf(JSONObject.parseObject(response).get("responseMsg"));
        }

        //更新数据库
        log.info("批量更新 江苏 数据库记录");
        for (ApocalypseFeedback feedback : feedbackList) {
            feedback.setRespCode(responseCode);
            feedback.setRespMsg(responseMsg);
            feedbackMapper.updateById(feedback);
        }
        log.info("批量更新 江苏 数据库记录完成");


        return JSONObject.toJavaObject(JSON.parseObject(response), InvokeResponse.class);
    }

    /**
     * 默认实现，返回null
     * @param apocalypseAfterLoan
     * @return
     */
    @Override
    public InvokePostloanRequest assemblePostlaonRequest(ApocalypseAfterLoan apocalypseAfterLoan) {
        return null;
    }

    /**
     *
     * @param area 地区
     * @param feedbackList 所有的反馈数据，自行过滤出贷后名单
     * @return
     */
    @Override
    public List<ApocalypseFeedback> getPostloanList(String area, List<ApocalypseFeedback> feedbackList) {
        return null;
    }


    /**
     * 处理 反馈响应文件（当天日期处理的是昨天的反馈记录）
     * 文件目录：/tax/feedback/response/当天日期/feedback_当天日期时间yyyyMMddHHmmSS.xml
     * 文件内容：(加密){"bankId":"","content":[{"nsrsbh":"","flag":""},{}]},成功反馈税局=0，失败=-1，对应反馈表，成功为2，失败为-1
     * 注意反馈请求是上传到T-1日的目录，但是响应文件放的是T日的目录
     */
    @Scheduled(cron = "${jiangsu.postloan.schedule.corn}")
    @SchedulerLock(name = "jiangsu_feedback_handle", lockAtLeastFor = "15m", lockAtMostFor = "30m")
    public void handleFeedbackResponse(){

        JiangsuFeedbackDownloadDTO feedbackDownloadDTO = new JiangsuFeedbackDownloadDTO();
        String nowDate = LocalDate.now().format(DateTimeFormatter.ofPattern(DateUtil.DATE_FMT_0));
        String ftpFilePath = feedbackPathResPrefix + nowDate + "/" ;
        feedbackDownloadDTO.setPath(ftpFilePath);

        //组装datagrid报文
        ApocalypseProperty property = CommonCache.PROPERTY_CACHE_MAP.get(getArea());
        Map<String, String> wrapMap = new HashMap<>(4);
        wrapMap.put("username",username);
        wrapMap.put("password",password);
        InvokeRequest request = new InvokeRequest();
        request.setMinistryCode(property.getMinistryCode());
        request.setHttpMethod("1");
        request.setRequestType(property.getRequestType());
        request.setRequestUrl(convertUtils.getInvokeUrl(property, wrapMap));
        request.setResponseType(property.getResponseType());
        request.setEncryptAlgorithm("");
        request.setEncryptedField("");
        request.setRequestData(feedbackDownloadDTO.toString());

        //调用datagrid获取文件列表
        log.info("[jiangsu获取反馈文件列表] 调用 datagrid 请求报文: {}", JSON.toJSONString(request));
        String response = HttpClientUtil.postJson(invokeUrl, JSON.toJSONString(request));
        log.info("[jiangsu获取反馈文件列表] 调用 datagrid 返回报文: {}", response);

        //解析
        String responseCode = "";
        String responseMsg = "";
        List<String> dataList = new ArrayList<>();
        JSONObject responseJson = JSONObject.parseObject(response);
        //提取参数{"responseCode":"000000","responseMsg":"成功","data":["xxxxx.xml","aaaaa.xml","ccccc.xml"]}
        if(CommonResponseInfo.SUCCESS.getCode().equals(responseJson.get("responseCode"))){
            JSONArray jsonArray = JSON.parseArray(responseJson.get("data").toString());
            dataList = JSONObject.parseArray(jsonArray.toJSONString()).toJavaList(String.class);
        }

        if(CollectionUtils.isEmpty(dataList)){
            log.info("当前目录下无反馈响应文件：{},更新反馈响应文件流程结束！",ftpFilePath);
            return;
        }

        //获取每一个反馈响应文件，并处理更新入库
        for (String fileName : dataList) {
            JiangsuFeedbackDTO feedbackDTO = new JiangsuFeedbackDTO();
            feedbackDTO.setContent("");
            feedbackDTO.setFilename(fileName);
            feedbackDTO.setIsUpload("0");//0-下载
            feedbackDTO.setPath(ftpFilePath);

            JSONObject jsonObject = JSON.parseObject(feedbackDTO.toString());
            log.info("jiangsu feedback jsonObject = {}",jsonObject);

            //组装参数
            ApocalypseFeedback tempFeedback = new ApocalypseFeedback();
            tempFeedback.setId(UUID.randomUUID().toString());
            tempFeedback.setArea(getArea());
            tempFeedback.setTreatedXml(feedbackDTO.toString());
            InvokeRequest invokeRequest = assembleFeedbackRequestObject(tempFeedback);

            //推送数据
            log.info("[jiangsu获取反馈响应文件内容] 调用 datagrid 请求报文: {}", JSON.toJSONString(invokeRequest));
            String responseData = HttpClientUtil.postJson(invokeUrl, JSON.toJSONString(invokeRequest));
            log.info("[jiangsu获取反馈响应文件内容] 调用 datagrid 返回报文: {}", response);

            //提取参数{"responseCode":"000000","responseMsg":"成功","data":null}
            if(CommonResponseInfo.SUCCESS.getCode().equals(JSONObject.parseObject(response).get("responseCode"))){
                responseCode = CommonEnum.FEEDBACK_STATUS_2222.getCode();
                responseMsg = CommonEnum.FEEDBACK_STATUS_2222.getMessage();
            }else{
                responseCode = String.valueOf(JSONObject.parseObject(response).get("responseCode"));
                responseMsg = String.valueOf(JSONObject.parseObject(response).get("responseMsg"));
            }

            //更新数据库
            log.info("批量更新 江苏 数据库记录");
/*            for (ApocalypseFeedback feedback : feedbackList) {
                feedback.setRespCode(responseCode);
                feedback.setRespMsg(responseMsg);
                feedbackMapper.update();
            }*/
            log.info("批量更新 江苏 数据库记录完成");


        }







        //解析文件


        //更新数据库


/*        JiangsuFeedbackDTO feedbackDTO = new JiangsuFeedbackDTO();
        feedbackDTO.setContent("");
        feedbackDTO.setFilename(fileName);
        feedbackDTO.setIsUpload("1");
        feedbackDTO.setPath(feedbackResponsePath);

        ApocalypseFeedback tempFeedback = new ApocalypseFeedback();
        tempFeedback.setId(feedbackList.get(0).getId());
        tempFeedback.setArea(feedbackList.get(0).getArea());
        tempFeedback.setTreatedXml(feedbackDTO.toString());
        InvokeRequest invokeRequest = assembleRequestObject(tempFeedback);*/



    }


    /**
     * 组装请求datagrid的报文
     * @param apocalypseFeedback
     * @return
     */
    @Override
    public InvokeRequest assembleFeedbackRequestObject(ApocalypseFeedback apocalypseFeedback) {
        String area = apocalypseFeedback.getArea();

        ApocalypseProperty property = CommonCache.PROPERTY_CACHE_MAP.get(area);
        Map<String, String> wrapMap = new HashMap<>();
        wrapMap.put("username",username);
        wrapMap.put("password",password);
        InvokeRequest request = new InvokeRequest();
        request.setMinistryCode(property.getMinistryCode());
        request.setRequestData(apocalypseFeedback.getTreatedXml());
        request.setRequestId(apocalypseFeedback.getId());
        request.setEncryptedField(property.getEncryptedField());
        request.setDecryptedField(property.getDecryptedField());
        request.setFilledField(property.getFilledField());
        request.setSignatureField(property.getSignatureField());
        request.setEncryptAlgorithm(property.getEncryptAlgorithm());
        request.setSignatureAlgorithm(property.getSignatureAlgorithm());
        request.setRequestUrl(convertUtils.getInvokeUrl(property, wrapMap));
        request.setRequestType(property.getRequestType());
        request.setResponseType(property.getResponseType());
        request.setHttpMethod("1");

        return request;
    }



}
