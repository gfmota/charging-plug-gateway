package com.template.template.domain.gateways;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ChargingPlugRecordGateway {
    Optional<ChargingPlugStationRecord> getChargingPlugStationDataRecord(LocalDateTime from, LocalDateTime to);

    Optional<ChargingPlugStationCurrentStatus> getChargingPlugStationCurrentStatus();
}
