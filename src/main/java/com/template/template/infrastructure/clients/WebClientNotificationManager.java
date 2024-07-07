package com.template.template.infrastructure.clients;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.gateways.ChargingPlugNotificationGateway;
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
    private Counter hourlyReportNotificationCounter;

    @Autowired
    public WebClientNotificationManager(MeterRegistry meterRegistry) {
        currentStatusNotificationCounter = Counter.builder("current_status_notification_counter")
                .description("Number of notifications (requests) made about current status")
                .register(meterRegistry);

        dailyReportNotificationCounter = Counter.builder("daily_report_notification_counter")
                .description("Number of notifications (requests) made about daily report")
                .register(meterRegistry);

        hourlyReportNotificationCounter = Counter.builder("hourly_report_notification_counter")
                .description("Number of notifications (requests) made about hourly report")
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

    @Override
    public void notifyChargingPlugStationHourlyReport(String path, String uri, ChargingPlugStationRecord notification) {
        hourlyReportNotificationCounter.increment();
        WebClient.create(path)
                .post()
                .uri(uri)
                .accept(MediaType.ALL)
                .bodyValue(notification)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful, clientResponse -> {
                    log.info("Charging plug station hourly sent to " + path + uri);
                    return Mono.empty();
                })
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error sending charging plug station hourly to " + path + uri);
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
