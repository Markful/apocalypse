package com.vzoom.apocalypse.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vzoom.apocalypse.api.service.CheckDataService;
import com.vzoom.apocalypse.common.cache.CommonCache;
import com.vzoom.apocalypse.common.constants.Constants;
import com.vzoom.apocalypse.common.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.common.entity.ApocalypseProperty;
import com.vzoom.apocalypse.common.repositories.AreaRulesMapper;
import com.vzoom.apocalypse.common.repositories.PropertyMapper;
import com.vzoom.apocalypse.common.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.vzoom.apocalypse.common.cache.CommonCache.*;

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

    /**
     * 1、获取所有已经配置了数据的地区，放入缓存
     * 2、获取apocalypse_property 表中所有地区的 字段的顺序的配置，存入缓存
     * key:area
     * value:nsrsbh|nsrmc|xxx|....
     */
    @Override
    public void loadAreaField(){

        QueryWrapper<ApocalypseProperty> wrapper = new QueryWrapper<>();
        List<ApocalypseProperty> apocalypseProperties = propertyMapper.selectList(wrapper);

        //去重得到所有配置的地区,只过滤得到不重复的area字段
        List<ApocalypseProperty> collect = apocalypseProperties.stream().filter(x -> StringUtils.isNotEmpty(x.getArea()) && Constants.AGREE_Y.equals(x.getUsedFlag()))
                .collect(Collectors.toList());

        //读取所有配置文件的地区，存入缓存
        for (ApocalypseProperty areaProperty : collect) {
            String area = areaProperty.getArea();
            String property = areaProperty.getFeedbackParam();
            String bankTaxParam = areaProperty.getBankTaxParam();
            if(StringUtils.isNotEmpty(bankTaxParam)){
                Map map = ConvertUtils.fromJsonToMap(bankTaxParam);
                CommonCache.BANK_TAX_PARAM_MAP.put(area,map);
            }

            if(StringUtils.isNotEmpty(area) && null != property){
                log.info("添加 {} 地区反馈配置 信息:{}",area,areaProperty);
                CommonCache.AREA_LIST.add(area);

                //保存地区个性化分隔符
                if(!StringUtils.isEmpty(areaProperty.getAreaSeparator())){
                    AREA_SEPARATOR_CACHE.put(area,areaProperty.getAreaSeparator());
                }

                String separatorRegex2 = ConvertUtils.findSeparator(property,area);
                String[] feedbackCol = property.split(separatorRegex2);
                CommonCache.AREAFIELD_PROPERTIES_CACHE.put(area,feedbackCol);

                //解析每个地区的nsrsbh所在位置下标
                for (int i = 0; i < feedbackCol.length; i++) {
                    if(Constants.NSRSBH.equalsIgnoreCase(feedbackCol[i])){
                        NSRSBH_INDEX.put(area,i);
                    }
                }


                PROPERTY_CACHE_MAP.put(area,areaProperty);

            }


        }

    }

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
        for (String area : AREAFIELD_PROPERTIES_CACHE.keySet()) {
            //获取当前地区所有字段的规则
            log.info("当前配置校验地区：{}",area);
            wrapper.eq("area",area);
            List<ApocalypseAreaRules> areaRulesList = areaRulesMapper.selectList(wrapper);
            if(CollectionUtils.isEmpty(areaRulesList)){
                log.error("当前配置地区 {} 规则未配置，请到数据库中进行规则配置",area);
                return false;
            }
            RULES_MAP.put(area,areaRulesList);
            log.info("{} 规则校验通过",area);

        }

        log.info("规则表检查：apocalypse_property");
        for (ApocalypseProperty property : PROPERTY_CACHE_MAP.values()) {
            if (!checkPropertyParam(property)) {
                log.error("配置参数缺失或格式错误，地区：{},配置参数：{}", property.getArea(), property);
                break;
            }
        }

        return true;
    }

    /**
     * 检查 apocalypse_property 字段是否满足规则要求
     * @param property
     * @return
     */
    private boolean checkPropertyParam(ApocalypseProperty property) {

        Assert.notNull(property.getMinistryCode(),"ministry_code 不允许为空");
        Assert.notNull(property.getFeedbackStrategy(),"feedback_strategy 不允许为空");
        Assert.notNull(property.getFeedbackParam(),"feedback_param 不允许为空");
        Assert.notNull(property.getTransportType(),"transport_type 不允许为空");
        Assert.isTrue(property.getFeedbackParam().contains(Constants.NSRSBH),"feedback_param 必须要包含字段nsrsbh");

        return true;
    }


}
