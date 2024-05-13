package com.template.template.infrastructure.clients.opendatahubmobility.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenDataHubMobilityResponseData {
    @JsonProperty("EChargingPlug")
    private ChargingPlug chargingPlug;

    @Data
    public static class ChargingPlug {
        private Map<String, OpenDataHubMobilityResponseStation> stations;
    }
}
