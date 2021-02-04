package com.vzoom.apocalypse.common.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/29
 */

public class CommonCache {

    /*{地区：反馈文件字段(xxx|xxx|xxx)} 配置*/
    public static HashMap<String,String> areafieldPropertiesCache = new HashMap<>();

    /*规则已经配置了的地区存放到缓存中*/
    public static List<String> areaList = new ArrayList<>(32);




}
