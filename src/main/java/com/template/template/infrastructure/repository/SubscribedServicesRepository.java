package com.template.template.infrastructure.repository;

import com.template.template.domain.entity.ChargingPlugStationEventType;
import com.template.template.domain.entity.SubscribedService;
import com.template.template.domain.gateways.SubscribedServicesGateway;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class SubscribedServicesRepository implements SubscribedServicesGateway {
    private static final String FILE_PATH = "localStorage.csv";

    @Override
    public void subscribe(SubscribedService subscribedService) throws IOException {
        final FileWriter writer = new FileWriter(FILE_PATH, true);
        final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        csvPrinter.printRecord(subscribedService.getPath(), subscribedService.getUri(),
                subscribedService.getEventType().toString());
        csvPrinter.flush();
    }

    @Override
    public List<SubscribedService> getSubscribedServices(ChargingPlugStationEventType eventType) throws IOException {
        Reader reader = new FileReader(FILE_PATH);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        return csvParser.stream().filter(record -> record.get("EVENT_TYPE").equals(eventType.toString()))
                .map(this::recordToSubscribedService).toList();
    }

    private SubscribedService recordToSubscribedService(final CSVRecord record) {
        return new SubscribedService(record.get("PATH"), record.get("URI"),
                ChargingPlugStationEventType.valueOf(record.get("EVENT_TYPE")));
    }
}
