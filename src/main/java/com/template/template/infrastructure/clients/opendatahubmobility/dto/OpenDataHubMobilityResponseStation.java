package com.template.template.infrastructure.clients.opendatahubmobility.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenDataHubMobilityResponseStation {
    @JsonProperty("scoordinate")
    private Coordinates coordinate;
    @JsonProperty("sdatatypes")
    private DataTypes dataTypes;

    @Data
    public static class Coordinates {
        private Double x;
        private Double y;
    }

    @Data
    public static class DataTypes {
        @JsonProperty("echarging-plug-status")
        private OpenDataHubMobilityResponseMeasurement status;
    }
}
