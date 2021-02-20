package com.vzoom.apocalypse.api.service;


public interface ExceptionService {

    void toException(Exception e,String... args);

    void insertAnomalyLogByException(Exception e, String exceptionMsg);

}
