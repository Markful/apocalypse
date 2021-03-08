package com.vzoom.apocalypse.common.enums;

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

    FEEDBACK_PUSH_FLAG_0("0","每次推送1条反馈数据"),
    FEEDBACK_PUSH_FLAG_1("1","每次推送所有反馈数据"),

    FEEDBACK_FAIL("9999","反馈失败"),
    NEVER_FEEDBACK("9999","该纳税人未做过反馈"),

    FEEDBACK_STRATEGY_SFTP("sftp",""),
    FEEDBACK_STRATEGY_SFTP_ZIP("sftp_zip",""),
    FEEDBACK_STRATEGY_FTP("ftp",""),
    FEEDBACK_STRATEGY_FTP_ZIP("ftp_zip",""),
    FEEDBACK_STRATEGY_TXT("txt","com.vzoom.apocalypse.api.strategy.child.ReadTxtFileServiceImpl"),
    FEEDBACK_STRATEGY_TXT_ZIP("txt_zip",""),

    FEEDBACK_STATUS_0000("0000","反馈成功"),
    FEEDBACK_STATUS_9999("9999","反馈失败"),
    FEEDBACK_STATUS_1111("1111","初始入库"),
    FEEDBACK_STATUS_2222("2222","正在反馈"),
    FEEDBACK_STATUS_3333("3333","网络异常"),
    FEEDBACK_STATUS_4444("4444","参数错误"),

    POSTLOAN_PUSH_STATUS_1111("1111","等待推数(包括失败重推)"),
    POSTLOAN_PUSH_STATUS_0000("0000","推送datagrid成功"),
    POSTLOAN_PUSH_STATUS_9999("9999","推送datagrid失败"),

    FEEDBACK_SOURCE_SCHEDULE("schedule","数据来源为反馈定时任务"),
    FEEDBACK_SOURCE_REFETCH("refetch","数据来源为失败后定时重取"),

    DATAGRID_BIZCODE_0("0","贷前"),
    DATAGRID_BIZCODE_1("1","贷后")



    ;


    private String code;
    private String message;

    private CommonEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(String code) {
        for (CommonEnum c : values()) {
            if (c.getCode().equals(code)) {
                return c.message;
            }
        }
        return null;
    }

    public static String getCodeByMessage(String message) {
        for (CommonEnum c : values()) {
            if (c.getMessage().equals(message)) {
                return c.code;
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
