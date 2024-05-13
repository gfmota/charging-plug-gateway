package com.template.template.domain.usecases;

import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.gateways.ChargingPlugRecordGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChargingPlugRecordUsecase {
    @Autowired
    private ChargingPlugRecordGateway chargingPlugRecordGateway;

    public ChargingPlugStationRecord getChargingPlugRecordFromTimeRange(
            final LocalDateTime from, final LocalDateTime to) {
        return chargingPlugRecordGateway.getChargingPlugStationDataRecord(from, to);
    }

    public ChargingPlugStationRecord getChargingPlugRecordFromLastDay() {
        final LocalDateTime now = LocalDateTime.now();
        return chargingPlugRecordGateway.getChargingPlugStationDataRecord(now.minusDays(1), now);
    }
}
