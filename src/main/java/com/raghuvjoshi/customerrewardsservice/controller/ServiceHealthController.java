package com.raghuvjoshi.customerrewardsservice.controller;

import com.raghuvjoshi.customerrewardsservice.service.ServiceHealthIndicator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to handle application health check requests
 */
@RestController
@RequestMapping("/api/rewards")
@Slf4j
public class ServiceHealthController {
    @Autowired
    private ServiceHealthIndicator healthIndicator;

    /**
     * Respond with application health for any requests by monitoring tool
     * @return - Response Entity specifying if App is running or down.
     */
    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
            Health health = healthIndicator.health();
            if (health.getStatus().getCode().equals("UP")) {
                log.info("Health check successful. Service is running");
                return ResponseEntity.ok("Service is running");
            } else {
                log.error("Health check unsuccessful. Service is down");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is down");
            }

    }
}
