package com.vzoom.apocalypse.api.service.impl;

import com.vzoom.apocalypse.api.config.CacheConfig;
import com.vzoom.apocalypse.api.dto.FeedbackContext;
import com.vzoom.apocalypse.api.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.api.service.FeedbackEngine;
import com.vzoom.apocalypse.common.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vzoom.apocalypse.common.cache.CommonCache.areafieldPropertiesCache;


/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/2/20
 */
@Slf4j
@Service
public class FeedbackEngineImpl implements FeedbackEngine {



    /**
     * 反馈引擎核心方法
     * @param context
     * @return
     */
    @Override
    public void exchange(FeedbackContext context) throws Exception {

        String originalText = context.getOriginal_text();

        //自动识别 反馈文件 分隔符
        String separatorRegex = ConvertUtils.findSeparator(originalText);
        log.info("当前分隔符为：{}",separatorRegex);

        //根据缓存中存入的字段顺序，依次调用引擎组装参数
        String[] feedbackArray = originalText.split(separatorRegex);

        //解析配置文件的字段顺序
        String propertyField = areafieldPropertiesCache.get(context.getArea());
        String separatorRegex2 = ConvertUtils.findSeparator(propertyField);
        String[] feedbackCol = originalText.split(separatorRegex2);

        //检查配置个数和实际字段个数是否一致
        if(feedbackArray.length != feedbackCol.length){
            throw new Exception("反馈字段个数与配置个数不相符");
        }

        Map<String, String> feedbackMap = new HashMap<>(feedbackCol.length + 1);
        for (int i = 0; i < feedbackCol.length; i++) {
            //需要将key全部改成小写
            feedbackMap.put(ConvertUtils.toLowerCase(feedbackCol[i]),feedbackArray[i]);
        }


        List<ApocalypseAreaRules> apocalypseAreaRules = CacheConfig.RULES_MAP.get(context.getArea());
        for (ApocalypseAreaRules apocalypseAreaRule : apocalypseAreaRules) {
            if(apocalypseAreaRule.getRule_expression().isEmpty()){
                //如果EL表达式为空，则不进行修改
                continue;
            }

            //EL表达式处理对应的字段，并放入新的Map中
            String originalField = feedbackMap.get(ConvertUtils.toLowerCase(apocalypseAreaRule.getFeedback_field()));

            String treatedField = invokeEngine(originalField,apocalypseAreaRule.getRule_expression());


        }





    }

    /**
     * 根据EL表达式处理数据
     * @param originalField
     * @return
     */
    private String invokeEngine(String originalField,String expression) {

        // 创建一个ExpressionParser对象，用于解析表达式
        ExpressionParser parser = new SpelExpressionParser();

        //

    }


}
