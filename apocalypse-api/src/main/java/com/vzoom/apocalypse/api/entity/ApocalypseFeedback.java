package com.vzoom.apocalypse.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
    private String id;

    /**
     * 请求流水
     */
    @TableField("REQ_NO")
    private String req_no;

    /**
     * md5值
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
    private String original_text;

    /**
     * 反馈信息的处理后的报文
     * xxxx|xxxx|xxxx|xxxx....
     */
    @TableField("TREATED_TEXT")
    private String treated_text;


    /**
     * 反馈信息 流向：税局接口标识
     * （用于区分反馈给税局的接口类型，每个税局每个接口都应该有值）
     */
    @TableField("TARGET")
    private String target;

    /**
     * 反馈 datagrid 的报文
     */
    @TableField("REQUEST_JSON")
    private String request_json;

    /**
     * 统一状态码
     * 反馈成功：0000
     * 反馈失败：9999
     * 参数错误：1111
     * 正在反馈：2222
     * 网络异常：3333
     *
     */
    @TableField("RESP_CODE")
    private String resp_code;

    /**
     * 处理返回信息
     */
    @TableField("RESP_MSG")
    private String resp_msg;

    /**
     * 税局返回代码
     */
    @TableField("TAX_RESP_CODE")
    private String tax_resp_code;

    /**
     * 税局返回信息
     */
    @TableField("TAX_RESP_MSG")
    private String tax_resp_msg;

    /**
     * 录入时间
     */
    @TableField("LRSJ")
    private Date lrsj;

    /**
     * 记录更新时间
     */
    @TableField("UPDATETIME")
    private Date updatetime;


}
