package com.vzoom.apocalypse.common.cache;

import com.vzoom.apocalypse.common.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.common.entity.ApocalypseProperty;
import io.swagger.models.auth.In;

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
    public static Map<String,String[]> AREAFIELD_PROPERTIES_CACHE = new HashMap<>();

    /**
     * 反馈字段中税号所在位置
     * {地区:税号所在AREAFIELD_PROPERTIES_CACHE中的下标}
     */
    public static Map<String, Integer> NSRSBH_INDEX = new HashMap<>();

    /**
     * 规则已经配置了的地区存放到缓存中
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


    /**
     * 反馈分隔符，如果有此地区配置，则使用指定的分隔符
     * Map<area,split>
     */
    public static Map<String,String> AREA_SEPARATOR_CACHE = new HashMap<>();

}
