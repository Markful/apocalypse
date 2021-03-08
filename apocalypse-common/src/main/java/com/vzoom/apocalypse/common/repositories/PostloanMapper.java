package com.vzoom.apocalypse.common.repositories;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vzoom.apocalypse.common.entity.ApocalypseAfterLoan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/3/4
 */
@Repository
@Mapper
public interface PostloanMapper extends BaseMapper<ApocalypseAfterLoan> {
}
