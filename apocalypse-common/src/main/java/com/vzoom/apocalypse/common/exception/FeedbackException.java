package com.vzoom.apocalypse.common.exception;


import com.vzoom.apocalypse.common.entity.ResponseInfo;
import com.vzoom.apocalypse.common.exception.BasicException;

/**
 * @author yangzhilin
 * @date 2020-11-07
 */
public class FeedbackException extends BasicException {

    public FeedbackException() {
    }

    public FeedbackException(ResponseInfo responseInfo) {
        super(responseInfo);
    }

    public FeedbackException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public FeedbackException(String message) {
        super(message);
    }

    public FeedbackException(Throwable cause) {
        super(cause);
    }

    public FeedbackException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeedbackException(String code, String message) {
        super(code, message);
    }
}
