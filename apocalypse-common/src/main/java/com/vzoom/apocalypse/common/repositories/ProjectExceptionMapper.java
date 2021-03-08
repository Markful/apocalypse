package com.vzoom.apocalypse.common.repositories;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vzoom.apocalypse.common.entity.AnomalyLog;
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
