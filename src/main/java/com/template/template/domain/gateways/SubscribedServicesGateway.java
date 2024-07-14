package com.template.template.domain.gateways;

import com.template.template.domain.entity.ChargingPlugStationEventType;
import com.template.template.domain.entity.SubscribedService;

import java.io.IOException;
import java.util.List;

public interface SubscribedServicesGateway {
    void subscribe(SubscribedService subscribedService) throws IOException;

    List<SubscribedService> getSubscribedServices(ChargingPlugStationEventType eventType) throws IOException;
}
