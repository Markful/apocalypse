package com.vzoom.apocalypse.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/3/4
 */
@TableName("apocalypse_after_loan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApocalypseAfterLoan {

    /**
     * UUID*
     */
    @TableField("ID")
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * nsrsbh*
     */
    @TableField("NSRSBH")
    private String nsrsbh;

    /**
     * ticket*
     */
    @TableField("TICKET")
    private String ticket;

    /**
     * 批次号
     */
    @TableField("BATCH_NUMBER")
    private String batchNumber;

    /**
     * 纳税人名称
     */
    @TableField("COMPANY_NAME")
    private String companyName;

    /**
     * 取数状态
     */
    @TableField("FETCH_STATUS")
    private String fetchStatus;

    /**
     * 取数次数
     */
    @TableField("FETCH_TIMES")
    private Integer fetchTimes;

    /**
     * 地区标识
     */
    @TableField("AREA")
    private String area;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;


    /**
     * 产品ID，贷后可视化使用
     */
    @TableField("PRODUCT_ID")
    private String productId;


    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;



}
