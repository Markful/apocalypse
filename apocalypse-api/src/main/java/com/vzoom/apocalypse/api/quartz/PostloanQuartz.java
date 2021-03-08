package com.vzoom.apocalypse.api.quartz;

import com.vzoom.apocalypse.common.repositories.PropertyMapper;
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
        log.info("begin readFeedbackFile");
        for (String area : CommonCache.AREA_LIST) {
            try {
                feedbackService.readFeedbackFile(area);
            }catch (Exception e){
                exceptionService.toException(e,"地区：" + area);
            }

        }


        log.info("end readFeedbackFile");
    }

    /**
     * 推送反馈文件定时任务
     */
//    @SchedulerLock(name = "push_feedback_file", lockAtLeastFor = "10m", lockAtMostFor = "20m")
    public void pushFeedbackFile(){
        log.info("begin pushFeedbackFile");
        for (String area : CommonCache.AREA_LIST) {
            try {
                log.info("当前推送反馈数据区域：{}",area);
                feedbackService.pushFeedbackInfo(area);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("end pushFeedbackFile");
    }

    /**
     * 读取贷后名单定时任务
     */
//    @SchedulerLock(name = "read_postloan_file", lockAtLeastFor = "10m", lockAtMostFor = "20m")
    public void readPostloanFile(){
        log.info("begin readPostloanFile");
        for (String area : CommonCache.AREA_LIST) {
            try {
                log.info("当前统计贷后名单的区域为：{}",area);

                feedbackService.readAndPushPostloanInfo(area);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        log.info("end readPostloanFile");
    }


    /**
     * 重推反馈/贷后数据
     */
    @SchedulerLock(name = "repush_record", lockAtLeastFor = "10m", lockAtMostFor = "20m")
    public void rePushRecord(){
        log.info("begin repush record");

        for (String area : CommonCache.AREA_LIST) {
            try {
                log.info("当前重取的区域为：{}",area);

                feedbackService.rePushPostloanInfo(area);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        log.info("end repush record");
    }








}
