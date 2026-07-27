package com.bank.recommendationService.controller;

import com.bank.recommendationService.dto.ServiceInfoResponse;
import com.bank.recommendationService.service.ManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final ManagementService managementService;

    public ManagementController(
            ManagementService managementService
    ) {
        this.managementService =
                managementService;
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        managementService.clearCaches();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info")
    public ResponseEntity<ServiceInfoResponse> getServiceInfo() {
        ServiceInfoResponse response =
                managementService.getServiceInfo();

        return ResponseEntity.ok(response);
    }
}