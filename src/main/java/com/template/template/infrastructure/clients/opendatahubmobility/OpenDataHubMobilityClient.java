package com.template.template.infrastructure.clients.opendatahubmobility;

import com.template.template.infrastructure.clients.opendatahubmobility.dto.OpenDataHubMobilityResponseDTO;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://mobility.api.opendatahub.com")
public interface OpenDataHubMobilityClient {
    @GetMapping(value = "/v2/tree%2Cnode/EChargingPlug/%2A/{from}/{to}", consumes = "application/json")
    OpenDataHubMobilityResponseDTO getReportFromTimeRange(
            @NonNull @PathVariable("from") String from, @NonNull @PathVariable("to") String to);
}
