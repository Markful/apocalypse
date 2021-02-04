package com.vzoom.apocalypse.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vzoom.apocalypse.api.config.InitContext;
import com.vzoom.apocalypse.api.dto.FeedbackContext;
import com.vzoom.apocalypse.api.entity.ApocalypseProperty;
import com.vzoom.apocalypse.api.repository.PropertyMapper;
import com.vzoom.apocalypse.api.service.FeedbackService;
import com.vzoom.apocalypse.api.service.HandlerService;
import com.vzoom.apocalypse.api.service.rules.CommonRulesHandler;
import com.vzoom.apocalypse.common.config.Builder;
import com.vzoom.apocalypse.common.enums.CommonEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.vzoom.apocalypse.api.config.CacheConfig.PROPERTY_CACHE_LIST;

@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private CommonRulesHandler commonRulesHandler;

    /**
     * 读取反馈文件，入库记录
     *
     * @param area
     */
    @Override
    public void readFeedbackFile(String area) {

        //TODO 根据配置的策略 获取文件内容
        ApocalypseProperty property = PROPERTY_CACHE_LIST.stream().findFirst().filter(x -> x.getArea().equals(area)).get();

        List<FeedbackContext> feedbackContext = readFeedbackFileByStrategy(property);



        //获取当前地区下的配置记录
        QueryWrapper<ApocalypseProperty> wrapper = new QueryWrapper<>();
        wrapper.eq("area",area);
        List<ApocalypseProperty> apocalypseProperties = propertyMapper.selectList(wrapper);

        //根据当前的配置记录，使用对应的策略读取文件
        for (ApocalypseProperty apocalypseProperty : apocalypseProperties) {
            FeedbackContext feedbackContext = Builder.of(FeedbackContext::new)
                    .with(FeedbackContext::setArea, area)
                    .with(FeedbackContext::setFeedback_file_path, apocalypseProperty.getFeedback_file_path())
                    .with(FeedbackContext::setFeedback_frequency, apocalypseProperty.getFeedback_frequency())
                    .with(FeedbackContext::setFeedback_strategy, apocalypseProperty.getFeedback_strategy())
                    .with(FeedbackContext::setProduct_id, apocalypseProperty.getProduct_id())
                    .build();

            commonRulesHandler.HandleRules(feedbackContext);
        }



    }

    /**
     * 根据不同的策略获取文件内容
     * @param property
     * @return
     */
    private FeedbackContext readFeedbackFileByStrategy(ApocalypseProperty property) {

        InitContext.getBean(CommonEnum.getMessageByCode(property.getFeedback_strategy()));



        return null;
    }


}
