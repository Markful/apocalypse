package com.vzoom.apocalypse.common.cache;

import com.vzoom.apocalypse.common.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.common.entity.ApocalypseProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/29
 */

public class CommonCache {

    /**
     * 配置文件 配置
     */
    public static Map<String,ApocalypseProperty> PROPERTY_CACHE_MAP = new HashMap<>(32);

    /**
     * {地区：反馈文件字段(xxx|xxx|xxx)} 配置
     */
    public static HashMap<String,String> AREAFIELD_PROPERTIES_CACHE = new HashMap<>();

    /**
     * 规则已经配置了的地区存放到缓存中，来源于配置：feedback.param.xxxx
     */
    public static List<String> AREA_LIST = new ArrayList<>(32);

    /**
     * apocalypse_property表中税局分配给银行的固定参数：bank_tax_param
     * 结构：(area,Map(key,value))，key用小写
     */
    public static Map<String,Map<String,String>> BANK_TAX_PARAM_MAP = new HashMap<>();

    /**
     * apocalypse_rules 表缓存数据
     */
    public static Map<String,List<ApocalypseAreaRules>> RULES_MAP = new HashMap<>();

}
