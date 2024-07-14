package com.template.template.domain.usecases;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.gateways.ChargingPlugPublishGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class ChargingPlugStationPublisherUseCase {

    @Autowired
    private ChargingPlugRecordUsecase chargingPlugRecordUsecase;
    @Autowired
    private ChargingPlugPublishGateway chargingPlugPublishGateway;

    @Scheduled(cron = "0 * * * * ?")
//    @Scheduled(cron = "0 */5 * * * ?")
    private void notifyLastStatusSubscribers() {
        log.info("Publishing Charging Plug Stations last status");
        final ChargingPlugStationCurrentStatus currentStatus = chargingPlugRecordUsecase
                .getChargingPlugCurrentStatus().orElseThrow();
        chargingPlugPublishGateway.publishChargingPlugStationCurrentStatus(currentStatus);
    }

    @Scheduled(cron = "30 * * * * ?")
//    @Scheduled(cron = "0 0 0 * * ?")
    private void notifyDailySubscribers() {
        log.info("Publishing Charging Plug Stations daily report");
        final ChargingPlugStationRecord report = chargingPlugRecordUsecase
                .getChargingPlugRecordFromLastDay().orElseThrow();
        chargingPlugPublishGateway.publishChargingPlugStationDailyReport(report);
    }

}
