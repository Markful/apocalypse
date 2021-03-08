package com.vzoom.apocalypse.common.repositories;

import com.vzoom.apocalypse.common.entity.ApocalypseFeedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BankFeedbackMapper {

    /**
     * 查询纳税人基础信息
     *
     * @param nsrsbh 参数纳税人识别号
     * @return 返回数量
     */
    Integer selectNsrjcxx(@Param("nsrsbh") String nsrsbh);

    /**
     * 执行自定义SQL查询 ApocalypseFeedback 表中的满足条件的数据
     *
     * @param sqlStr sql语句
     * @return 查询结果
     */
    List<ApocalypseFeedback> selectFeedbackList(@Param(value = "sqlStr") String sqlStr);




    /**
     * 查询贷后名单表中最近的批次号
     * @return 批次号
     */
    String queryRecentlyBatchNumber();

}
