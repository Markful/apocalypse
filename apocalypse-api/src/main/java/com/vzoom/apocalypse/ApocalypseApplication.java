package com.vzoom.apocalypse;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication(scanBasePackages = {"com.vzoom.apocalypse"})
@EnableAsync
@EnableScheduling
@EnableSwagger2
@EnableSchedulerLock(defaultLockAtMostFor = "20m")
public class ApocalypseApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApocalypseApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApocalypseApplication.class, args);
    }

}
