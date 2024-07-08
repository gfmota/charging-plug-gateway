package com.template.template.domain.entity;

import java.util.ArrayList;
import java.util.List;

public record ChargingPlugStation(
        String id,
        Coordinates position,
        ArrayList<ChargingPlug> plugs) {
}
