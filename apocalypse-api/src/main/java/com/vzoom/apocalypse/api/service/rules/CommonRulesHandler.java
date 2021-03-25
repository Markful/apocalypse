package com.vzoom.apocalypse.api.service.rules;

import com.vzoom.apocalypse.api.config.InitContext;
import com.vzoom.apocalypse.common.dto.FeedbackContext;
import com.vzoom.apocalypse.common.repositories.FeedbackMapper;
import com.vzoom.apocalypse.common.service.FeedbackEngine;
import com.vzoom.apocalypse.common.cache.CommonCache;
import com.vzoom.apocalypse.common.entity.ApocalypseFeedback;
import com.vzoom.apocalypse.common.entity.ApocalypseProperty;
import com.vzoom.apocalypse.common.utils.FreeMarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 模板方法通用实现, 如果无法满足要求，则需要重新继承 AbstractRules 方法
 * @Author: wangyh
 * @Date: 2021/1/26
 */
@Slf4j
@Service
public class CommonRulesHandler extends AbstractRulesDecorator {

    @Autowired
    private FeedbackEngine feedbackEngine;

    @Autowired
    private FeedbackMapper feedbackMapper;


    /**
     * 调用反馈引擎，将原始数据转换成税局需要的格式
     *
     * @param feedbackContext
     */
    @Override
    public void invokeApocalypseEngine(FeedbackContext feedbackContext) throws Exception {

        try {

            feedbackEngine.exchange(feedbackContext);

        }catch (Exception e){
            log.error("反馈引擎调用报错:{}", ExceptionUtils.getMessage(e));
            throw e;
        }
    }

    /**
     * 调用模板，生成原始税局报文
     *
     * @param feedbackContext
     * @return
     */
    @Override
    public String packagingXmlFromFreemarker(FeedbackContext feedbackContext) {

        //找到符合当前地区的模板
        ApocalypseProperty property = CommonCache.PROPERTY_CACHE_MAP.get(feedbackContext.getArea());

        //调用模板方法
        FreeMarkerUtil freeMarkerUtil = InitContext.getBean("freeMarkerUtil");
        String wrapped = freeMarkerUtil.process("body", property.getTaxTemplate(), feedbackContext.getFeedbackMap());

        //将处理好的数据存入
        feedbackContext.setTreatedXml(wrapped);

        return wrapped;
    }

    /**
     * 处理好的字段存入 apocalypse_feedback 表
     *
     * @param feedbackContext
     */
    @Override
    public String saveDataToFeedback(FeedbackContext feedbackContext) {

        ApocalypseFeedback feedback = new ApocalypseFeedback();
        BeanUtils.copyProperties(feedbackContext, feedback);

        //规则处理
        feedbackMapper.insert(feedback);
        log.info("入库成功");


        return null;

    }


    /**
     * 调用datagrid 出口网关
     *
     * @param feedbackContext
     * @return
     */
    @Override
    public String invokeDatagrid(FeedbackContext feedbackContext) {


        return null;
    }

}
