package com.vzoom.apocalypse.api.config;

import com.vzoom.apocalypse.api.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.api.entity.ApocalypseProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheConfig {

    /*配置文件 配置*/
    public static List<ApocalypseProperty> PROPERTY_CACHE_LIST = new ArrayList<>(32);

    /* 数据库字段处理规则配置 area:规则列表 */
    public static Map<String,List<ApocalypseAreaRules>> RULES_MAP = new HashMap<>();

}
