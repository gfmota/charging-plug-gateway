package com.template.template.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ChargingPlugStatus {
    AVAILABLE("AVAILABLE"),
    UNAVAILABLE("UNAVAILABLE");

    @JsonValue
    private String value;

    ChargingPlugStatus(String label) {
        this.value = label;
    }
}
