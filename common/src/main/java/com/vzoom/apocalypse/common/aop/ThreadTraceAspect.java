package com.vzoom.apocalypse.common.aop;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 线程追踪AOP
 * @Author vzoom
 * @Date 2020/12/22 14:19
 * @Version 1.0
 */
@Component
@Aspect
public class ThreadTraceAspect {

    @Around("@annotation(com.vzoom.apocalypse.common.annotation.ThreadTrace)")
    public void threadTraceAspect(ProceedingJoinPoint joinPoint) throws Throwable {
       /* Object[] args = joinPoint.getArgs();
        if(args==null||args.length==0){
             joinPoint.proceed();
        }else{
            JSONObject jsonObject =JSONObject.parseObject(JSONObject.toJSONString(args[0]));
            ThreadMdcUtil.setTraceIdIfAbsent(jsonObject.getString("requestId"));
            joinPoint.proceed();
            ThreadMdcUtil.removeTraceId();
        }*/

    }
}
