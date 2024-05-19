package com.template.template.domain.gateways;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;

import java.time.LocalDateTime;

public interface ChargingPlugRecordGateway {
    ChargingPlugStationRecord getChargingPlugStationDataRecord(LocalDateTime from, LocalDateTime to);

    ChargingPlugStationCurrentStatus getChargingPlugStationCurrentStatus();
}
