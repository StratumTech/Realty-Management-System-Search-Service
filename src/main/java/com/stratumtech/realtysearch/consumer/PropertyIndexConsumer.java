package com.stratumtech.realtysearch.consumer;

import java.util.Map;
import java.util.UUID;

import ch.qos.logback.core.util.StringUtil;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import com.stratumtech.realtysearch.dto.mapper.PropertyMapper;
import com.stratumtech.realtysearch.service.PropertySearchService;
import com.stratumtech.realtysearch.dto.request.PropertyIndexRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class PropertyIndexConsumer {

    private final PropertyMapper propertyMapper;
    private final PropertySearchService propertySearchService;

    @Builder
    public record KafkaRequest(
            String operation,
            UUID entityId,
            Object data
    ) {
    }

    @KafkaListener(
            groupId = "consumer-group-1",
            topics = "${kafka.topic.indexing-property-topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void handle(@Payload KafkaRequest message) {
        readDispatch(message);
    }

    @KafkaListener(
            groupId = "consumer-group-2",
            topics = "${kafka.topic.indexing-property-topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void handle2(@Payload KafkaRequest message) {
        readDispatch(message);
    }

    public void readDispatch(KafkaRequest message) {
        log.debug(
                "Received message with operation '{}'. Routing to handler: readPropery{}",
                message.operation(),
                StringUtil.capitalizeFirstLetter(message.operation())
        );
        String operation = message.operation();
        switch (operation) {
            case "index" -> readPropertyIndex(message.data());
            case "update" -> readPropertyUpdate(message.entityId(), message.data());
            case "delete" -> readPropertyDelete(message.entityId());
        }
    }

    @SuppressWarnings("unchecked")
    public void readPropertyIndex(Object obj) {
        PropertyIndexRequest request = propertyMapper.toIndexRequest((Map<String, Object>) obj);
        propertySearchService.index(request);
        log.info("Indexed property {}", request.getPropertyId());
    }

    @SuppressWarnings("unchecked")
    public void readPropertyUpdate(UUID uuid, Object obj) {
        final var data = (Map<String, Object>) obj;
        final var changes = (Map<String, Object>) data.getOrDefault("changes", Map.of());
        propertySearchService.update(uuid, changes);
        log.info("Updated property {}", uuid);
    }

    public void readPropertyDelete(UUID propertyUuid) {
        propertySearchService.invalidate(propertyUuid);
        log.debug("Invalidate property {}", propertyUuid);
    }
}
