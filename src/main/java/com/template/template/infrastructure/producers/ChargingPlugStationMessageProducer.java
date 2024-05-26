package com.template.template.infrastructure.producers;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.gateways.ChargingPlugPublishGateway;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChargingPlugStationMessageProducer implements ChargingPlugPublishGateway {
    private static final String CHARGING_PLUG_STATION_TOPIC = "charging-plug-station-topic";
    private static final String CHARGING_PLUG_STATION_DAILY_REPORT_ROUTINE_KEY = "charging-plug-station-daily-report";
    private static final String CHARGING_PLUG_STATION_CURRENT_STATUS_ROUTINE_KEY =
            "charging-plug-station-current-status";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void publishChargingPlugStationDailyReport(final ChargingPlugStationRecord chargingPlugStationRecord) {
        rabbitTemplate.convertAndSend(CHARGING_PLUG_STATION_TOPIC,
                CHARGING_PLUG_STATION_DAILY_REPORT_ROUTINE_KEY, chargingPlugStationRecord);
    }

    @Override
    public void publishChargingPlugStationCurrentStatus(
            final ChargingPlugStationCurrentStatus chargingPlugStationCurrentStatus) {
        rabbitTemplate.convertAndSend(CHARGING_PLUG_STATION_TOPIC,
                CHARGING_PLUG_STATION_CURRENT_STATUS_ROUTINE_KEY, chargingPlugStationCurrentStatus);
    }
}
