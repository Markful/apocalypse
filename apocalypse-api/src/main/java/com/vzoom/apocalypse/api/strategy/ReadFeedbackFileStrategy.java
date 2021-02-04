package com.vzoom.apocalypse.api.strategy;

import java.util.List;

public interface ReadFeedbackFileStrategy {

    /**
     * 读取反馈数据
     * @param path 读取路径
     * @return 文件中的数据
     * @throws Exception 抛出的异常
     */
    List<String> readFeedbackData(String path) throws Exception;

}
