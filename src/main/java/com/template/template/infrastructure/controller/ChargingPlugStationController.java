package com.template.template.infrastructure.controller;

import com.template.template.domain.entity.SubscribedService;
import com.template.template.domain.usecases.SubscriberUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class ChargingPlugStationController {
    @Autowired
    private SubscriberUseCase subscriberUseCase;

    @PostMapping("/subscribe")
    private ResponseEntity<?> subscribe(final @RequestBody SubscribedService subscribedService) {
        try {
            subscriberUseCase.subscribe(subscribedService);
            return ResponseEntity.ok().build();
        } catch (final Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
