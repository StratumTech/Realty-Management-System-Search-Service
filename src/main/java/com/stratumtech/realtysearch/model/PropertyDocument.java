package com.stratumtech.realtysearch.model;

import java.util.UUID;
import java.util.List;
import java.time.Instant;
import java.math.BigDecimal;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "properties")
public class PropertyDocument {

    @Id
    private UUID id;

    @Field(type = FieldType.Keyword)
    private UUID agentUuid;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Keyword)
    private String dealType;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Field(type = FieldType.Integer)
    private Integer rooms;

    @Field(type = FieldType.Double)
    private Double area;

    @Field(type = FieldType.Keyword)
    private List<String> features;

    @Field(type = FieldType.Text)
    private String address;

    @Field(type = FieldType.Text)
    private String layout;

    @Field(type = FieldType.Date)
    private Instant createdAt;
}
