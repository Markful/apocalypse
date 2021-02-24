package com.vzoom.apocalypse.api.service.rules;

import com.vzoom.apocalypse.api.dto.FeedbackContext;
import com.vzoom.apocalypse.common.cache.CommonCache;
import com.vzoom.apocalypse.common.entity.ApocalypseFeedback;
import com.vzoom.apocalypse.api.repository.FeedbackMapper;
import com.vzoom.apocalypse.api.repository.PropertyMapper;
import com.vzoom.apocalypse.api.service.FeedbackEngine;
import com.vzoom.apocalypse.common.entity.ApocalypseProperty;
import com.vzoom.apocalypse.common.utils.FreeMarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 模板方法通用实现
 * @Author: wangyh
 * @Date: 2021/1/26
 */
@Slf4j
public class CommonRulesHandler extends AbstractRulesDecorator {

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private FeedbackEngine feedbackEngine;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private FreeMarkerUtil freeMarkerUtil;

    /**
     * 通用处理逻辑
     * 如果无法满足要求，则需要重新继承 AbstractRules 方法
     * @param feedbackContext
     */


    /**
     * 调用反馈引擎，将原始数据转换成税局需要的格式
     *
     * @param feedbackContext
     */
    @Override
    public void invokeApocalypseEngine(FeedbackContext feedbackContext) {

        try {
            feedbackEngine.exchange(feedbackContext);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 调用模板，生成原始税局报文
     * @param feedbackContext
     * @return
     */
    @Override
    public String packagingXmlFromFreemarker(FeedbackContext feedbackContext) {

        //找到符合当前地区的模板
        ApocalypseProperty collect = CommonCache.PROPERTY_CACHE_LIST.stream().filter(x -> x.getArea().equals(feedbackContext.getArea())
                && x.getMinistry_code().equals(feedbackContext.getMinistry_code())
                && x.getProduct_id().equals(feedbackContext.getProduct_id())
                && x.getTemplate_type().equals(feedbackContext.getTemplate_type())).findFirst().get();

        //调用模板方法
        String wrapped = freeMarkerUtil.process("body", collect.getRequest_template(), feedbackContext.getFeedbackMap());

        //将处理好的数据存入
        feedbackContext.setTreated_xml(wrapped);

        return null;
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
