package com.stratumtech.realtysearch.service;

import java.util.List;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import co.elastic.clients.json.JsonData;
import co.elastic.clients.elasticsearch._types.FieldValue;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stratumtech.realtysearch.exception.PropertyNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitSupport;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import com.stratumtech.realtysearch.model.PropertyDocument;
import com.stratumtech.realtysearch.dto.mapper.PropertyMapper;
import com.stratumtech.realtysearch.repository.PropertyESRepository;
import com.stratumtech.realtysearch.dto.request.PropertyIndexRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPropertySearchServiceImpl implements PropertySearchService {

    private final PropertyESRepository esRepository;
    private final ElasticsearchOperations operations;
    private final PropertyMapper propertyMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void index(PropertyIndexRequest request) {
        PropertyDocument document = propertyMapper.toDocument(request);
        esRepository.save(document);
    }

    @Override
    public void update(UUID uuid, Map<String, Object> changes) {
        PropertyDocument doc = esRepository.findById(uuid)
                .orElseThrow(() -> new PropertyNotFoundException(uuid));

        try {
            objectMapper.updateValue(doc, changes);
        } catch (JsonMappingException e) {
            log.error("Error while updating property", e);
            throw new RuntimeException(e);
        }

        esRepository.save(doc);
    }

    @Override
    public void invalidate(UUID uuid) {
        esRepository.deleteById(uuid);
    }

    @Override
    public Page<PropertyDocument> search(
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
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> {
                    if (agentUuid != null) {
                        b.filter(f -> f.term(t -> t.field("agentUuid").value(agentUuid.toString())));
                    }
                    if (title != null) {
                        b.filter(f -> f.match(m -> m.field("title").query(title)));
                    }
                    if (type != null) {
                        b.filter(f -> f.term(t -> t.field("type").value(type)));
                    }
                    if (dealType != null) {
                        b.filter(f -> f.term(t -> t.field("dealType").value(dealType)));
                    }
                    if (minPrice != null || maxPrice != null) {
                        b.filter(f -> f.range(r -> r.untyped(urq -> {
                            urq.field("price");
                            if (minPrice != null) urq.gte(JsonData.of(minPrice));
                            if (maxPrice != null) urq.lte(JsonData.of(maxPrice));
                            return urq;
                        })));
                    }
                    if (minRooms != null || maxRooms != null) {
                        b.filter(f -> f.range(r -> r.untyped(urq -> {
                            urq.field("rooms");
                            if (minRooms != null) urq.gte(JsonData.of(minRooms));
                            if (maxRooms != null) urq.lte(JsonData.of(maxRooms));
                            return urq;
                        })));
                    }
                    if (area != null) {
                        b.filter(f -> f.range(r -> r.untyped(urq -> {
                            urq.field("area").gte(JsonData.of(area));
                            return urq;
                        })));
                    }
                    if (features != null && !features.isEmpty()) {
                        List<FieldValue> vals = features.stream().map(FieldValue::of).collect(Collectors.toList());
                        b.filter(f -> f.terms(t -> t.field("features").terms(v -> v.value(vals))));
                    }
                    if (layout != null) {
                        b.filter(f -> f.match(m -> m.field("layout").query(layout)));
                    }
                    return b;
                }))
                .withPageable(pageable)
                .build();
        SearchHits<PropertyDocument> hits = operations.search(query, PropertyDocument.class);
        return SearchHitSupport.searchPageFor(hits, pageable)
                .map(SearchHit::getContent);
    }
}
