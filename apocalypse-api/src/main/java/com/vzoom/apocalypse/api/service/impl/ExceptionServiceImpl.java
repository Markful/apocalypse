package com.vzoom.apocalypse.api.service.impl;

import com.vzoom.apocalypse.common.entity.AnomalyLog;
import com.vzoom.apocalypse.common.repositories.ProjectExceptionMapper;
import com.vzoom.apocalypse.api.service.ExceptionService;
import com.vzoom.apocalypse.common.constants.Constants;
import com.vzoom.apocalypse.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 异常处理
 */

@Service
@Slf4j
public class ExceptionServiceImpl implements ExceptionService {

    @Autowired
    private ProjectExceptionMapper projectExceptionMapper;


    /**
     * 异常日志入库 及 打印
     * @param e
     * @param args
     */
    @Override
    public void toException(Exception e, String... args) {

        


    }

    @Override
    public void insertAnomalyLogByException(Exception e, String exceptionMsg) {

        try {
            AnomalyLog anomalyLog = new AnomalyLog();
            //贷后反馈服务异常没有nsrsbh，使用afterloanLog做为标识
            anomalyLog.setNsrsbh("afterloanLog");
            String excepMsg = exceptionMsg +"-----"+ (e==null?null: ExceptionUtils.getStackTrace(e));
            if(excepMsg.length()>=3000){
                excepMsg = excepMsg.substring(0,2900);
            }

            anomalyLog.setExcepMsg(excepMsg);
            anomalyLog.setExcepCode(Constants.FEEDBACK_FAIL);
            anomalyLog.setExcepTime(DateUtil.getCurrentDateString());

            projectExceptionMapper.insert(anomalyLog);

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("添加异常日志错误:{}", e != null ? e.getMessage(): null);
        }

    }

}
