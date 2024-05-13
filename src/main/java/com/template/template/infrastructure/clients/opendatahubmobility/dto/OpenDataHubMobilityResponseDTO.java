package com.template.template.infrastructure.clients.opendatahubmobility.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenDataHubMobilityResponseDTO {
    private OpenDataHubMobilityResponseData data;
}
