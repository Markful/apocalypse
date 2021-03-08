package com.vzoom.apocalypse.common.utils;

import com.vzoom.apocalypse.common.exception.BasicException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author yangzhilin
 * @date 2020/10/24
 */
@Component
public class FreeMarkerUtil {

    @Autowired
    @Qualifier("freemarkerCfg")
    private Configuration configuration;


    public String process(String templateName, String templateValue, Map model) {

        return processTemplate(model, templateName, templateValue, configuration);
    }

    private String processTemplate(Map<String, Object> model, String templateName, String templateValue, Configuration configuration) {
        StringWriter stringWriter = new StringWriter();
        try {
            Template template = new Template(templateName, templateValue, configuration);
            template.process(model, stringWriter);
            return  stringWriter.toString();
        } catch (Exception e) {
            throw new BasicException("转换失败");
        }


    }


}
