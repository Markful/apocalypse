package com.vzoom.zxxt.apocalypse.config;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2020/3/4
 */
public interface Consumer1<T,P1>{
    void accept(T t, P1 p1);
}
