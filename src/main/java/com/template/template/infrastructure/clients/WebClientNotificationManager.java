package com.template.template.infrastructure.clients;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.gateways.ChargingPlugNotificationGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WebClientNotificationManager implements ChargingPlugNotificationGateway {
    @Override
    public void notifyChargingPlugStationCurrentStatus(final String path, final String uri,
                                                       final ChargingPlugStationCurrentStatus notification) {
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
