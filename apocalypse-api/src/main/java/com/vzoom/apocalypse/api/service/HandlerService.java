package com.vzoom.apocalypse.api.service;

import com.vzoom.apocalypse.common.entity.ApocalypseProperty;

public interface HandlerService {

    void handle();

    void handle(ApocalypseProperty apocalypseProperty);

}
