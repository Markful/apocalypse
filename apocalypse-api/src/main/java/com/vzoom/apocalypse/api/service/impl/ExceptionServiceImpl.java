package com.vzoom.apocalypse.api.service.impl;

import com.vzoom.apocalypse.api.service.ExceptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 异常处理
 */

@Service
@Slf4j
public class ExceptionServiceImpl implements ExceptionService {


    /**
     * 异常日志入库 及 打印
     * @param e
     * @param args
     */
    @Override
    public void toException(Exception e, String... args) {




    }

}
