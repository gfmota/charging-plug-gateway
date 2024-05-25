package com.template.template.domain.usecases;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationEventType;
import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.entity.SubscribedService;
import com.template.template.domain.gateways.SubscribedServicesGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class SubscriberUseCase {
    @Autowired
    private SubscribedServicesGateway subscribedServicesGateway;

    @Autowired
    private ChargingPlugRecordUsecase chargingPlugRecordUsecase;

    public void subscribe(final SubscribedService subscribedService) throws IOException {
        subscribedServicesGateway.subscribe(subscribedService);
    }

    @Scheduled(cron = "0 */5 * * * ?")
    private void notifyLastStatusSubscribers() throws IOException {
        log.info("Notifying subscribers");
        final ChargingPlugStationCurrentStatus currentStatus = chargingPlugRecordUsecase.getChargingPlugCurrentStatus();
        final List<SubscribedService> subscribersPath =
                subscribedServicesGateway.getSubscribedServices(ChargingPlugStationEventType.LAST_STATUS);

        subscribersPath.forEach(subscriber -> {
            WebClient.create(subscriber.getPath())
                    .post()
                    .uri(subscriber.getUri())
                    .accept(MediaType.ALL)
                    .bodyValue(currentStatus)
                    .retrieve()
                    .onStatus(HttpStatusCode::is2xxSuccessful, clientResponse -> {
                        log.info("Charging plug station last status sent to " + subscriber);
                        return Mono.empty();
                    })
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("Error sending charging plug station last status to " + subscriber);
                        return Mono.empty();
                    })
                    .bodyToMono(Void.class)
                    .onErrorResume(throwable -> {
                        log.error("Error connecting to subscriber " + subscriber);
                        return Mono.empty();
                    })
                    .subscribe();
        });
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void notifyDailySubscribers() throws IOException {
        log.info("Notifying subscribers");
        final ChargingPlugStationRecord currentStatus =
                chargingPlugRecordUsecase.getChargingPlugRecordFromLastDay();
        final List<SubscribedService> subscribersPath =
                subscribedServicesGateway.getSubscribedServices(ChargingPlugStationEventType.DAILY);

        subscribersPath.forEach(subscriber -> {
            WebClient.create(subscriber.getPath())
                    .post()
                    .uri(subscriber.getUri())
                    .accept(MediaType.ALL)
                    .bodyValue(currentStatus)
                    .retrieve()
                    .onStatus(HttpStatusCode::is2xxSuccessful, clientResponse -> {
                        log.info("Charging plug station daily sent to " + subscriber);
                        return Mono.empty();
                    })
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("Error sending charging plug station daily to " + subscriber);
                        return Mono.empty();
                    })
                    .bodyToMono(Void.class)
                    .onErrorResume(throwable -> {
                        log.error("Error connecting to subscriber " + subscriber);
                        return Mono.empty();
                    })
                    .subscribe();
        });
    }
}
