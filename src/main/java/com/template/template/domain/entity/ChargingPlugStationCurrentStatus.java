package com.template.template.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChargingPlugStationCurrentStatus implements Serializable {
    private List<ChargingPlugStationStatus> stations;
    private LocalDateTime moment;
}
