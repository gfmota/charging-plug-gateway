package com.template.template.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChargingPlugStationRecord implements Serializable {
    private List<ChargingPlugStation> stations;
    private LocalDateTime from;
    private LocalDateTime to;
}
