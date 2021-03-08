package com.vzoom.apocalypse.api.config;

import com.vzoom.apocalypse.api.service.CheckDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/20
 */
@Component
@Slf4j
public class ContextConfig implements ApplicationRunner {

    @Autowired
    private CheckDataService checkDataService;

    @Value("${ignore.check.rules.switch:false}")
    private boolean ignoreCheckRules;

    /**
     * 启动前缓存加载，表校验，数据校验等
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        /*将所有的地区配置读取出来,放入缓存*/
        log.info("开始读取配置文件");
        checkDataService.loadAreaField();


        /*校验配置文件规则是否正确*/
        if(!checkDataService.checkFeedbackPropertyIntegrity() && !ignoreCheckRules){
            throw new Exception("数据库规则校验不通过，启动失败");
        }

    }



}
