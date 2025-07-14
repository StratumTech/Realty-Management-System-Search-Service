package com.stratumtech.realtysearch.dto.mapper;

import java.util.*;
import java.time.Instant;
import java.sql.Timestamp;
import java.math.BigDecimal;

import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.stratumtech.realtysearch.model.PropertyDocument;
import com.stratumtech.realtysearch.dto.request.PropertyIndexRequest;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    @Mapping(source = "propertyId", target = "id")
    @Mapping(target = "createdAt", qualifiedByName = "mapInstant")
    PropertyDocument toDocument(PropertyIndexRequest property);

    @Mapping(source = "id", target = "propertyId")
    @Mapping(target = "createdAt", qualifiedByName = "mapTimestamp")
    PropertyIndexRequest toIndexRequest(PropertyDocument propertyDocument);

    PropertyIndexRequest toIndexRequest(Map<String, Object> property);

    @Named("mapInstant")
    default Instant toInstant(Timestamp createAt) {
        return createAt == null
                ? null
                : createAt.toInstant();
    }

    @Named("mapTimestamp")
    default Timestamp toTimestamp(Instant createAt) {
        return createAt == null
                ? null
                : Timestamp.from(createAt);
    }

    default String map(Object value){
        return value == null ? null : value.toString();
    }

    default UUID mapToUUID(Object value) {
        return value == null
                ? null
                : UUID.fromString(value.toString());
    }

    default BigDecimal mapToBigDecimal(Object value) {
        return value == null
                ? null
                : new BigDecimal(value.toString());
    }

    default Integer mapToInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number n) return n.intValue();
        return Integer.valueOf(value.toString());
    }

    default Double mapToDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Number n) return n.doubleValue();
        return Double.valueOf(value.toString());
    }

    default Timestamp mapToTimestamp(Object value) {
        if (value == null) return null;
        if (value instanceof Number n) {
            return new Timestamp(n.longValue());
        }
        return Timestamp.valueOf(value.toString());
    }

    @SuppressWarnings("unchecked")
    default Set<String> mapToFeatures(Object value) {
        return value == null
                ? null
                : new HashSet<>((Collection<String>) value);
    }
}