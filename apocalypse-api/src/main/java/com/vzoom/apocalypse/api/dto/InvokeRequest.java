package com.vzoom.apocalypse.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yangzhilin
 * @date 2020/10/19
 */
@Setter
@Getter
@ToString
public class InvokeRequest extends BaseDTO {

    private static final long serialVersionUID = 3091605899684284L;
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 请求uri
     */
    private String requestUrl;
    /**
     * 工单id
     */
    private String ticketId;
    /**
     * 请求头
     */
    private String header;
    /**
     * 请求报文
     */
    private String requestData;
    /**
     * 数据源
     */
    private String ministryCode;
    /**
     * 接口id
     */
    private String serviceId;
    /**
     * 加密字段
     */
    private String encryptedField;
    /**
     * 填充字段
     */
    private String filledField;
    /**
     * 解密字段
     */
    private String decryptedField;
    /**
     * 签名字段
     */
    private String signatureField;
    /**
     * 加密算法
     */
    private String encryptAlgorithm;
    /**
     * 签名算法
     */
    private String signatureAlgorithm;
    /**
     * 请求报文格式
     */
    private String requestType;
    /**
     * 响应报文格式
     */
    private String responseType;

    private String httpMethod;



}
