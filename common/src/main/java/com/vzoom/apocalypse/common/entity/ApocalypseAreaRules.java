package com.vzoom.apocalypse.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 地区反馈规则表
 * @Author: wangyh
 * @Date: 2021/1/29
 */
@TableName("apocalypse_rules")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApocalypseAreaRules {

    /**
     * UUID*
     */
    @TableField("ID")
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * SERIAL_NUM 处理字段优先级
     * 0,1,2,3,4
     */
    @TableField("SERIAL_NUM")
    private String serial_num;

    /**
     * AREA
     */
    @TableField("AREA")
    private String area;

    /**
     * MINISTRY_CODE，与datagrid中的配置一致
     */
    @TableField("MINISTRY_CODE")
    private String ministry_code;

    /**
     * FEEDBACK_field
     * nsrsbh|nsrmc|dkje....
     */
    @TableField("FEEDBACK_FIELD")
    private String feedback_field;


    /**
     * RULE_EXPRESSION（使用spel表达式）
     * 为空表示完全根据银行传入的字段，不进行处理
     *
     */
    @TableField("RULE_EXPRESSION")
    private String rule_expression;

    /**
     * LRSJ
     * 录入时间
     */
    @TableField("LRSJ")
    private String lrsj;

}
