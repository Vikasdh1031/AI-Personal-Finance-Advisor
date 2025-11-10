package com.vikas.finadvisor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Map<String, Object> checkHealth() {
        return Map.of(
                "status", "OK",
                "app", "AI Personal Finance Advisor",
                "version", "1.0.0"
        );
    }
}
