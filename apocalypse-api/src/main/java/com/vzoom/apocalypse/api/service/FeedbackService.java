package com.vzoom.apocalypse.api.service;


public interface FeedbackService {

    void readFeedbackFile(String area);

    void pushFeedbackInfo(String area) throws Exception;

}
