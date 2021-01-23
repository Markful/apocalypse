package com.vzoom.zxxt.apocalypse.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vzoom.zxxt.apocalypse.entity.ApocalypseFeedback;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface FeedbackMapper extends BaseMapper<ApocalypseFeedback> {
}