package com.vzoom.apocalypse.common.service;

import com.vzoom.apocalypse.common.dto.InvokePostloanRequest;
import com.vzoom.apocalypse.common.dto.InvokeRequest;
import com.vzoom.apocalypse.common.dto.InvokeResponse;
import com.vzoom.apocalypse.common.entity.ApocalypseAfterLoan;
import com.vzoom.apocalypse.common.entity.ApocalypseFeedback;

import java.util.List;

/**
 * @Description: 推送反馈数据的抽象接口，需要各自jar包实现此接口方法
 * @Author: wangyh
 * @Date: 2021/2/24
 */
public interface InvokeService {

    /**
     * 设定区域，用于从工厂方法中获取对应的实现类
     * @return
     */
    String getArea();

    /**
     * 拼装反馈请求datagrid的报文
     * @param feedback
     * @return
     */
    InvokeRequest assembleFeedbackRequestObject(ApocalypseFeedback feedback);

    /**
     * 推送反馈数据给到datagrid，具体业务逻辑在各自jar包中实现
     * @param feedbackList
     */
    InvokeResponse pushFeedbackRequest(List<ApocalypseFeedback> feedbackList);


    /**
     * 拼装贷后名单通知报文
     * 如果使用默认实现，则直接返回null
     * @param apocalypseAfterLoan 贷后取数用户
     * @return 返回请求参数，如果使用默认实现，则返回null
     */
    InvokePostloanRequest assemblePostlaonRequest(ApocalypseAfterLoan apocalypseAfterLoan);

    /**
     * 个性化自定义获取需要取贷后的数据名单
     * @param area 地区
     * @param feedbackList 所有的反馈数据，自行过滤出贷后名单
     * @return
     */
    List<ApocalypseFeedback> getPostloanList(String area,List<ApocalypseFeedback> feedbackList);
}
