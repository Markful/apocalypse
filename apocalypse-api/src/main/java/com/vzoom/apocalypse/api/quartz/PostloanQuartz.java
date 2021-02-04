package com.vzoom.apocalypse.api.quartz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vzoom.apocalypse.api.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.api.entity.ApocalypseProperty;
import com.vzoom.apocalypse.api.repository.PropertyMapper;
import com.vzoom.apocalypse.api.service.ExceptionService;
import com.vzoom.apocalypse.api.service.FeedbackService;
import com.vzoom.apocalypse.common.cache.CommonCache;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/26
 */
@Component
@Slf4j
public class PostloanQuartz {

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ExceptionService exceptionService;

//    @Scheduled(cron = "${quartz.feedback.upload.cron}")
    @SchedulerLock(name = "read_feedback_file", lockAtLeastFor = "10m", lockAtMostFor = "20m")
    public void readFeedbackFile(){

        for (String area : CommonCache.areaList) {
            log.info("当前读取反馈文件的地区：{}",area);
            try {
                feedbackService.readFeedbackFile(area);
            }catch (Exception e){
                exceptionService.toException(e,"地区：" + area);
            }

        }



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
