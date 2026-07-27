package com.bank.recommendationService.dto;

public class ServiceInfoResponse {

    private final String name;
    private final String version;

    public ServiceInfoResponse(
            String name,
            String version
    ) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
