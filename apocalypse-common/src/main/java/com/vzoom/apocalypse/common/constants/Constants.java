package com.vzoom.apocalypse.common.constants;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wangyuhao
 * @Description
 * @Date: 14:27 2019/11/22
 */
public class Constants {

    public static final String SUCCESS_CODE = "00000000";
    public static final String FAIL_CODE = "99999999";
    public static final String TAX_SUCCESS_RTNCODE = "0";
    public static final String TAX_FAIL_RTNCODE = "1";
    public static final String AGREE_Y = "Y";
    public static final String DISAGREE_N = "N";

    public static final String EXCEPTION_MESSAGE = "程序异常";
    public static final String INVALID_MESSAGE = "参数校验失败";
    public static final String SUCCESS_MESSAGE = "执行成功";
    public static final String FAIL_AUTHORIZATION_MESSAGE = "授权失败";


    public static final String REFEATCH_INIT_CODE = "1";
    public static final String REFEATCH_FAIL_CODE = "-1";

    public static final String FEEDBACK_SUCCESS = "0000";
    public static final String FEEDBACK_FAIL = "9999";

    public static final String DEFAULT_PRODUCT_ID = "000";


    public static int ACCURACY = 2; //默认精度
    public static final String SPEL_METHOD_CHOOSE = "choose";
    public static final String SPEL_METHOD_TRIM_S0 = "trim_s0";//去掉首尾空格，不去中间空格
    public static final String SPEL_METHOD_TRIM_S1 = "trim_s1";//去掉所有空格
    public static final String SPEL_METHOD_TRIM_0 = "trim_0";//去掉最前面的0，保留符号
    public static final String SPEL_METHOD_TRIM_1 = "trim_1";//去掉末尾小数点
    public static final String SPEL_METHOD_TRIM_2 = "trim_2";//保留几位小数
    public static final String SPEL_METHOD_TRIM_3 = "trim_3";//转换成万元，保留n位小数，eg:502300 -> 50.23
    public static final String SPEL_METHOD_TRIM_4 = "trim_4";//万元转成元，保留n位小数0
    public static final String SPEL_METHOD_DATE_0 = "date_0";//将当前系统时间转换为指定格式
    public static final String SPEL_METHOD_DATE_1 = "date_1";//将字段的时间转换为指定格式
    public static final String SPEL_METHOD_DATE_2 = "date_2";//格式转换，支持互转(yyyymmdd,yyyy-mm-dd)(yyyy-mm-dd,yyyy-mm-dd hh:mm:ss)(yyyymmdd,yyyy-mm-dd hh:mm:ss)

    public static final String NSRSBH = "nsrsbh";
    public static final String COMMON_AREA = "common";
    public static ConcurrentHashMap<String,String> cacheMap = new ConcurrentHashMap<>();

    /**
     * 网关
     */
    public static final String ROUTE_GATEWAY = "gateway";
    /**
     * 代理
     */
    public static final String ROUTE_PROXY = "proxy";
    public static final String ROUTE_TAX = "tax";


    public static final String API_CHANNEL = "api";
    public static final String MQ_CHANNEL = "mq";
    public static final String NO_MQ = "nomq";


}
