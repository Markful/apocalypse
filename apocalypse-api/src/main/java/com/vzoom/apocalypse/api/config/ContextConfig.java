package com.vzoom.apocalypse.api.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vzoom.apocalypse.api.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.api.repository.AreaRulesMapper;
import com.vzoom.apocalypse.api.service.CheckDataService;
import com.vzoom.apocalypse.common.cache.CommonCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/20
 */
@Component
public class ContextConfig implements ApplicationRunner {

    @Autowired
    private FeedbackProperties feedbackProperties;


    @Autowired
    private CheckDataService checkDataService;

    @Autowired
    private AreaRulesMapper areaRulesMapper;

    @Autowired
    private Environment environment;

    /**
     * 启动前缓存加载，表校验，数据校验等
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        /*将所有的地区配置读取出来,存入Map：CommonCache.areafieldPropertiesCache*/
        loadAreaField();

        /*TODO 校验配置文件规则是否正确*/
        if(checkDataService.checkFeedbackPropertyIntegrity()){


        }


        /*TODO 读取 数据库 算法配置，加载到缓存*/
        //feedback.param.hubei



        /**/

    }


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
                CommonCache.areafieldPropertiesCache.put(area,property);
            }
        }

    }


}
