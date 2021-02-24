package com.vzoom.apocalypse.common.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * AREA
     * 地区
     */
    @TableField("AREA")
    private String area;

    /**
     * MINISTRY_CODE，与datagrid中的配置一致
     */
    @TableField("MINISTRY_CODE")
    private String ministry_code;

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
     * 模板类型（如果一个税局有多个请求模板，可以通过此字段进行区分）
     */
    @TableField("TEMPLATE_TYPE")
    private String template_type;


    /**
     * 模板表达式
     */
    @TableField("REQUEST_TEMPLATE")
    private String request_template;

    /**
     * LRSJ
     * 录入时间
     */
    @TableField("LRSJ")
    private Date lrsj;

}
