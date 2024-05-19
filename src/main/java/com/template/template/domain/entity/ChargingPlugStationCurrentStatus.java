package com.template.template.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChargingPlugStationCurrentStatus {
    private List<ChargingPlugStationStatus> stations;
    private LocalDateTime moment;
}
