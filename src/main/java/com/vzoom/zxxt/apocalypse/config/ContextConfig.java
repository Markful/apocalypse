package com.vzoom.zxxt.apocalypse.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/20
 */
@Component
public class ContextConfig implements ApplicationRunner {

    /**
     * 启动前缓存加载，表校验，数据校验等
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        /*读取 数据库 算法配置，加载到缓存*/
        //feedback.param.hubei



        /**/

    }




}
