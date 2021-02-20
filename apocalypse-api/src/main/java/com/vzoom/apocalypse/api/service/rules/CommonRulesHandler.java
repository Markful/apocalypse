package com.vzoom.apocalypse.api.service.rules;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vzoom.apocalypse.api.dto.FeedbackContext;
import com.vzoom.apocalypse.api.entity.ApocalypseFeedback;
import com.vzoom.apocalypse.api.entity.ApocalypseProperty;
import com.vzoom.apocalypse.api.repository.PropertyMapper;
import com.vzoom.apocalypse.api.service.AreaRules;
import com.vzoom.apocalypse.api.service.FeedbackEngine;
import com.vzoom.apocalypse.api.service.HandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        feedbackEngine.exchange(feedbackContext);





    }

    @Override
    public String packagingXmlFromFreemarker(FeedbackContext feedbackContext) {
        return null;
    }

    @Override
    public String invokeDatagrid(FeedbackContext feedbackContext) {
        return null;
    }

}
