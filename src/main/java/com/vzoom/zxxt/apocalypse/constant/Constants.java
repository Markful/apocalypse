package com.vzoom.zxxt.apocalypse.constant;

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
