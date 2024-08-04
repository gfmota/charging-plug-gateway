package com.template.template.domain.gateways;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;

public interface ChargingPlugPublishGateway {
    void publishChargingPlugStationDailyReport(ChargingPlugStationRecord chargingPlugStationRecord);

    void publishChargingPlugStationHourlyReport(ChargingPlugStationRecord chargingPlugStationRecord);

    void publishChargingPlugStationCurrentStatus(ChargingPlugStationCurrentStatus chargingPlugStationCurrentStatus);
}
