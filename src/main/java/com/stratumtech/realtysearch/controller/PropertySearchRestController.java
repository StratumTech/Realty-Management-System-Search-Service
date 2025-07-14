package com.stratumtech.realtysearch.controller;

import java.util.UUID;
import java.util.List;
import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stratumtech.realtysearch.model.PropertyDocument;
import com.stratumtech.realtysearch.service.PropertySearchService;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class PropertySearchRestController {

    private final PropertySearchService searchService;

    public PropertySearchRestController(PropertySearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(value = "/properties", method = RequestMethod.GET)
    public Page<PropertyDocument> search(
            @RequestParam(required = false) UUID agentId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String dealType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minRooms,
            @RequestParam(required = false) Integer maxRooms,
            @RequestParam(required = false) Double area,
            @RequestParam(required = false) List<String> features,
            @RequestParam(required = false) String layout,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return searchService.search(
                agentId, title,
                type, dealType, minPrice, maxPrice,
                minRooms, maxRooms, area,
                features, layout,
                page, size
        );
    }

}
