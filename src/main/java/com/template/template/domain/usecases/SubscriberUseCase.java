package com.template.template.domain.usecases;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationEventType;
import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.entity.SubscribedService;
import com.template.template.domain.gateways.ChargingPlugNotificationGateway;
import com.template.template.domain.gateways.SubscribedServicesGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SubscriberUseCase {
    @Autowired
    private SubscribedServicesGateway subscribedServicesGateway;
    @Autowired
    private ChargingPlugRecordUsecase chargingPlugRecordUsecase;
    @Autowired
    private ChargingPlugNotificationGateway chargingPlugNotificationGateway;

    public void subscribe(final SubscribedService subscribedService) throws IOException {
        subscribedServicesGateway.subscribe(subscribedService);
    }

   @Scheduled(cron = "0 */3 * * * ?")
//    @Scheduled(cron = "0 */5 * * * ?")
    private void notifyLastStatusSubscribers() throws IOException {
        log.info("Notifying last status subscribers");
        final List<SubscribedService> subscribersPath =
                subscribedServicesGateway.getSubscribedServices(ChargingPlugStationEventType.LAST_STATUS);
        if (subscribersPath.isEmpty()) {
            log.info("No subscribers for last status");
            return;
        }

        final ChargingPlugStationCurrentStatus currentStatus =
                chargingPlugRecordUsecase.getChargingPlugCurrentStatus().orElseThrow(IOException::new);

        subscribersPath.forEach(subscriber -> {
            chargingPlugNotificationGateway.notifyChargingPlugStationCurrentStatus(subscriber.getPath(),
                    subscriber.getUri(), currentStatus);
        });
    }

   @Scheduled(cron = "0 */3 * * * ?")
//    @Scheduled(cron = "0 0 0 * * ?")
    private void notifyDailySubscribers() throws IOException {
        log.info("Notifying daily report subscribers");
        final List<SubscribedService> subscribersPath =
                subscribedServicesGateway.getSubscribedServices(ChargingPlugStationEventType.DAILY);
        if (subscribersPath.isEmpty()) {
            log.info("No subscribers for daily report");
            return;
        }

        final ChargingPlugStationRecord recordFromLastDay =
                chargingPlugRecordUsecase.getChargingPlugRecordFromLastDay().orElseThrow(IOException::new);

        subscribersPath.forEach(subscriber -> {
            chargingPlugNotificationGateway.notifyChargingPlugStationDailyReport(subscriber.getPath(),
                    subscriber.getUri(), recordFromLastDay);
        });
    }

   @Scheduled(cron = "0 */3 * * * ?")
//    @Scheduled(cron = "0 0 1 * * ?")
    private void notifyHourlySubscribers() throws IOException {
        log.info("Notifying hourly report subscribers");
       final List<SubscribedService> subscribersPath =
               subscribedServicesGateway.getSubscribedServices(ChargingPlugStationEventType.HOURLY);
       if (subscribersPath.isEmpty()) {
           log.info("No subscribers for hourly report");
           return;
       }

        final ChargingPlugStationRecord recordFromLastDay =
                chargingPlugRecordUsecase.getChargingPlugRecordFromTimeRange(
                        LocalDateTime.now().minusHours(1), LocalDateTime.now()).orElseThrow(IOException::new);

        subscribersPath.forEach(subscriber -> {
            chargingPlugNotificationGateway.notifyChargingPlugStationHourlyReport(subscriber.getPath(),
                    subscriber.getUri(), recordFromLastDay);
        });
    }
}
