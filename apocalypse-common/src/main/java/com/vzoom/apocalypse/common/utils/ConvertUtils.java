package com.vzoom.apocalypse.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.vzoom.apocalypse.common.entity.ApocalypseProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.vzoom.apocalypse.common.cache.CommonCache.AREA_SEPARATOR_CACHE;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/20
 */
@Slf4j
@Component
public class ConvertUtils {


    @Autowired
    private FreeMarkerUtil freeMarkerUtil;

    /**
     * @Author: chenlei
     * @Description: 组装数据源的url
     * @Date: 2020-11-05 17:51
     * @return: java.lang.String
     * @Param datasourceMinistryConfig:
     * @Param datasourceApi:
     */
    public String getInvokeUrl(ApocalypseProperty feedbackProperty, Map wrappedReqMap) {
        String protocol;
        switch (feedbackProperty.getTransportType()) {
            case "0":
                protocol = "file://";
                break;
            case "1":
                protocol = "http://";
                break;
            case "2":
                protocol = "https://";
                break;
            case "3":
                protocol = "ftp://";
                break;
            case "4":
                protocol = "sftp://";
                break;
            case "5":
                protocol = "other";
                break;
            default:
                protocol = "http";
        }

        String ip = feedbackProperty.getInvokeIp() == null ? feedbackProperty.getInvokeDomain() : feedbackProperty.getInvokeIp();
        if (StringUtils.isEmpty(ip)) {
            log.info("[apocalypse_property] ip/domain 配置 为空, area: {}", feedbackProperty.getArea());
            return null;
        }
        StringBuilder urlBuffer = new StringBuilder();
        urlBuffer.append(protocol).append(ip);
        if (!StringUtils.isEmpty(feedbackProperty.getInvokePort())) {
            urlBuffer.append(":").append(feedbackProperty.getInvokePort());
        }
        if (!StringUtils.isEmpty(feedbackProperty.getInvokeUri())) {
            urlBuffer.append(feedbackProperty.getInvokeUri());
        }

        String url = urlBuffer.toString();
        // 填充url参数
        url = freeMarkerUtil.process("urlFill", url, wrappedReqMap);

        log.info("[组装调用datagrid参数] 请求 URI : {}",  url);
        return url.trim();

    }

    public static String replaceDate(String str) {
        str = str.replaceAll("\\{yyyyMMdd}", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .replaceAll("\\{yyyyMM}",LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")))
                .replaceAll("\\{yyyyMMdd-1}",LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .replaceAll("\\{yyyyMM-1}",LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM")));
        return str;
    }

    public static Map fromJsonToMap(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map<String, Object> valueMap = new HashMap<>(8);
        valueMap.putAll(jsonObject);
        return valueMap;
    }

    /**
     * 将字段全部改成小写
     * @param text
     * @return
     */
    public static String toLowerCase(String text){

        return text.toLowerCase();
    }

    /**
     * 将字段全部改成小写
     * @param text
     * @return
     */
    public static String toUpperCase(String text){

        return text.toUpperCase();
    }

    /**
     * 截取指定长度字符串，不足的直接返回原字符串
     * @param msg
     * @param length
     * @return
     */
    public static String subString(String msg,int length){

        if(msg.length() <= length){
            return msg;
        }

        return msg.substring(0,length);
    }

    /**
     * 传入正则表达式，找出字符串中的分隔符
     * @return 转义后的分隔符，带\
     *
     */
    public static String findSeparator(String str,String area){

        if(AREA_SEPARATOR_CACHE.containsKey(area)){
            //返回个性化分隔符
            return AREA_SEPARATOR_CACHE.get(area);
        }

        String[] split = str.split("[A-Za-z0-9]+");
        StringBuilder sb = new StringBuilder();

        if(!split[0].isEmpty()){
            char[] chars = split[0].toCharArray();
            for (char aChar : chars) {
                sb.append("\\").append(aChar);
            }

        }else{
            char[] chars = split[1].toCharArray();
            for (char aChar : chars) {
                sb.append("\\").append(aChar);
            }
        }
        return sb.toString();

    }

    /**
     * 截取指定长度字符串，不足则补充对应的字符
     * @param msg 原始字符串
     * @param length 截取长度
     * @param fill 补充字符
     * @return
     */
    public static String subStringAppend(String msg,int length,String fill){
        int msgLength = msg.length();
        if(msgLength > length){
            return msg.substring(0,length);
        }

        return append(msg, length, msgLength,fill);
    }

    private static String append(String str, int start,int end, String fill) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            sb.append(fill);
        }
        sb.append(str);
        return sb.toString();
    }


}
