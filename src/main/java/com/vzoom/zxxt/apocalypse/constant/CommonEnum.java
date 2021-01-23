package com.vzoom.zxxt.apocalypse.constant;

/**
 * @Author: wangyuhao
 * @Description
 * @Date: 17:13 2019/11/22
 */
public enum CommonEnum {

    FEEDBACK_SUCCESS("0000","反馈成功"),
    FEEDBACK_PARAM_WRONG("1111","反馈参数错误"),
    FEEDBACK_WAITING_RESPONSE("2222","正在反馈并等待税局返回"),
    FEEDBACK_NET_EXCEPTION("3333","网络异常"),


    FEEDBACK_FAIL("9999","反馈失败"),
    NEVER_FEEDBACK("9999","该纳税人未做过反馈")

    ;


    private String code;
    private String message;

    private CommonEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(String code) {
        for (CommonEnum c : CommonEnum.values()) {
            if (c.getCode().equals(code)) {
                return c.message;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
