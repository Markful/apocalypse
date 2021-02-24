package com.vzoom.apocalypse.api.dto;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.CloneUtils;

import java.io.Serializable;

/**
 * @desc DTO类
 * @Author zengfj
 * @create 2020/10/24
 **/
@Slf4j
public class DTO implements Serializable,Cloneable {

    @Override
    protected Object clone() {
        Object newObj=null;
        try {
            newObj=CloneUtils.clone(this);
        } catch (CloneNotSupportedException e) {
            log.error("对象克隆异常",e);
            throw new RuntimeException("对象克隆异常",e);
        }
        return newObj;
    }
}
