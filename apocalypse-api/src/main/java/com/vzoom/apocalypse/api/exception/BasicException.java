package com.vzoom.apocalypse.api.exception;


import com.vzoom.apocalypse.api.entity.ResponseInfo;

/**
 * 一般异常
 */
public class BasicException extends RuntimeException{

    /**
     * 错误代码
     */
    private String code;
    /**
     * 错误消息
     */
    private String message;

    public BasicException() {
        super();
    }

    public BasicException(String code, String message){
        super(message);
        this.code=code;
        this.message=message;
    }

    public BasicException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code=code;
        this.message=message;

    }

    public BasicException(String message) {
        super(message);
        this.message=message;
    }

    public BasicException(Throwable cause) {
        super(cause);
    }

    public BasicException(String message, Throwable cause) {
        super(message, cause);
    }

    public BasicException(ResponseInfo responseInfo) {
        super(responseInfo.getMsg());
        this.code = responseInfo.getCode();
        this.message = responseInfo.getMsg();
    }

    public BasicException(ResponseInfo responseInfo, Throwable e) {
        super(responseInfo.getMsg(), e);
        this.code = responseInfo.getCode();
        this.message = responseInfo.getMsg();
    }

    public BasicException(ResponseInfo responseInfo, String msg) {
        super(msg == null ? responseInfo.getMsg() : responseInfo.getMsg() + ":" + msg);
        this.code = responseInfo.getCode();
        this.message = msg == null ? responseInfo.getMsg() : responseInfo.getMsg() + ":" + msg;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
