package com.vzoom.apocalypse.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @desc DTO基础类
 * @Author zengfj
 * @create 2020/10/24
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseDTO extends DTO {


    /**
     * 请求ID
     */
    protected String requestId;
    /**
     *  请求客户端IP
     */
    protected String clientIp;

    /**
     * 客户授权key
     */
    protected String appKey;

    /**
     * appId
     */
    @NotNull(message = "appId不能为空")
    protected String appId;

}
