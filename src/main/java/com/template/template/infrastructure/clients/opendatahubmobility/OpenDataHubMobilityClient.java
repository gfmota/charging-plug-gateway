package com.template.template.infrastructure.clients.opendatahubmobility;

import com.template.template.infrastructure.clients.opendatahubmobility.dto.OpenDataHubMobilityResponseDTO;
import lombok.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://mobility.api.opendatahub.com", name = "OpenDataHubMobility")
public interface OpenDataHubMobilityClient {
    @GetMapping(value = "/v2/tree%2Cnode/EChargingPlug/%2A/{from}/{to}?limit=200&offset=0&where=sorigin.eq.%22DRIWE%22&shownull=false&distinct=true&timezone=UTC",
            consumes = "application/json")
    @Cacheable(cacheNames = "openDataHubMobilityReportFromTimeRange", key = "#from + '_' + #to")
    OpenDataHubMobilityResponseDTO getReportFromTimeRange(
            @NonNull @PathVariable("from") String from, @NonNull @PathVariable("to") String to);

    @GetMapping(value = "/v2/tree%2Cnode/EChargingPlug/%2A/latest?limit=200&offset=0&where=sorigin.eq.%22DRIWE%22&shownull=false&distinct=true&timezone=UTC",
            consumes = "application/json")
    @Cacheable("openDataHubMobilityLatestReport")
    OpenDataHubMobilityResponseDTO getLatestReport();
}
