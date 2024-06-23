package com.template.template.domain.gateways;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;

public interface ChargingPlugNotificationGateway {
    void notifyChargingPlugStationCurrentStatus(String path, String uri, ChargingPlugStationCurrentStatus notification);
    void notifyChargingPlugStationDailyReport(String path, String uri, ChargingPlugStationRecord notification);
}
