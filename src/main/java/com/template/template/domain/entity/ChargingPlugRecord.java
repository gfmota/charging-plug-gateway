package com.template.template.domain.entity;

import java.time.LocalDateTime;

public record ChargingPlugRecord(
        LocalDateTime moment,
        ChargingPlugStatus status) {
}
