package com.vzoom.apocalypse.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vzoom.apocalypse.api.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.api.repository.AreaRulesMapper;
import com.vzoom.apocalypse.api.service.CheckDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.vzoom.apocalypse.common.cache.CommonCache.areafieldPropertiesCache;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/29
 */
@Service
@Slf4j
public class CheckDataServiceImpl implements CheckDataService {

    @Autowired
    private AreaRulesMapper areaRulesMapper;


    /**
     * 检查配置的数据完整性
     * 根据配置的地区字段，与数据库表的字段数一致，则校验通过
     * feedback.param.地区 中的字段数 与数据库中的一致，则校验通过/或者数据库没有此地区，则日志打印此地区未授权反馈
     * @return
     */
    @Override
    public boolean checkFeedbackPropertyIntegrity() {
        log.info("规则表检查：apocalypse_rules");
        QueryWrapper<ApocalypseAreaRules> wrapper = new QueryWrapper<>();

        //参数数量检查
        for (String area : areafieldPropertiesCache.keySet()) {
            //获取当前地区所有字段的规则
            log.info("当前配置校验地区：{}",area);
            wrapper.eq("area",area);
            List<ApocalypseAreaRules> areaRulesList = areaRulesMapper.selectList(wrapper);
            if(CollectionUtils.isEmpty(areaRulesList)){
                log.error("当前配置地区 {} 规则未配置，请到数据库中进行规则配置",area);
                return false;
            }

            //获取地区配置的分隔符
            String areaPropreties = areafieldPropertiesCache.get(area);
            String separator = areaPropreties.split("[A-Za-z0-9]+")[0];
            //获取每个参数
            String[] split = areaPropreties.split(separator);
            for (String column : split) {
                if(StringUtils.isNotEmpty(column)){
                    if(areaRulesList.stream().noneMatch(x -> x.getFeedback_field().equals(column))){
                        //如果对应字段不存在，则说明表中没有配置对应字段的规则
                        log.error("表中未配置 {} 地区，{} 字段的规则，请重新配置",area,column);
                        return false;
                    }
                }
            }
        }

        //地区检查



        return true;
    }


}
