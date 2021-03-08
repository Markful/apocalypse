package com.vzoom.apocalypse.jiangsu.dto;

import lombok.Data;


/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/3/2
 */
@Data
public class JiangsuFeedbackDownloadDTO {

    String path;


    @Override
    public String toString() {
        return "{" +
                "\"path\":\"" + path +
                "}";
    }
}
