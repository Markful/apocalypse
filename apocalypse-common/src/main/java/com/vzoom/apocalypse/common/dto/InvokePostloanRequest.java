package com.vzoom.apocalypse.common.dto;

import lombok.Data;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/3/4
 */
@Data
public class InvokePostloanRequest<T> {

    /**
     * 分权id(测试10000001)
     */
    private String appId;

    /**
     * appId对应的key(测试test123456)
     */
    private String appKey;

    /**
     * 数据源编码
     */
    private String ministryCode;

    /**
     * 数据源类型编码
     */
    private String dsType;

    /**
     * 税号
     */
    private String nsrsbh;

    /**
     * 贷前贷后标志，贷前0 贷后1
     */
    private String bizCode;

    /**
     * 泛型body，默认只包含一个税号，如果存在其他参数则需要在地区jar包中单独实现
     */
    private T body;
}
