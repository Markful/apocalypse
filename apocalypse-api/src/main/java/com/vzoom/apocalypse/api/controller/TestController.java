package com.vzoom.apocalypse.api.controller;

import com.vzoom.apocalypse.api.quartz.PostloanQuartz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2020/7/6
 */
@RestController
@Api(tags = "测试接口")
@Slf4j
@RequestMapping("/api")
public class TestController {

    @Autowired
    private PostloanQuartz postloanQuartz;

    @GetMapping("/test")
    @ApiOperation(value = "type:1-读取反馈，2-推送反馈，3-读取贷后，4-推送贷后。", httpMethod = "GET")
    public void test1(@RequestParam(name = "type") String type){

        switch (type) {
            case "1":
                //处理反馈文件
                postloanQuartz.readFeedbackFile();
                break;
            case "2":
                //推送反馈信息给datagrid
                postloanQuartz.pushFeedbackFile();
                break;

            case "3":
                //处理贷后名单，并推送给datagrid
                postloanQuartz.readPostloanFile();
                break;

            case "4":
                //重推贷后名单
                postloanQuartz.rePushRecord();
                break;


            default:

                break;


        }

    }

}
