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
    private String ministryCode;


    /**
     * USED_FLAG，配置是否启用标识，Y-启用，N-不启用
     */
    @TableField("USED_FLAG")
    private String usedFlag;


    /**
     * PRODUCT_ID
     * 产品编号
     */
    @TableField("PRODUCT_ID")
    private String productId;

    /**
     * FEEDBACK_FILE_PATH
     * 反馈文件路径配置
     */
    @TableField("FEEDBACK_FILE_PATH")
    private String feedbackFilePath;

    /**
     * POSTLOAN_FILE_PATH
     * 贷后名单文件路径
     */
    @TableField("POSTLOAN_FILE_PATH")
    private String postloanFilePath;

    /**
     * FEEDBACK_FREQUENCY
     * 反馈的频率 10
     */
    @TableField("FEEDBACK_FREQUENCY")
    private String feedbackFrequency;

    /**
     * POSTLOAN_FREQUENCY
     * 贷后取数频率
     *
     */
    @TableField("POSTLOAN_FREQUENCY")
    private String postloanFrequency;


    /**
     * FEEDBACK_STRATEGY
     * 反馈文件读取 策略：SFTP：1，FTP:2，本地文本文件：3
     * 如果是ZIP，则自动识别解压缩
     */
    @TableField("FEEDBACK_STRATEGY")
    private String feedbackStrategy;


    /**
     * POSTLOAN_STRATEGY
     * 贷后名单策略：SFTP：1，FTP:2，本地文本文件：3
     * 如果是ZIP，则自动识别解压缩
     */
    @TableField("POSTLOAN_STRATEGY")
    private String postloanStrategy;

    /**
     * 反馈数据推送datagrid规则，状态码为哪些的可以推送给datagrid
     */
    @TableField("PUSH_FEEDBACK_RULE")
    private String pushFeedbackRule;

    /**
     * 贷后名单推送datagrid规则
     */
    @TableField("PUSH_POSTLOAN_RULE")
    private String pushPostloanRule;


    /**
     * 模板类型（如果一个税局有多个请求模板，可以通过此字段进行区分）
     */
    @TableField("TEMPLATE_TYPE")
    private String templateType;

    /**
     * 税务报文模板表达式
     */
    @TableField("TAX_TEMPLATE")
    private String taxTemplate;


    /**
     * 反馈配置的字段
     * eg:nsrsbh|nsrmc|dkje|....
     */
    @TableField("FEEDBACK_PARAM")
    private String feedbackParam;

    /**
     * 税局分配给银行的固定参数，使用小写json串保存
     */
    @TableField("BANK_TAX_PARAM")
    private String bankTaxParam;

    /**
     * URL前置类型：
     * 0-file://
     * 1-http://
     * 2-https://
     * 3-ftp://或者sftp://
     * 5-other
     */
    @TableField("TRANSPORT_TYPE")
    private String transportType;

    /**
     * 调用IP
     * 调用Datagrid的requestUrl参数组成部分
     * 可以使用EL表达式
     */
    @TableField("INVOKE_IP")
    private String invokeIp;

    /**
     * 调用域名
     * 调用Datagrid的requestUrl参数组成部分
     * 可以使用EL表达式
     */
    @TableField("INVOKE_DOMAIN")
    private String invokeDomain;

    /**
     * 调用端口
     * 调用Datagrid的requestUrl参数组成部分
     * 可以使用EL表达式
     */
    @TableField("INVOKE_PORT")
    private String invokePort;

    /**
     * 调用URI
     * 调用Datagrid的requestUrl参数组成部分
     * 可以使用EL表达式
     */
    @TableField("INVOKE_URI")
    private String invokeUri;

    @TableField("GET_FEEDBACK_SQL")
    private String getFeedbackSql;


    @TableField("GET_POSTLOAN_SQL")
    private String getPostloanSql;


    /**
     * 请求报文类型 0(default):json 1:xml
     */
    @TableField("REQUEST_TYPE")
    private String requestType;

    /**
     * 响应报文类型 0(default):json 1:xml
     */
    @TableField("RESPONSE_TYPE")
    private String responseType;

    /**
     * 签名的字段,以";"表示多个,NONE表示无
     */
    @TableField("SIGNATURE_FIELD")
    private String signatureField;

    /**
     * 签名算法
     */
    @TableField("SIGNATURE_ALGORITHM")
    private String signatureAlgorithm;

    /**
     * 请求时需要被填充的字段,以";"分隔表示多字段,"NONE"表示无
     */
    @TableField("FILLED_FIELD")
    private String filledField;

    /**
     * 请求时需要被加密的字段,以";"分隔表示多字段,"NONE"表示无
     */
    @TableField("ENCRYPTED_FIELD")
    private String encryptedField;

    /**
     * 返回时需要被解密的字段
     */
    @TableField("DECRYPTED_FIELD")
    private String decryptedField;

    /**
     * 加密算法
     */
    @TableField("ENCRYPT_ALGORITHM")
    private String encryptAlgorithm;

    /**
     * 个性化分隔符
     */
    @TableField("AREA_SEPARATOR")
    private String areaSeparator;


    /**
     * LRSJ
     * 录入时间
     */
    @TableField("LRSJ")
    private Date lrsj;

}
