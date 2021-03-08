package com.vzoom.apocalypse.common.dto;

import com.alibaba.fastjson.JSONObject;
import com.vzoom.apocalypse.common.enums.CommonResponseInfo;

import java.io.Serializable;

/**
 * 基础返回类
 *  @Author zengfj
 * @param <T>
 */
public class ResponseModel<T> implements Serializable {

    /**
     * 返回码
     */
    private String responseCode;

    /**
     * 返回消息
     */
    private String responseMsg;

    /**
     * 返回数据
     */
    private T data;

    public ResponseModel() {
       this(CommonResponseInfo.SUCCESS.getCode(),CommonResponseInfo.SUCCESS.getMsg());
    }

    public ResponseModel(ResponseInfo responseInfo) {
        this.responseCode= responseInfo.getCode();
        this.responseMsg = responseInfo.getMsg();
    }

    public ResponseModel(ResponseInfo responseInfo, T data) {
        this.responseCode= responseInfo.getCode();
        this.responseMsg = responseInfo.getMsg();
        this.data = data;
    }



    public ResponseModel(String responseCode) {
        this.responseCode = responseCode;
    }

    public ResponseModel(String responseCode, String responseMsg) {
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
    }

    public ResponseModel(T data) {
        this.responseCode = CommonResponseInfo.SUCCESS.getCode();
        this.responseMsg = CommonResponseInfo.SUCCESS.getMsg();
        this.data = data;
    }

    public ResponseModel(String responseCode, String responseMsg, T data) {
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
        this.data = data;
    }


    /**
     * 创建成功ResponseModel
     * @return
     */
    public static ResponseModel  buildSuccess(){
        return new ResponseModel();
    }

    /**
     * 创建成功ResponseModel
     * @return
     */
    public static ResponseModel  buildFail(){
        return new ResponseModel(CommonResponseInfo.FAILED);
    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseCode = responseInfo.getCode();
        this.responseMsg = responseInfo.getMsg();
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
