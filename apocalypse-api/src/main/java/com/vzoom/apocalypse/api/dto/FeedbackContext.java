package com.vzoom.apocalypse.api.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/26
 */
@Data
public class FeedbackContext {

    /*数据库id*/
    private String id;

    /*地区编码，对应数据库处理规则*/
    private String area;

    /*请求流水*/
    private String req_no;

    /*产品id*/
    private String product_id;

    /*md5值*/
    private String md5;

    /*纳税人识别号*/
    private String nsrsbh;

    /*反馈信息的来源*/
    private String source;

    /*反馈信息的原始报文 xxxx|xxxx|xxxx|xxxx....*/
    private String original_text;

    /*反馈信息的处理后的报文{xxxx：aaa,xxxx:bbb,xxxx:ccc,xxxx:ddd....}*/
    private String treated_json;

    /*反馈信息的处理后的报文XML*/
    private String treated_xml;

    /*反馈信息 流向：税局接口标识（用于区分反馈给税局的接口类型，每个税局每个接口都应该有值）*/
    private String target;

    /*统一状态码
     * 反馈成功：0000
     * 反馈失败：9999
     * 参数错误：1111
     * 正在反馈：2222
     * 网络异常：3333
     */
    private String resp_code;

    /*处理返回信息*/
    private String resp_msg;

    /*税局返回代码*/
    private String tax_resp_code;

    /*税局返回信息*/
    private String tax_resp_msg;

    /*FEEDBACK_FILE_PATH 反馈文件路径配置*/
    private String feedback_file_path;

    /*POSTLOAN_FILE_PATH 贷后名单文件路径 */
    private String postloan_file_path;

    /*FEEDBACK_FREQUENCY 反馈的频率 10 */
    private String feedback_frequency;

    /*POSTLOAN_FREQUENCY 贷后取数频率 */
    private String postloan_frequency;

    /*FEEDBACK_STRATEGY 反馈文件读取 策略：SFTP：1，FTP:2，本地文本文件：3。如果是ZIP，则自动识别解压缩*/
    private String feedback_strategy;

    /*POSTLOAN_STRATEGY：贷后名单策略：SFTP：1，FTP:2，本地文本文件：3
     * 如果是ZIP，则自动识别解压缩
     */
    private String postloan_strategy;

    /*录入时间*/
    private Date lrsj;

}
