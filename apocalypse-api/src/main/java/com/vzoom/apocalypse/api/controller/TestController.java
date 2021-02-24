package com.vzoom.apocalypse.api.controller;

import com.vzoom.apocalypse.api.quartz.PostloanQuartz;
import io.swagger.annotations.Api;
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
    public void test1(@RequestParam(name = "type") String type, @RequestParam(name = "content") String content){

        switch (type) {
            case "1":
                //处理反馈文件
                postloanQuartz.readFeedbackFile();
                break;
            case "2":
                //推送反馈信息
                postloanQuartz.pushFeedbackFile();
                break;

            case "3":
                //处理贷后名单
                postloanQuartz.readPostloanFile();
                break;

            case "4":
                //推送贷后名单
                postloanQuartz.pushPostloanFile();
                break;


            default:

                break;


        }

    }

}
