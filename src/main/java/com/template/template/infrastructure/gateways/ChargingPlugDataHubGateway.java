package com.template.template.infrastructure.gateways;

import com.template.template.domain.entity.*;
import com.template.template.domain.gateways.ChargingPlugRecordGateway;
import com.template.template.infrastructure.clients.opendatahubmobility.OpenDataHubMobilityClient;
import com.template.template.infrastructure.clients.opendatahubmobility.dto.OpenDataHubMobilityResponseDTO;
import com.template.template.infrastructure.clients.opendatahubmobility.dto.OpenDataHubMobilityResponseMeasurement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ChargingPlugDataHubGateway implements ChargingPlugRecordGateway {
    @Autowired
    private OpenDataHubMobilityClient openDataHubMobilityClient;

    @Override
    public ChargingPlugStationRecord getChargingPlugStationDataRecord(
            final LocalDateTime from, final LocalDateTime to) {
        try {
            final OpenDataHubMobilityResponseDTO response =
                    openDataHubMobilityClient.getReportFromTimeRange(from.toString(), to.toString());
            final var chargingPlugStationRecord = new ChargingPlugStationRecord();
            chargingPlugStationRecord.setTo(to);
            chargingPlugStationRecord.setFrom(from);
            chargingPlugStationRecord.setStations(mapResponseDtoToChargingPlugStationList(response));
            return chargingPlugStationRecord;
        } catch (Exception e) {
            log.warn("Failed to get data from interval {} - {}", from.toString(), to.toString(), e);
            return null;
        }
    }

    private List<ChargingPlugStation> mapResponseDtoToChargingPlugStationList(
            final OpenDataHubMobilityResponseDTO responseDto) {
        final var res = new ArrayList<ChargingPlugStation>();
        responseDto.getData().getChargingPlug().getStations().entrySet().forEach(
            entry -> {
                final List<ChargingPlugRecord> records = entry.getValue().getDataTypes().getStatus().getMeasurements()
                        .stream().map(this::mapMeasurementToChargingPlugRecord).toList();
                final ChargingPlug plug = new ChargingPlug(entry.getKey(), records);

                final String stationId = parseStationId(entry.getKey());
                boolean stationFound = false;
                for (final var station : res) {
                    if (Objects.equals(station.id(), stationId)) {
                        station.plugs().add(plug);
                        stationFound = true;
                        break;
                    }
                }

                if (!stationFound) {
                    final var position = new Coordinates(entry.getValue().getCoordinate().getX(),
                            entry.getValue().getCoordinate().getY());
                    res.add(new ChargingPlugStation(entry.getKey(), position, List.of(plug)));
                }
            }
        );
        return res;
    }

    private String parseStationId(final String input) {
        final int secondDashIndex = input.indexOf('-', input.indexOf('-') + 1);
        if (secondDashIndex != -1) {
            return input.substring(0, secondDashIndex);
        }
        return input;
    }

    private ChargingPlugRecord mapMeasurementToChargingPlugRecord(
            final OpenDataHubMobilityResponseMeasurement.Measurement measurement) {
        return new ChargingPlugRecord(measurement.getMoment(), measurement.getValue()
                ? ChargingPlugStatus.AVAILABLE : ChargingPlugStatus.UNAVAILABLE);
    }
}
