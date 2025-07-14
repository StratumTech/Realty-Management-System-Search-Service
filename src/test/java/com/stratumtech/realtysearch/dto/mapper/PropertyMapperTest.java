package com.stratumtech.realtysearch.dto.mapper;

import java.util.Set;
import java.util.List;
import java.util.UUID;
import java.time.Instant;
import java.sql.Timestamp;
import java.math.BigDecimal;

import org.mapstruct.factory.Mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;

import com.stratumtech.realtysearch.model.PropertyDocument;
import com.stratumtech.realtysearch.dto.request.PropertyIndexRequest;

class PropertyMapperTest {

    private PropertyMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PropertyMapper.class);
    }

    @Test
    void toDocument_shouldMapIndexRequestToDocument() {
        UUID propertyId = UUID.randomUUID();
        String type = "flat";
        String dealType = "sale";
        BigDecimal price = BigDecimal.valueOf(350_000);
        Integer rooms = 3;
        Set<String> features = Set.of("Pool", "Garden");
        Timestamp now = Timestamp.from(Instant.now());

        PropertyIndexRequest request = PropertyIndexRequest.builder()
                .propertyId(propertyId)
                .agentUuid(UUID.randomUUID())
                .title("Test Title")
                .type(type)
                .dealType(dealType)
                .price(price)
                .rooms(rooms)
                .area(75.0)
                .features(features)
                .address("Some Address")
                .layout("Some Layout")
                .createdAt(now)
                .build();

        PropertyDocument doc = mapper.toDocument(request);

        assertThat(doc).isNotNull();
        assertThat(doc.getId()).isEqualTo(propertyId);
        assertThat(doc.getType()).isEqualTo(type);
        assertThat(doc.getDealType()).isEqualTo(dealType);
        assertThat(doc.getPrice()).isEqualByComparingTo(price);
        assertThat(doc.getRooms()).isEqualTo(rooms);
        assertThat(doc.getFeatures())
                .containsExactlyInAnyOrderElementsOf(features);
        assertThat(doc.getCreatedAt()).isEqualTo(now.toInstant());
    }

    @Test
    void toDocument_shouldHandleNullCreatedAt() {
        PropertyIndexRequest request = PropertyIndexRequest.builder()
                .propertyId(UUID.randomUUID())
                .agentUuid(UUID.randomUUID())
                .title("Title")
                .type("house")
                .dealType("rent")
                .price(BigDecimal.valueOf(200_000))
                .rooms(2)
                .area(60.0)
                .features(Set.of())
                .address("Addr")
                .layout("Layout")
                .createdAt(null)
                .build();

        PropertyDocument doc = mapper.toDocument(request);

        assertThat(doc).isNotNull();
        assertThat(doc.getCreatedAt()).isNull();
    }

    @Test
    void toDocument_withNullFeatures_shouldProduceNullFeatures() {
        PropertyIndexRequest request = PropertyIndexRequest.builder()
                .propertyId(UUID.randomUUID())
                .agentUuid(UUID.randomUUID())
                .title("Title")
                .type("flat")
                .dealType("sale")
                .price(BigDecimal.valueOf(100_000))
                .rooms(1)
                .area(30.0)
                .features(null)
                .address("Addr")
                .layout("Layout")
                .createdAt(Timestamp.from(Instant.now()))
                .build();

        PropertyDocument doc = mapper.toDocument(request);

        assertThat(doc).isNotNull();
        assertThat(doc.getFeatures()).isNull();
    }

    @Test
    void toIndexRequest_shouldMapDocumentToIndexRequest() {
        UUID id = UUID.randomUUID();
        String type = "house";
        String dealType = "rent";
        BigDecimal price = BigDecimal.valueOf(150_000);
        Integer rooms = 4;
        List<String> features = List.of("Parking", "Balcony");
        Instant now = Instant.now();

        PropertyDocument doc = PropertyDocument.builder()
                .id(id)
                .type(type)
                .dealType(dealType)
                .price(price)
                .rooms(rooms)
                .features(features)
                .createdAt(now)
                .build();

        PropertyIndexRequest request = mapper.toIndexRequest(doc);

        assertThat(request).isNotNull();
        assertThat(request.getPropertyId()).isEqualTo(id);
        assertThat(request.getType()).isEqualTo(type);
        assertThat(request.getDealType()).isEqualTo(dealType);
        assertThat(request.getPrice()).isEqualByComparingTo(price);
        assertThat(request.getRooms()).isEqualTo(rooms);
        assertThat(request.getFeatures())
                .containsExactlyInAnyOrderElementsOf(features);
        assertThat(request.getCreatedAt()).isEqualTo(Timestamp.from(now));
    }

    @Test
    void toIndexRequest_shouldHandleNullCreatedAt() {
        PropertyDocument doc = PropertyDocument.builder()
                .id(UUID.randomUUID())
                .type("flat")
                .dealType("sale")
                .price(BigDecimal.valueOf(50_000))
                .rooms(1)
                .features(List.of())
                .createdAt(null)
                .build();

        PropertyIndexRequest request = mapper.toIndexRequest(doc);

        assertThat(request).isNotNull();
        assertThat(request.getCreatedAt()).isNull();
    }

    @Test
    void toIndexRequest_withNullFeatures_shouldProduceNullFeatures() {
        PropertyDocument doc = PropertyDocument.builder()
                .id(UUID.randomUUID())
                .type("bungalow")
                .dealType("sale")
                .price(BigDecimal.valueOf(250_000))
                .rooms(2)
                .features(null)
                .createdAt(Instant.now())
                .build();

        PropertyIndexRequest request = mapper.toIndexRequest(doc);

        assertThat(request).isNotNull();
        assertThat(request.getFeatures()).isNull();
    }
}
