package com.vzoom.apocalypse.api.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vzoom.apocalypse.common.entity.ApocalypseProperty;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;

@Repository
@Mapper
public interface PropertyMapper extends BaseMapper<ApocalypseProperty> {
}
