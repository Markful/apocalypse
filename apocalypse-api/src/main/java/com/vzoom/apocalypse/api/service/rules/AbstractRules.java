package com.vzoom.apocalypse.api.service.rules;

import com.vzoom.apocalypse.api.dto.FeedbackContext;
import com.vzoom.apocalypse.api.service.AreaRules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description: 反馈规则处理抽象类
 * @Author: wangyh
 * @Date: 2021/1/20
 */
@Service
@Slf4j
public abstract class AbstractRules implements AreaRules {


    /**
     * 反馈逻辑处理抽象类
     * @param feedbackContext
     */
    @Override
    public void HandleRules(FeedbackContext feedbackContext){

        //TODO 获取对应配置



        //TODO 根据地区模板，组装XML报文




        //请求datagrid入口网关




    };



}
