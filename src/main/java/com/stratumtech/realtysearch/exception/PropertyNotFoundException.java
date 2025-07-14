package com.stratumtech.realtysearch.exception;

import java.util.UUID;

public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException(UUID propertyUuid) {
        super(String.format("Property not found: %s", propertyUuid));
    }

    public PropertyNotFoundException(UUID propertyUuid, Throwable cause) {
        super(String.format("Property not found: %s", propertyUuid), cause);
    }

    public PropertyNotFoundException(Throwable cause) {
        super(cause);
    }
}
