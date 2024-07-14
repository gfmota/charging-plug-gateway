package com.template.template.infrastructure.producers;

import com.template.template.domain.entity.ChargingPlugStationCurrentStatus;
import com.template.template.domain.entity.ChargingPlugStationRecord;
import com.template.template.domain.gateways.ChargingPlugPublishGateway;
import com.template.template.infrastructure.clients.opendatahubmobility.OpenDataHubMobilityClient;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChargingPlugStationMessageProducer implements ChargingPlugPublishGateway {
    private static final String CHARGING_PLUG_STATION_TOPIC = "charging-plug-station-topic";
    private static final String CHARGING_PLUG_STATION_DAILY_REPORT_ROUTINE_KEY = "charging-plug-station-daily-report";
    private static final String CHARGING_PLUG_STATION_CURRENT_STATUS_ROUTINE_KEY =
            "charging-plug-station-current-status";

    private MeterRegistry meterRegistry;
    private Counter dailyReportMessagePublishedCounter;
    private Counter currentStatusMessagePublishedCounter;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public ChargingPlugStationMessageProducer(MeterRegistry meterRegistry, RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.meterRegistry = meterRegistry;

        dailyReportMessagePublishedCounter = Counter.builder("daily_report_message_published_counter")
                .description("Number of messages sent for daily report")
                .register(meterRegistry);

        currentStatusMessagePublishedCounter = Counter.builder("current_status_message_published_counter")
                .description("Number of messages sent for current status")
                .register(meterRegistry);
    }

    @Override
    public void publishChargingPlugStationDailyReport(final ChargingPlugStationRecord chargingPlugStationRecord) {
        dailyReportMessagePublishedCounter.increment();
        rabbitTemplate.convertAndSend(CHARGING_PLUG_STATION_TOPIC,
                CHARGING_PLUG_STATION_DAILY_REPORT_ROUTINE_KEY, chargingPlugStationRecord);
    }

    @Override
    public void publishChargingPlugStationCurrentStatus(
            final ChargingPlugStationCurrentStatus chargingPlugStationCurrentStatus) {
        currentStatusMessagePublishedCounter.increment();
        rabbitTemplate.convertAndSend(CHARGING_PLUG_STATION_TOPIC,
                CHARGING_PLUG_STATION_CURRENT_STATUS_ROUTINE_KEY, chargingPlugStationCurrentStatus);
    }
}
