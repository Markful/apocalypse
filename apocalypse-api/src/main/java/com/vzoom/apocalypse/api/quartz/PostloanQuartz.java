package com.vzoom.apocalypse.api.quartz;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/26
 */
@Component
@Slf4j
public class PostloanQuartz {

//    @Scheduled(cron = "${quartz.feedback.upload.cron}")
    @SchedulerLock(name = "read_feedback_file", lockAtLeastFor = "10m", lockAtMostFor = "20m")
    public void readFeedbackFile(){



    }


    @SchedulerLock(name = "push_feedback_file", lockAtLeastFor = "10m", lockAtMostFor = "20m")
    public void pushFeedbackFile(){



    }

    @SchedulerLock(name = "read_postloan_file", lockAtLeastFor = "10m", lockAtMostFor = "20m")
    public void readPostloanFile(){



    }

    @SchedulerLock(name = "push_postloan_file", lockAtLeastFor = "10m", lockAtMostFor = "20m")
    public void pushPostloanFile(){



    }








}
