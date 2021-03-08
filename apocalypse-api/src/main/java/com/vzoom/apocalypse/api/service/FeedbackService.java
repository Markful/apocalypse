package com.vzoom.apocalypse.api.service;


public interface FeedbackService {

    void readFeedbackFile(String area);

    void pushFeedbackInfo(String area) throws Exception;

    void readAndPushPostloanInfo(String area) throws Exception;

    void rePushPostloanInfo(String area);
}
