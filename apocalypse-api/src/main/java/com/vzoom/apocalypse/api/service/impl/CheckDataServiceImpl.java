package com.vzoom.apocalypse.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vzoom.apocalypse.api.repository.AreaRulesMapper;
import com.vzoom.apocalypse.api.repository.PropertyMapper;
import com.vzoom.apocalypse.api.service.CheckDataService;
import com.vzoom.apocalypse.common.cache.CommonCache;
import com.vzoom.apocalypse.common.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.common.entity.ApocalypseProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.vzoom.apocalypse.api.config.CacheConfig.RULES_MAP;
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

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private Environment environment;

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
            RULES_MAP.put(area,areaRulesList);
            /* 数据库中 只需要配置 需要进行EL表达式替换的参数
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
            }*/
        }

        return true;
    }

    /**
     * 获取相关配置参数
     */
    @Override
    public void loadAreaFieldProperties(){
        QueryWrapper<ApocalypseProperty> wrapper = new QueryWrapper<>();
        CommonCache.PROPERTY_CACHE_LIST = propertyMapper.selectList(wrapper);
    }

    /**
     * 获取配置文件中所有地区的 字段的顺序的配置，存入缓存
     * key:area
     * value:nsrsbh|nsrmc|xxx|....
     */
    @Override
    public void loadAreaField(){

        QueryWrapper<ApocalypseAreaRules> wrapper = new QueryWrapper<>();
        List<ApocalypseAreaRules> apocalypseAreaRules = areaRulesMapper.selectList(wrapper);

        //去重得到所有配置的地区,只过滤得到不重复的area字段
        List<ApocalypseAreaRules> collect = apocalypseAreaRules.stream().filter(x -> StringUtils.isNotEmpty(x.getArea())).collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ApocalypseAreaRules::getArea))), ArrayList::new)
        );

        //读取所有配置文件的地区，存入缓存
        for (ApocalypseAreaRules areaRules : collect) {
            String area = areaRules.getArea();
            String property = environment.getProperty("feedback.param." + area);
            if(StringUtils.isNotEmpty(area) && null != property){
                CommonCache.areaList.add(area);
                CommonCache.areafieldPropertiesCache.put(area,property);
            }
        }

    }

}
