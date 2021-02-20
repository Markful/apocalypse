package com.vzoom.apocalypse.common.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/20
 */
public class ConvertUtils {


    public static String replaceDate(String str) {
        str = str.replaceAll("\\{yyyyMMdd}", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .replaceAll("\\{yyyyMM}",LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")))
                .replaceAll("\\{yyyyMMdd-1}",LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .replaceAll("\\{yyyyMM-1}",LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM")));
        return str;
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
     */
    public static String findSeparator(String str){


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
