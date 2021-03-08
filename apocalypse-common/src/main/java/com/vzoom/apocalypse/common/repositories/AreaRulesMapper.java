package com.vzoom.apocalypse.common.repositories;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vzoom.apocalypse.common.entity.ApocalypseAreaRules;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/29
 */
@Repository
@Mapper
public interface AreaRulesMapper extends BaseMapper<ApocalypseAreaRules> {
}
