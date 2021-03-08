package com.vzoom.apocalypse.common.dto;

import lombok.Data;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/3/4
 */
@Data
public class PostloanResponse {

    private PostloanResData data;
    private String responseCode;
    private String responseMsg;

}
