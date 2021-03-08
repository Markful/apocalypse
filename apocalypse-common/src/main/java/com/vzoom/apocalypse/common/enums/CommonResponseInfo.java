package com.vzoom.apocalypse.common.enums;

import com.vzoom.apocalypse.common.entity.ResponseInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * 返回枚举
 * @author wangyh
 */
public enum CommonResponseInfo implements ResponseInfo {

    /*000000-返回成功*/
    SUCCESS("000000","成功"),
    /*000001-失败*/
    FAILED("000001","失败"),
    /*000002-请求异常*/
    REQUEST_EXCEPTION("000002","请求异常"),
    /*000003-服务异常*/
    SERVICE_EXCEPTION("000003","服务异常"),
    /*000004-参数错误*/
    PARAM_ERROR("000004","参数错误"),
    /*000005-参数错误*/
    SYSTEM_ERROR("000005","系统内部错误"),

    REQUEST_TIMEOUT("000006", "请求超时"),

    READ_SFTP_EXCEPTION("000007", "SFTP读取错误")

    ;



    private String code;
    private String msg;

    CommonResponseInfo(String code, String msg){
        this.code=code;
        this.msg=msg;
    }
    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public String toString(){
        return "{code="+code+", msg="+msg+"}";
    }

    /**
     * 获取返回消息
     * @param code
     * @return
     */
    public static String getMsg(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        for(CommonResponseInfo obj: CommonResponseInfo.values()){
            if(obj.getCode().equals(code)){
                return obj.getMsg();
            }
        }
        return null;
    }
}
