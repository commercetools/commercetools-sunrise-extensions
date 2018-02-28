package com.commercetools.sunrise.extensions.models;

import io.sphere.sdk.models.Reference;
import io.sphere.sdk.models.Resource;
import lombok.Data;

@Data
public class ExtensionRequest<T extends Resource<T>> {
    private String action;
    private Reference<T> resource;

    public T getResourceObj() {
        if (getResource() != null && getResource().getObj() != null) {
            return getResource().getObj();
        } else {
            throw new IllegalArgumentException("Field 'obj' is not expanded.");
        }
    }
}
