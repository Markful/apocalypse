package com.vzoom.apocalypse.common.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/26
 */
@Data
public class FeedbackContext {

    /*数据库id*/
    private String id;

    /*地区名称，对应数据库处理规则*/
    private String area;

    /*请求流水*/
    private String req_no;

    /*地区编码*/
    private String ministryCode;

    /*产品id*/
    private String productId;

    /*md5值*/
    private String md5;

    /*纳税人识别号*/
    private String nsrsbh;

    /*反馈信息的来源*/
    private String source;

    /*反馈信息的原始报文 xxxx|xxxx|xxxx|xxxx....*/
    private String originalText;

    /*反馈信息的处理后的报文{xxxx：aaa,xxxx:bbb,xxxx:ccc,xxxx:ddd....}*/
    private String treatedJson;

    /*处理后的反馈Map，用于拼装模板*/
    private Map feedbackMap;

    /*反馈信息的处理后的报文XML*/
    private String treatedXml;

    /*发送给datagrid的报文*/
    private String requestJson;

    /*反馈信息 流向：税局接口标识（用于区分反馈给税局的接口类型，每个税局每个接口都应该有值）*/
    private String target;

    /*统一状态码
     * 反馈成功：0000
     * 反馈失败：9999
     * 参数错误：1111
     * 正在反馈：2222
     * 网络异常：3333
     */
    private String respCode;

    /*处理返回信息*/
    private String respMsg;

    /*税局返回代码*/
    private String taxRespCode;

    /*税局返回信息*/
    private String taxRespMsg;

    /*FEEDBACK_FILE_PATH 反馈文件路径配置*/
    private String feedbackFilePath;

    /*POSTLOAN_FILE_PATH 贷后名单文件路径 */
    private String postloanFilePath;

    /*FEEDBACK_FREQUENCY 反馈的频率 10 */
    private String feedbackFrequency;

    /*POSTLOAN_FREQUENCY 贷后取数频率 */
    private String postloanFrequency;

    /*FEEDBACK_STRATEGY 反馈文件读取 策略：SFTP：1，FTP:2，本地文本文件：3。如果是ZIP，则自动识别解压缩*/
    private String feedbackStrategy;

    /*POSTLOAN_STRATEGY：贷后名单策略：SFTP：1，FTP:2，本地文本文件：3
     * 如果是ZIP，则自动识别解压缩
     */
    private String postloanStrategy;

    /*模板类型，如果税局反馈模板有多个，需要通过配置此字段来实现不同的模板*/
    private String templateType;

    /*模板表达式*/
    private String requestTemplate;

    /*录入时间*/
    private Date lrsj;

}
