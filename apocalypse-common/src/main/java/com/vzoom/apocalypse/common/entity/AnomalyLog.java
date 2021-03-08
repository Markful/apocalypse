package com.vzoom.apocalypse.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/2/19
 */
@TableName("apocalypse_anomaly")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnomalyLog {

    /**
     * nsrsbh*
     */
    @TableField("NSRSBH")
    private String nsrsbh;

    /**
     * EXCEP_CODE*
     */
    @TableField("EXCEP_CODE")
    private String excepCode;

    /**
     * EXCEP_MSG*
     */
    @TableField("EXCEP_MSG")
    private String excepMsg;

    /**
     * EXCEP_TIME*
     */
    @TableField("EXCEP_TIME")
    private String excepTime;

    /**
     * CREATE_DATE*
     */
    @TableField("CREATE_DATE")
    private String createDate;

}
