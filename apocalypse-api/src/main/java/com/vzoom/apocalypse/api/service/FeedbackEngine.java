package com.vzoom.apocalypse.api.service;

import com.vzoom.apocalypse.api.dto.FeedbackContext;

/**
 * @Description: 反馈引擎
 * @Author: wangyh
 * @Date: 2021/2/20
 */
public interface FeedbackEngine {

    void exchange(FeedbackContext source) throws Exception;

}
