package com.template.template.infrastructure.clients;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.gateways.ChargingPlugNotificationGateway;
import com.template.template.infrastructure.clients.opendatahubmobility.OpenDataHubMobilityClient;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WebClientNotificationManager implements ChargingPlugNotificationGateway {
    private Counter currentStatusNotificationCounter;
    private Counter dailyReportNotificationCounter;

    @Autowired
    public WebClientNotificationManager(MeterRegistry meterRegistry) {
        currentStatusNotificationCounter = Counter.builder("current_status_notification_counter")
                .description("Number of requests sent to /v2/tree%2Cnode/EChargingPlug/%2A/{from}/{to}?limit=200&offset=0&where=sorigin.eq.%22DRIWE%22&shownull=false&distinct=true&timezone=UTC Open Data Hub endpoint")
                .register(meterRegistry);

        dailyReportNotificationCounter = Counter.builder("daily_report_notification_counter")
                .description("Number of requests sent to /v2/tree%2Cnode/EChargingPlug/%2A/latest?limit=200&offset=0&where=sorigin.eq.%22DRIWE%22&shownull=false&distinct=true&timezone=UTC Open Data Hub endpoint")
                .register(meterRegistry);
    }

    @Override
    public void notifyChargingPlugStationCurrentStatus(final String path, final String uri,
                                                       final ChargingPlugStationCurrentStatus notification) {
        currentStatusNotificationCounter.increment();
        WebClient.create(path)
                .post()
                .uri(uri)
                .accept(MediaType.ALL)
                .bodyValue(notification)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful, clientResponse -> {
                    log.info("Charging plug station last status sent to " + path + uri);
                    return Mono.empty();
                })
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error sending charging plug station last status to " + path + uri);
                    return Mono.empty();
                })
                .bodyToMono(Void.class)
                .onErrorResume(throwable -> {
                    log.error("Error connecting to subscriber " + path);
                    return Mono.empty();
                })
                .subscribe();

    }

    @Override
    public void notifyChargingPlugStationDailyReport(final String path, final String uri,
                                                     final ChargingPlugStationRecord notification) {
        dailyReportNotificationCounter.increment();
        WebClient.create(path)
                .post()
                .uri(uri)
                .accept(MediaType.ALL)
                .bodyValue(notification)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful, clientResponse -> {
                    log.info("Charging plug station daily sent to " + path + uri);
                    return Mono.empty();
                })
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error sending charging plug station daily to " + path + uri);
                    return Mono.empty();
                })
                .bodyToMono(Void.class)
                .onErrorResume(throwable -> {
                    log.error("Error connecting to subscriber " + path);
                    return Mono.empty();
                })
                .subscribe();

    }
}
