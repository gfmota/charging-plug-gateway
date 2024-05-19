package com.template.template.domain.entity;

import java.util.Map;

public record ChargingPlugStationStatus(
        String id,
        Coordinates position,
        Map<String, ChargingPlugStatus> plugs) {
}
