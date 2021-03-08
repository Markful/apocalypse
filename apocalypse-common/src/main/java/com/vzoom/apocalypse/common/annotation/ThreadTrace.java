package com.vzoom.apocalypse.common.annotation;

import java.lang.annotation.*;

/**
 * 线程追踪
 * @Author vzoom
 * @Date 2020/12/22 14:23
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ThreadTrace {

}
