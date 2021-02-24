package com.vzoom.apocalypse.api.dto;

/**
 * @author yangzhilin
 * @date 2020/10/19
 */
public class InvokeResponse extends ResponseModel {

    private static final long serialVersionUID = -6963709471904601668L;

    public InvokeResponse(ResponseInfo responseInfo) {
        super(responseInfo);
    }

    public InvokeResponse(String responseCode, String responseMsg) {
        super(responseCode, responseMsg);
    }

    public InvokeResponse() {
    }
}
