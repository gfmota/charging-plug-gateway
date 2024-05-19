package com.template.template.infrastructure.clients.opendatahubmobility.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenDataHubMobilityResponseMeasurement {
    @JsonProperty("tmeasurements")
    private List<Measurement> measurements;

    @Data
    public static class Measurement {
        @JsonProperty("mvalue")
        private Boolean value;
        @JsonProperty("mvalidtime")
        private String moment;
    }
}
