package com.vzoom.apocalypse.engine;

import com.alibaba.fastjson.JSONObject;
import com.vzoom.apocalypse.common.dto.FeedbackContext;
import com.vzoom.apocalypse.common.cache.CommonCache;
import com.vzoom.apocalypse.common.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.common.service.FeedbackEngine;
import com.vzoom.apocalypse.common.constants.Constants;
import com.vzoom.apocalypse.common.enums.CommonEnum;
import com.vzoom.apocalypse.common.utils.ConvertUtils;
import com.vzoom.apocalypse.common.utils.DateUtil;
import com.vzoom.apocalypse.common.utils.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vzoom.apocalypse.common.cache.CommonCache.AREAFIELD_PROPERTIES_CACHE;
import static com.vzoom.apocalypse.common.cache.CommonCache.NSRSBH_INDEX;
import static com.vzoom.apocalypse.common.constants.Constants.*;
import static java.util.regex.Pattern.compile;


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
     *
     * @param context
     * @return
     */
    @Override
    public void exchange(FeedbackContext context) throws Exception {

        //实际反馈字段
        String originalText = context.getOriginalText();

        //自动识别 反馈文件 分隔符
        String separatorRegex = ConvertUtils.findSeparator(originalText,context.getArea());
        log.info("当前分隔符为：{}", separatorRegex);
        String[] feedbackArray = originalText.split(separatorRegex);

        //配置文件的字段顺序,字段来源：apocalypse_property.FEEDBACK_PARAM
        String[] feedbackCol = AREAFIELD_PROPERTIES_CACHE.get(context.getArea());

        //检查配置个数和实际字段个数是否一致
        if (feedbackArray.length != feedbackCol.length) {
            throw new Exception("反馈字段个数与配置个数不相符:" + feedbackArray.length + ":" + feedbackCol.length);
        }

        //找出税号
        context.setNsrsbh(feedbackArray[NSRSBH_INDEX.get(context.getArea())]);

        Map<String, String> feedbackMap = new HashMap<>(feedbackCol.length + 1);
        for (int i = 0; i < feedbackCol.length; i++) {
            //需要将key全部改成小写
            feedbackMap.put(ConvertUtils.toLowerCase(feedbackCol[i]), feedbackArray[i]);
        }

        List<ApocalypseAreaRules> apocalypseAreaRules = CommonCache.RULES_MAP.get(context.getArea());
        for (ApocalypseAreaRules apocalypseAreaRule : apocalypseAreaRules) {
            if (StringUtils.isEmpty(apocalypseAreaRule.getRuleExpression())) {
                //如果EL表达式为空，则不进行修改
                continue;
            }
            String key = ConvertUtils.toLowerCase(apocalypseAreaRule.getFeedbackField());
            //EL表达式处理对应的字段，并放入新的Map中
            String originalField = feedbackMap.get(key);

            //调用引擎
            String treatedField = this.invokeEngine(originalField, apocalypseAreaRule.getRuleExpression());

            //替换处理后的字段
            feedbackMap.put(key, treatedField);

        }

        context.setFeedbackMap(feedbackMap);

        //处理后的MD5值存入
        String treatedJson = JSONObject.toJSONString(feedbackMap);
        context.setMd5(Md5Util.getMd5(treatedJson));

        context.setTreatedJson(treatedJson);
        context.setRespCode(CommonEnum.FEEDBACK_STATUS_1111.getCode());
        context.setRespMsg(CommonEnum.FEEDBACK_STATUS_1111.getMessage());

    }

    /**
     * 根据EL表达式处理数据
     * 规则：
     * 1、常量：直接赋值
     * 2、表达式：#{表达式内容}
     * 3、
     *
     * @param originalField   字段值
     * @param expressionArray 表达式组合
     *     自定义函数：
     *     choose('defaultValue','exp1','return1','exp2','return2',.....)：比较参数后返回指定数据：字段值orgValue和exp1,exp2,exp3..比较，
     *                              找到相同的，则返回对应的value值，都没有比对上则返回defaultValue
     *     sql() ：执行SQL
     *     没有任何函数 ：常量
     *     trim_s0() : 去掉首尾空格
     *     trim_s1() : 去掉所有空格
     *     trim_0() : 去掉前面的0，如果有符号，保留符号
     *     trim_1() : 去掉末尾小数点
     *     trim_2(n) : 保留n位小数点，默认2位
     *     trim_3(n) : 元转换成万元，保留n位小数，默认2位，eg:trim_3(3),54321 ->5.432
     *     trim_4(n) : 万元转换成元，保留n位小数,默认2位
     *     replace(org,target) : 将org转换成target，并且支持正则。
     *
     *     日期转换
     *     date_0(yyyy-mm-dd) : 将当前系统时间转换为指定格式
     *     date_1(yyyy-MM-dd) : 转换时间为指定格式。
     *     date_2(格式A,格式B) : 格式转换，支持互转(yyyymmdd,yyyy-mm-dd)(yyyy-mm-dd,yyyy-mm-dd hh:mm:ss)(yyyymmdd,yyyy-mm-dd hh:mm:ss)
     *
     * @return
     */
    public String invokeEngine(String originalField, String expressionArray) throws Exception {

        if (StringUtils.isEmpty(originalField)) {
            log.info("原始字段为空");
            return originalField;
        }

        // 创建一个ExpressionParser对象，用于解析表达式
        ExpressionParser parser = new SpelExpressionParser();
        //创建上下文
        EvaluationContext context = new StandardEvaluationContext();


        String[] expressionList = expressionArray.split(";");
        //根据表达式组合的顺序，从左往后执行函数，且每个函数的入参 都是 前一个函数的结果
        for (int i = 0; i < expressionList.length; i++) {
            String expression = expressionList[i];
            if (StringUtils.isEmpty(expression)) {
                continue;
            }

            if (expression.contains(SPEL_METHOD_CHOOSE)) {
                String[] split = expression.split(SPEL_METHOD_CHOOSE + "\\(");
                expression = SPEL_METHOD_CHOOSE + "(" + originalField + "," + split[1];
                originalField = parser.parseExpression(expression,null).getValue(this, String.class);
                continue;
            }

            //去掉首尾空格
            if (expression.contains(SPEL_METHOD_TRIM_S0)) {
                originalField = originalField.trim();
                continue;
            }

            //去掉所有空格
            if (expression.contains(SPEL_METHOD_TRIM_S1)) {
                originalField = originalField.replaceAll("\\s", "");
                continue;
            }


            //格式化，去掉数字前面的0
            if (expression.contains(SPEL_METHOD_TRIM_0)) {
                //+00100.00 -> +100.00
                //00100.23 -> 100.23
                originalField = originalField.replaceAll("^(\\-)(0+)", "-").replaceAll("^(\\+)(0+)", "+").replaceAll("^(0+)", "");
                continue;
            }

            //去掉末尾小数点
            if (expression.contains(SPEL_METHOD_TRIM_1)) {
                //100.00 -> 100
                //100.23 -> 100
                originalField = originalField.replaceAll("\\.[0-9]+", "");
                continue;
            }

            //保留小数
            if (expression.contains(SPEL_METHOD_TRIM_2)) {
                String[] split = expression.split(SPEL_METHOD_TRIM_2 + "\\(");
                expression = SPEL_METHOD_TRIM_2 + "(" + originalField + "," + split[1];
                originalField = parser.parseExpression(expression).getValue(this, String.class);

                continue;
            }

            //元->万元，保留小数
            if (expression.contains(SPEL_METHOD_TRIM_3)) {
                String[] split = expression.split(SPEL_METHOD_TRIM_3 + "\\(");
                expression = SPEL_METHOD_TRIM_3 + "(" + originalField + "," + split[1];
                originalField = parser.parseExpression(expression).getValue(this, String.class);

                continue;
            }

            //万元->元，保留小数0
            if (expression.contains(SPEL_METHOD_TRIM_4)) {
                String[] split = expression.split(SPEL_METHOD_TRIM_4 + "\\(");
                expression = SPEL_METHOD_TRIM_4 + "(" + originalField + "," + split[1];
                originalField = parser.parseExpression(expression).getValue(this, String.class);

                continue;
            }

            //字段替换
            if (expression.contains(SPEL_METHOD_REPLACE_0)) {
                String[] split = expression.split(",", 2);
                originalField = replace_0(originalField, split[0], split[1]);
            }


            //将当前系统时间转成指定格式
            if (expression.contains(SPEL_METHOD_DATE_0)) {
                String dateformat = "yyyy-MM-dd";
                Pattern pattern = compile("\\((.*?)\\)");
                Matcher m = pattern.matcher(expression);
                if (m.find()) {
                    dateformat = m.group(1);
                }

                originalField = DateUtil.formatLocalDateTimeToString(DateUtil.getCurrentLocalDateTime(),
                        dateformat.replaceAll("'", "").replaceAll("\"", ""));

                continue;
            }

            //将入参时间转换成指定格式
            if (expression.contains(SPEL_METHOD_DATE_1)) {
                String[] split = expression.split(SPEL_METHOD_DATE_1 + "\\(");
                expression = SPEL_METHOD_DATE_1 + "(" + originalField + "," + split[1];
                originalField = parser.parseExpression(expression).getValue(this, String.class);

                continue;
            }

            //日期格式互转
            if (expression.contains(SPEL_METHOD_DATE_2)) {
                String[] split = expression.split(SPEL_METHOD_DATE_2 + "\\(");
                expression = SPEL_METHOD_DATE_2 + "(" + originalField + "," + split[1];
                originalField = parser.parseExpression(expression).getValue(this, String.class);

                continue;
            }

        }


        return originalField;
    }

    /**
     * 保留小数
     * 100 -> 100.00
     * 123.2 -> 123.20
     * 123.2456 -> 123.25
     * @param originalField
     * @param accuracy
     * @return
     */
    public String trim_2(String originalField, String accuracy) {
        int count;
        if (StringUtils.isEmpty(accuracy)) {
            count = ACCURACY;
        } else {
            count = Integer.parseInt(accuracy);
        }

        if (count < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else if (count == 0) {
            originalField = new DecimalFormat("0").format(Double.valueOf(originalField));
        } else {
            StringBuilder formatStr = new StringBuilder("0.");
            for (int k = 0; k < count; k++) {
                formatStr.append("0");
            }
            originalField = new DecimalFormat(formatStr.toString()).format(Double.valueOf(originalField));
        }
        return originalField;
    }

    /**
     * 万元——>元，并保留几位小数
     * 123456 -> 12.35
     * @param originalField
     * @param accuracy 精度
     * @return
     */
    public String trim_3(String originalField, String accuracy) {
        int count;
        if (StringUtils.isEmpty(accuracy)) {
            count = ACCURACY;
        } else {
            count = Integer.parseInt(accuracy);
        }

        if (count < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else if (count == 0) {
            originalField = new DecimalFormat("0").format(Double.parseDouble(originalField) / 10000);
        } else {
            StringBuilder formatStr = new StringBuilder("0.");
            for (int k = 0; k < count; k++) {
                formatStr.append("0");
            }
            originalField = new DecimalFormat(formatStr.toString()).format(Double.parseDouble(originalField) / 10000);
        }

        return originalField;
    }

    /**
     * 元——>万元，保留几位小数
     * 1234567 ——> 123.46
     * @param originalField
     * @param accuracy 精度
     * @return
     */
    public String trim_4(String originalField, String accuracy) {
        int count;
        if (StringUtils.isEmpty(accuracy)) {
            count = ACCURACY;
        } else {
            count = Integer.parseInt(accuracy);
        }

        if (count < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else if (count == 0) {
            originalField = new DecimalFormat("0").format(Double.parseDouble(originalField) * 10000);
        } else {
            StringBuilder formatStr = new StringBuilder("0.");
            for (int k = 0; k < count; k++) {
                formatStr.append("0");
            }
            originalField = new DecimalFormat(formatStr.toString()).format(Double.parseDouble(originalField) * 10000);
        }

        return originalField;
    }


    /**
     * 替换字段
     * 12个月：replace_0(个月,) -->12
     * 12.000:replace_0(.000,1) -> 121
     * 12个月：replace_0(^[\u4e00-\u9fa5]+$,月) -->12月
     * @param originalField
     * @param orgRegex
     * @param target
     * @return
     */
    public String replace_0(String originalField, String orgRegex, String target){
        String temp = "";
        if(orgRegex.startsWith("^") && orgRegex.endsWith("$")){
            //正则
            Pattern pattern = Pattern.compile(orgRegex);
            Matcher m = pattern.matcher(originalField.replaceAll("\n","").replaceAll("\r",""));
            if(m.find()){
                temp = m.group(1);
            }
            originalField = originalField.replaceAll(temp,target);

        }else{
            //普通替换
            originalField = originalField.replaceAll(orgRegex,target);
        }

        return originalField;
    }


    public String date_1(String originalField, String dateFormat) throws Exception {
        if (StringUtils.isEmpty(dateFormat)) {
            dateFormat = "yyyy-MM-dd";
        } else {
            dateFormat = dateFormat.replaceAll("'", "").replaceAll("\"", "");
        }

        LocalDateTime localDateTime = DateUtil.stringToLocalDateTime(originalField, dateFormat);
        if (null == localDateTime) {
            throw new Exception("函数：" + SPEL_METHOD_DATE_1 + "解析报错");
        }

        return DateUtil.formatLocalDateTimeToString(localDateTime, dateFormat);
    }


    public String date_2(String originalField, String dateFormat1, String dateFormat2) throws Exception {

        LocalDateTime localDateTime = DateUtil.stringToLocalDateTime(originalField, dateFormat1);
        if (null == localDateTime) {
            throw new Exception("函数：" + SPEL_METHOD_DATE_2 + "解析报错");
        }

        return DateUtil.formatLocalDateTimeToString(localDateTime, dateFormat2);
    }

    public String choose(String target, String defaultValue, String... arg) {
        //0,1,2,3
        //1,3,5,7
        int length = arg.length;
        if (length == 0 || length % 2 != 0) {
            return defaultValue;
        } else {
            int n = 0;
            while (2 * n + 1 <= length) {
                if (target.equals(arg[2 * n])) {
                    return arg[2 * n + 1];
                }
                n++;
            }
            return defaultValue;
        }

    }

}
