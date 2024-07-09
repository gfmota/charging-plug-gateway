package com.template.template.domain.usecases;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.gateways.ChargingPlugRecordGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChargingPlugRecordUsecase {
    @Autowired
    private ChargingPlugRecordGateway chargingPlugRecordGateway;

    public Optional<ChargingPlugStationRecord> getChargingPlugRecordFromTimeRange(
            final LocalDateTime from, final LocalDateTime to) {
        return chargingPlugRecordGateway.getChargingPlugStationDataRecord(from, to);
    }

    public Optional<ChargingPlugStationRecord> getChargingPlugRecordFromLastDay() {
        final LocalDateTime today = LocalDate.now().atStartOfDay();
        return chargingPlugRecordGateway.getChargingPlugStationDataRecord(today.minusDays(1), today);
    }

    public Optional<ChargingPlugStationRecord> getChargingPlugRecordFromLastHour() {
        final LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        return chargingPlugRecordGateway.getChargingPlugStationDataRecord(now.minusHours(1), now);
    }

    public Optional<ChargingPlugStationCurrentStatus> getChargingPlugCurrentStatus() {
        return chargingPlugRecordGateway.getChargingPlugStationCurrentStatus();
    }
}
