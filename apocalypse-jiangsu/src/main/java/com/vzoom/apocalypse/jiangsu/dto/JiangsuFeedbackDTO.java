package com.vzoom.apocalypse.jiangsu.dto;

import lombok.Data;


/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/3/2
 */
@Data
public class JiangsuFeedbackDTO {

    String path;
    String filename;
    String isUpload;
    String content;


    @Override
    public String toString() {
        return "{" +
                "\"path\":\"" + path +
                "\",\"filename\":\"" + filename  +
                "\",\"isUpload\":\"" + isUpload +
                "\",\"content\":" + content +
                "}";
    }
}
