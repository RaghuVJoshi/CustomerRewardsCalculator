package com.raghuvjoshi.customerrewardsservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Slf4j
public class ServiceHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    public ServiceHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        try {
            log.debug(" Executing sample query on DB for health check");
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Health.up().build();
        } catch (Exception ex) {
            return Health.down().build();
        }
    }
}

