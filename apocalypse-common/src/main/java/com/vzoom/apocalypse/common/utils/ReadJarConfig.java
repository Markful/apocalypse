package com.vzoom.apocalypse.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/3/2
 */
@Slf4j
public class ReadJarConfig {

    public static Properties properties;

    static {
        try {
            /*PropertyConfigurator.configure(System.getProperty("user.dir")
                    + "/application.properties");*/
            properties = new Properties();
            // 读取SRC下配置文件 --- 属于读取内部文件
            properties.load(ReadJarConfig.class.getResourceAsStream("/application-area.properties"));

        }catch (Exception e){
            log.error("读取Jar包配置文件出错");
            e.printStackTrace();
        }
    }

    public static String readJarConfig(String key){
        return properties.getProperty(key);
    }


}
