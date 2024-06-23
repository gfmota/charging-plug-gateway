package com.template.template.infrastructure.controllers;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.usecases.ChargingPlugRecordUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
@Slf4j
public class CharginPlugStationController {
    @Autowired
    private ChargingPlugRecordUsecase chargingPlugRecordUsecase;

    @GetMapping("/currentStatus")
    private ResponseEntity<ChargingPlugStationCurrentStatus> getCurrentStatus() {
        log.info("Received current status request");
        return ResponseEntity.ok(chargingPlugRecordUsecase.getChargingPlugCurrentStatus());
    }

    @GetMapping("/lastDayReport")
    private ResponseEntity<ChargingPlugStationRecord> getLastDayReport() {
        log.info("Received last day report request");
        return ResponseEntity.ok(chargingPlugRecordUsecase.getChargingPlugRecordFromLastDay());
    }
}
