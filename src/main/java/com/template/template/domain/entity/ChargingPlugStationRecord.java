package com.template.template.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChargingPlugStationRecord {
    private List<ChargingPlugStation> stations;
    private LocalDateTime from;
    private LocalDateTime to;
}
