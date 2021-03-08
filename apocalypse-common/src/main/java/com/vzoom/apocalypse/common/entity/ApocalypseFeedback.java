package com.vzoom.apocalypse.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 反馈记录表
 *
 */
@TableName("apocalypse_feedback")
@Data
public class ApocalypseFeedback {

    /**
     * UUID*
     */
    @TableField("ID")
    @TableId(type = IdType.UUID)
    private String id;


    /**
     * TICKET_ID
     */
    @TableField("TICKET_ID")
    private String ticketId;

    /**
     * 请求流水
     */
    @TableField("REQ_NO")
    private String reqNo;

    /**
     * AREA
     */
    @TableField("AREA")
    private String area;

    /**
     * MINISTRY_CODE，与datagrid中的配置一致
     */
    @TableField("MINISTRY_CODE")
    private String ministryCode;

    /**
     * 处理后的反馈文件md5值
     */
    @TableField("MD5")
    private String md5;

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
    @TableField("ORIGINAL_TEXT")
    private String originalText;

    /**
     * 反馈信息的处理后的报文
     * {"nsrsbh":"","xxxx":"111","vvvv":"2222"}....
     */
    @TableField("TREATED_JSON")
    private String treatedJson;

    /**
     * 调用税局的原始明文报文
     */
    @TableField("TREATED_XML")
    private String treatedXml;

    /**
     * 反馈 datagrid 的报文
     */
    @TableField("REQUEST_JSON")
    private String requestJson;

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
     * 初始入库: 1111
     * 正在反馈：2222
     * 网络异常：3333
     * 参数错误：4444
     */
    @TableField("RESP_CODE")
    private String respCode;

    /**
     * 处理返回信息
     */
    @TableField("RESP_MSG")
    private String respMsg;

    /**
     * 税局返回代码
     */
    @TableField("TAX_RESP_CODE")
    private String taxRespCode;

    /**
     * 税局返回信息
     */
    @TableField("TAX_RESP_MSG")
    private String taxRespMsg;

    /**
     * 录入时间
     */
    @TableField("LRSJ")
    private Date lrsj;

    /**
     * UPDATE_TIME
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;




}
