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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                    openDataHubMobilityClient.getReportFromTimeRange(getStringFromLocalDateTime(from),
                            getStringFromLocalDateTime(to));
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

    @Override
    public ChargingPlugStationCurrentStatus getChargingPlugStationCurrentStatus() {
        try {
            final OpenDataHubMobilityResponseDTO response = openDataHubMobilityClient.getLatestReport();
            final var chargingPlugStationCurrentStatus = new ChargingPlugStationCurrentStatus();
            chargingPlugStationCurrentStatus.setMoment(LocalDateTime.now());
            chargingPlugStationCurrentStatus.setStations(
                    mapResponseDtoToChargingPlugStationCurrentStatusList(response));
            return chargingPlugStationCurrentStatus;
        } catch (Exception e) {
            log.warn("Failed to get latest data", e);
            return null;
        }
    }

    private List<ChargingPlugStationStatus> mapResponseDtoToChargingPlugStationCurrentStatusList(
            final OpenDataHubMobilityResponseDTO responseDto) {
        final var res = new ArrayList<ChargingPlugStationStatus>();
        responseDto.getData().getChargingPlug().getStations().entrySet().forEach(
                entry -> {
                    final String stationId = parseStationId(entry.getKey());
                    boolean stationFound = false;
                    for (final var station : res) {
                        if (Objects.equals(station.id(), stationId)) {
                            station.plugs().put(entry.getKey(), entry.getValue().getDataTypes().getStatus()
                                    .getMeasurements().get(0).getValue()
                                    ? ChargingPlugStatus.AVAILABLE : ChargingPlugStatus.UNAVAILABLE);
                            stationFound = true;
                            break;
                        }
                    }

                    if (!stationFound) {
                        final var position = new Coordinates(entry.getValue().getCoordinate().getX(),
                                entry.getValue().getCoordinate().getY());
                        final var plugs = new HashMap<String, ChargingPlugStatus>();
                        plugs.put(entry.getKey(),
                                entry.getValue().getDataTypes().getStatus().getMeasurements().get(0).getValue()
                                        ? ChargingPlugStatus.AVAILABLE : ChargingPlugStatus.UNAVAILABLE);
                        res.add(new ChargingPlugStationStatus(stationId, position, plugs));
                    }
                }
        );
        return res;
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
                    res.add(new ChargingPlugStation(stationId, position, List.of(plug)));
                }
            }
        );
        return res;
    }

    private String parseStationId(final String input) {
        final int firstDashIndex = input.indexOf('-');
        final int secondDashIndex = input.indexOf('-', firstDashIndex + 1);
        if (secondDashIndex != -1) {
            return input.substring(0, secondDashIndex);
        }
        if (firstDashIndex != -1) {
            return input.substring(0, firstDashIndex);
        }
        return input;
    }

    private ChargingPlugRecord mapMeasurementToChargingPlugRecord(
            final OpenDataHubMobilityResponseMeasurement.Measurement measurement) {
        return new ChargingPlugRecord(getLocalDateTimeFromString(measurement.getMoment()), measurement.getValue()
                ? ChargingPlugStatus.AVAILABLE : ChargingPlugStatus.UNAVAILABLE);
    }

    private LocalDateTime getLocalDateTimeFromString(final String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZ");
        try {
            return LocalDateTime.parse(input, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    private String getStringFromLocalDateTime(final LocalDateTime input) {
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        ZonedDateTime zonedDateTimeInput = ZonedDateTime.of(input, zoneId);
        return zonedDateTimeInput.format(formatter);
    }
}
