package com.template.template.domain.entity;

import java.util.List;

public record ChargingPlugStation(
        String id,
        Coordinates position,
        List<ChargingPlug> plugs) {
}
