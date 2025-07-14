package com.stratumtech.realtysearch.service;

import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import com.stratumtech.realtysearch.model.PropertyDocument;
import com.stratumtech.realtysearch.dto.request.PropertyIndexRequest;

public interface PropertySearchService {

    void index(PropertyIndexRequest request);

    void update(UUID uuid, Map<String, Object> changes);

    void invalidate(UUID uuid);

    Page<PropertyDocument> search(
            UUID agentUuid,
            String title,
            String type,
            String dealType,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minRooms,
            Integer maxRooms,
            Double area,
            List<String> features,
            String layout,
            int page,
            int size
    );

}
