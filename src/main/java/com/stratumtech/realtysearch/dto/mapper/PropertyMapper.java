package com.stratumtech.realtysearch.dto.mapper;

import java.time.Instant;
import java.sql.Timestamp;

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
}