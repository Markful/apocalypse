package com.vzoom.apocalypse.api.service.rules;

import com.vzoom.apocalypse.common.dto.FeedbackContext;
import com.vzoom.apocalypse.api.service.AreaRules;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 模板方法
 *
 * @Description: 反馈规则处理 模板
 * @Author: wangyh
 * @Date: 2021/1/20
 */
@Slf4j
public abstract class AbstractRulesDecorator  implements AreaRules {


    /**
     * 反馈逻辑处理 模板方法
     *
     * @param feedbackContext
     */
    @Override
    public void HandleRules(FeedbackContext feedbackContext){

        try {
            log.info("handleRulesStep1");
            //调用反馈引擎，将原始数据转换成税局需要的数据
            invokeApocalypseEngine(feedbackContext);

            //根据地区模板，组装XML报文
            log.info("handleRulesStep2");
            packagingXmlFromFreemarker(feedbackContext);

            //入库
            log.info("handleRulesStep3");
            saveDataToFeedback(feedbackContext);

        }catch (Exception e){
            log.error("反馈逻辑处理出错：{}",ExceptionUtils.getMessage(e));
        }
        //请求datagrid入口网关，反馈给税局
//        invokeDatagrid(feedbackContext);

    };

    public abstract void invokeApocalypseEngine(FeedbackContext feedbackContext) throws Exception;

    public abstract String packagingXmlFromFreemarker(FeedbackContext feedbackContext);

    public abstract String saveDataToFeedback(FeedbackContext feedbackContext);

    public abstract String invokeDatagrid(FeedbackContext feedbackContext);

}
