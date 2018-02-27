package com.commercetools.sunrise.extensions;

import io.sphere.sdk.models.Reference;
import io.sphere.sdk.models.Resource;
import lombok.Data;


@Data
public class ExtensionRequest<T extends Resource<T>> {
    private String action;
    private Reference<T> resource;
}
