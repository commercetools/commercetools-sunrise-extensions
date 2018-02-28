package com.commercetools.sunrise.extensions.models;

import io.sphere.sdk.models.Resource;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface Extension<T extends Resource<T>> extends Function<ExtensionRequest<T>, CompletionStage<ExtensionResponse<T>>> {
}
