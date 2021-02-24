package com.vzoom.apocalypse.api.service.impl;

import com.vzoom.apocalypse.api.dto.InvokeRequest;
import com.vzoom.apocalypse.api.service.InvokeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/2/24
 */
@Service
@Slf4j
public class InvokeServiceImpl implements InvokeService {


    /**
     * @Author: chenlei
     * @Description: 组装请求出口网关请求参数
     * @Date: 2020-11-05 17:50
     * @Param ticket:
     * @Param datasourceApi:
     * @Param wrappedReqMap:
     */
    @Override
    public InvokeRequest assembleRequestObject() {
/*        InvokeRequest request = new InvokeRequest();
        String ticket = collectContext.getTicket();
        DatasourceApi datasourceApi = collectContext.getApi();
        Map<String, String> wrappedReqMap = collectContext.getWrappedMap();
        String requestId = collectContext.getRequestId();
        request.setMinistryCode(datasourceApi.getMinistryCode());
        request.setRequestData(wrappedReqMap.get("datagram"));
        request.setRequestId(requestId);
        request.setTicketId(ticket);
        request.setHeader(wrappedReqMap.get("header"));
        DatasourceMinistryConfig datasourceMinistryConfig = constantConfig.getDatasourceMinistryConfig(datasourceApi.getMinistryCode());
        request.setEncryptedField(datasourceApi.getEncryptedField() == null ? datasourceMinistryConfig.getEncryptedField() : datasourceApi.getEncryptedField());
        request.setDecryptedField(datasourceApi.getDecryptedField() == null ? datasourceMinistryConfig.getDecryptedField() : datasourceApi.getDecryptedField());
        request.setFilledField(datasourceApi.getFilledField() == null ? datasourceMinistryConfig.getFilledField() : datasourceApi.getFilledField());
        request.setSignatureField(datasourceApi.getSignatureField() == null ? datasourceMinistryConfig.getSignatureField() : datasourceApi.getSignatureField());
        request.setEncryptAlgorithm(datasourceApi.getEncryptAlgorithm() == null ? datasourceMinistryConfig.getEncryptAlgrithm() : datasourceApi.getEncryptAlgorithm());
        request.setSignatureAlgorithm(datasourceApi.getSignatureAlgorithm() == null ? datasourceMinistryConfig.getSignatureAlgrithm() : datasourceApi.getSignatureAlgorithm());
        request.setRequestUrl(getInvokeUrl(datasourceMinistryConfig, datasourceApi, collectContext.getReqMap()));
        request.setRequestType(datasourceApi.getRequestType() == null ? datasourceMinistryConfig.getRequestType() : datasourceApi.getRequestType());
        request.setResponseType(datasourceApi.getResponseType() == null ? datasourceMinistryConfig.getResponseType() : datasourceApi.getResponseType());
        request.setHttpMethod(datasourceApi.getHttpMethod() == null ? "1" : datasourceApi.getHttpMethod());
        return request;*/
    return null;
    }
}
