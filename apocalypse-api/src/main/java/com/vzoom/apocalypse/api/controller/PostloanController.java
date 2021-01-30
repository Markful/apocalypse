package com.vzoom.apocalypse.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/28
 */
@Controller
public class PostloanController {

    @GetMapping(value = "/test")
    public String index(){
        System.out.println("访问到了");
        return "index";
    }


}
