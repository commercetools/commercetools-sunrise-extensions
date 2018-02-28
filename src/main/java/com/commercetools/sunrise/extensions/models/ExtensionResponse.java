package com.commercetools.sunrise.extensions.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.sphere.sdk.models.Resource;

public interface ExtensionResponse<T extends Resource<T>> {
    @JsonIgnore
    boolean successful();
}
