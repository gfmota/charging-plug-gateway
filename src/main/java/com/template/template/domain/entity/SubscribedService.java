package com.template.template.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscribedService {
    private String path;
    private String uri;
    private ChargingPlugStationEventType eventType;
}
