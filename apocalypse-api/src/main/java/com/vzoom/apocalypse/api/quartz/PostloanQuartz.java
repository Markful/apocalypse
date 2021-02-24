package com.vzoom.apocalypse.api.quartz;

import com.vzoom.apocalypse.api.repository.PropertyMapper;
import com.vzoom.apocalypse.api.service.ExceptionService;
import com.vzoom.apocalypse.api.service.FeedbackService;
import com.vzoom.apocalypse.common.cache.CommonCache;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    /**
     * 读取反馈文件定时任务
     */
//    @Scheduled(cron = "${quartz.feedback.upload.cron}")
//    @SchedulerLock(name = "read_feedback_file", lockAtLeastFor = "10m", lockAtMostFor = "20m")
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

    /**
     * 推送反馈文件定时任务
     */
    @SchedulerLock(name = "push_feedback_file", lockAtLeastFor = "10m", lockAtMostFor = "20m")
    public void pushFeedbackFile(){

        for (String area : CommonCache.areaList) {
            //TODO 判断当前地区是否推送反馈

            try {
                feedbackService.pushFeedbackInfo(area);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 读取贷后名单定时任务
     */
    @SchedulerLock(name = "read_postloan_file", lockAtLeastFor = "10m", lockAtMostFor = "20m")
    public void readPostloanFile(){



    }


    /**
     * 推送贷后名单
     */
    @SchedulerLock(name = "push_postloan_file", lockAtLeastFor = "10m", lockAtMostFor = "20m")
    public void pushPostloanFile(){



    }








}
