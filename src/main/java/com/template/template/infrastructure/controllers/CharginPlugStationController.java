package com.template.template.infrastructure.controllers;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.usecases.ChargingPlugRecordUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("/")
@Slf4j
public class CharginPlugStationController {
    @Autowired
    private ChargingPlugRecordUsecase chargingPlugRecordUsecase;

    @GetMapping("/currentStatus")
    private ResponseEntity<ChargingPlugStationCurrentStatus> getCurrentStatus() {
        log.info("Received current status request");
        final Optional<ChargingPlugStationCurrentStatus> status = chargingPlugRecordUsecase.getChargingPlugCurrentStatus();
        if (status.isPresent()) {
            return ResponseEntity.ok(status.get());
        }
        log.info("Failed to get current status");
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/lastDayReport")
    private ResponseEntity<ChargingPlugStationRecord> getLastDayReport() {
        log.info("Received last day report request");
        final Optional<ChargingPlugStationRecord> record = chargingPlugRecordUsecase.getChargingPlugRecordFromLastDay();
        if (record.isPresent()) {
            return ResponseEntity.ok(record.get());
        }
        log.info("Failed to get last day report");
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/lastHourReport")
    private ResponseEntity<ChargingPlugStationRecord> getLastHourReport() {
        log.info("Received last hour report request");
        final Optional<ChargingPlugStationRecord> record = chargingPlugRecordUsecase.getChargingPlugRecordFromLastHour();
        if (record.isPresent()) {
            return ResponseEntity.ok(record.get());
        }
        log.info("Failed to get last hour report");
        return ResponseEntity.internalServerError().build();
    }
}
