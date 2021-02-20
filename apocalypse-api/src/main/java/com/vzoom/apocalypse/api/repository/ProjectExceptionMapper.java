package com.vzoom.apocalypse.api.repository;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vzoom.apocalypse.api.entity.AnomalyLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/2/19
 */
@Repository
@Mapper
public interface ProjectExceptionMapper extends BaseMapper<AnomalyLog> {
}
