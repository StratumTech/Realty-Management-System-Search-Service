package com.stratumtech.realtysearch.repository;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.stratumtech.realtysearch.model.PropertyDocument;

public interface PropertyESRepository extends ElasticsearchRepository<PropertyDocument, UUID> {

}
