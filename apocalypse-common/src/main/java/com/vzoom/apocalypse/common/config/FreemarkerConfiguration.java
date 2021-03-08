package com.vzoom.apocalypse.common.config;

import freemarker.cache.StringTemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangzhilin
 * @date 2020/10/24
 */
@Configuration
public class FreemarkerConfiguration {

    @Bean
    public freemarker.template.Configuration freemarkerCfg() {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_30);
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(stringTemplateLoader);
        configuration.setDefaultEncoding("UTF-8");
        return configuration;
    }
}
