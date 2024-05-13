package com.template.template.domain.entity;

import java.util.List;

public record ChargingPlug(
        String id,
        List<ChargingPlugRecord> records) {
}
