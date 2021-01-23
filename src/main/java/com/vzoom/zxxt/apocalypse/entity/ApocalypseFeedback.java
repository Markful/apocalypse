package com.vzoom.zxxt.apocalypse.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 反馈记录表
 *
 */
@TableName("apocalypse_feedback")
public class ApocalypseFeedback {

    /**
     * UUID*
     */
    @TableField("ID")
    private String id;

    /**
     * 请求流水
     */
    @TableField("REQNO")
    private String reqNo;

    /**
     * 纳税人识别号*
     */
    @TableField("NSRSBH")
    private String nsrsbh;

    /**
     * 反馈信息的来源
     */
    @TableField("SOURCE")
    private String source;

    /**
     * 反馈信息的原始报文
     * xxxx|xxxx|xxxx|xxxx....
     */
    @TableField("ORIGINALTEXT")
    private String originalText;

    /**
     * 反馈信息的处理后的报文
     * xxxx|xxxx|xxxx|xxxx....
     */
    @TableField("TREATEDJSON")
    private String treatedJson;


    /**
     * 反馈信息 流向：税局接口标识
     * （用于区分反馈给税局的接口类型，每个税局每个接口都应该有值）
     */
    @TableField("TARGET")
    private String target;


    /**
     * 统一状态码
     * 反馈成功：0000
     * 反馈失败：9999
     * 参数错误：1111
     * 正在反馈：2222
     * 网络异常：3333
     *
     */
    @TableField("RESPCODE")
    private String respCode;

    /**
     * 处理返回信息
     */
    @TableField("RESPMSG")
    private String respMsg;

    /**
     * 税局返回代码
     */
    @TableField("TAXRESPCODE")
    private String taxRespCode;

    /**
     * 税局返回信息
     */
    @TableField("TAXRESPMSG")
    private String taxRespMsg;

    /**
     * 录入时间
     */
    @TableField("LRSJ")
    private Date lrsj;


}
