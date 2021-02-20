package com.vzoom.apocalypse.api.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@TableName("apocalypse_property")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApocalypseProperty {

    /**
     * UUID*
     */
    @TableField("ID")
    private String id;

    /**
     * AREA
     * 地区
     */
    @TableField("AREA")
    private String area;

    /**
     * PRODUCT_ID
     * 产品编号
     */
    @TableField("PRODUCT_ID")
    private String product_id;

    /**
     * FEEDBACK_FILE_PATH
     * 反馈文件路径配置
     */
    @TableField("FEEDBACK_FILE_PATH")
    private String feedback_file_path;

    /**
     * POSTLOAN_FILE_PATH
     * 贷后名单文件路径
     */
    @TableField("POSTLOAN_FILE_PATH")
    private String postloan_file_path;

    /**
     * FEEDBACK_FREQUENCY
     * 反馈的频率 10
     */
    @TableField("FEEDBACK_FREQUENCY")
    private String feedback_frequency;

    /**
     * POSTLOAN_FREQUENCY
     * 贷后取数频率
     *
     */
    @TableField("POSTLOAN_FREQUENCY")
    private String postloan_frequency;


    /**
     * FEEDBACK_STRATEGY
     * 反馈文件读取 策略：SFTP：1，FTP:2，本地文本文件：3
     * 如果是ZIP，则自动识别解压缩
     */
    @TableField("FEEDBACK_STRATEGY")
    private String feedback_strategy;


    /**
     * POSTLOAN_STRATEGY
     * 贷后名单策略：SFTP：1，FTP:2，本地文本文件：3
     * 如果是ZIP，则自动识别解压缩
     */
    @TableField("POSTLOAN_STRATEGY")
    private String postloan_strategy;

    /**
     * LRSJ
     * 录入时间
     */
    @TableField("LRSJ")
    private Date lrsj;



}
