package com.vzoom.apocalypse.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 原始配置字段注入类
 * @Description: 读取配置内容为feedback.param.xxxx，注意驼峰和- 都会字段转换
 * @Author: wangyh
 * @Date: 2021/1/20
 */
@Data
@Component
@PropertySource(value = "classpath:feedback-original.properties")
@ConfigurationProperties(prefix = "feedback.param")
public class FeedbackProperties {

}
