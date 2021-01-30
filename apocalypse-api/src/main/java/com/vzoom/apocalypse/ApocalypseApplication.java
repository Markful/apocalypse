package com.vzoom.apocalypse;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableSwagger2
@EnableSchedulerLock(defaultLockAtMostFor = "20m")
public class ApocalypseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApocalypseApplication.class, args);
    }

}
