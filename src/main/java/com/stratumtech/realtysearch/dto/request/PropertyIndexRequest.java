package com.stratumtech.realtysearch.dto.request;

import java.util.Set;
import java.util.UUID;
import java.sql.Timestamp;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class PropertyIndexRequest {
    private final UUID propertyId;
    private final UUID agentUuid;
    private final String title;
    private final String type;
    private final String dealType;
    private final BigDecimal price;
    private final Integer rooms;
    private final Double area;
    private final Set<String> features;
    private final String address;
    private final String layout;
    private final Timestamp createdAt;
}