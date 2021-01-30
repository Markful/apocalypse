package com.vzoom.apocalypse.api.service.rules;

import com.vzoom.apocalypse.api.dto.FeedbackContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/26
 */
@Service
@Slf4j
public class CommonRulesHandler extends AbstractRules {

    /**
     * 通用处理逻辑
     * 如果无法满足要求，则需要重新继承 AbstractRules 方法
     * @param feedbackContext
     */
    @Override
    public void HandleRules(FeedbackContext feedbackContext) {






    }

}
