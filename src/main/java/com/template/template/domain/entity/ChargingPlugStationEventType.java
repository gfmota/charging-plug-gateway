package com.template.template.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ChargingPlugStationEventType {
    DAILY("DAILY"),
    LAST_STATUS("LAST_STATUS");

    @JsonValue
    private String label;

    ChargingPlugStationEventType(final String label) {
        this.label = label;
    }
}
