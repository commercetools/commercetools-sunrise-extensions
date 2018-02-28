package com.commercetools.sunrise.extensions.models;

import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Resource;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class NoOpExtensionResponse<T extends Resource<T>> implements ExtensionResponse<T> {
    private final List<UpdateAction<T>> actions = Collections.emptyList();

    @Override
    public boolean successful() {
        return true;
    }
}
