package com.vzoom.apocalypse.common.cache;

import com.vzoom.apocalypse.common.entity.ApocalypseProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/29
 */

public class CommonCache {

    /*配置文件 配置*/
    public static List<ApocalypseProperty> PROPERTY_CACHE_LIST = new ArrayList<>(32);

    /*{地区：反馈文件字段(xxx|xxx|xxx)} 配置*/
    public static HashMap<String,String> areafieldPropertiesCache = new HashMap<>();

    /*规则已经配置了的地区存放到缓存中*/
    public static List<String> areaList = new ArrayList<>(32);




}
