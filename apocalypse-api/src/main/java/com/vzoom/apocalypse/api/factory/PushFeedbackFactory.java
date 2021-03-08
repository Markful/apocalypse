package com.vzoom.apocalypse.api.factory;

import com.vzoom.apocalypse.common.service.InvokeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 推送反馈数据工厂方法
 * @Author: wangyh
 * @Date: 2021/3/1
 */
@Slf4j
@Component
public class PushFeedbackFactory implements ApplicationContextAware {

    private static Map<String, InvokeService> pushFeedbackBeanMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, InvokeService> map = applicationContext.getBeansOfType(InvokeService.class);
        pushFeedbackBeanMap = new HashMap<>(64);
        map.forEach((key, value) -> pushFeedbackBeanMap.put(value.getArea(), value));



    }

    /**
     * 根据地区获取对应的推送反馈实现类
     * @param area
     * @param <T>
     * @return
     */
    public static <T extends InvokeService> T getPushFeedbackInvoke(String area) {
        return (T)pushFeedbackBeanMap.get(area);
    }

}
