package com.commercetools.sunrise.extensions.lambda;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.commercetools.sunrise.extensions.models.Extension;
import com.commercetools.sunrise.extensions.models.ExtensionRequest;
import com.commercetools.sunrise.extensions.models.ExtensionResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.client.SphereClientUtils;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.models.Resource;

import java.util.Collections;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public abstract class CtpExtensionAwsLambdaRequestHandler<T extends Resource<T>> implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private final Extension<T> extension;
    private final TypeReference<ExtensionRequest<T>> typeReference;

    public CtpExtensionAwsLambdaRequestHandler(Extension<T> extension, TypeReference<ExtensionRequest<T>> typeReference) {
        this.extension = extension;
        this.typeReference = typeReference;
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        final ExtensionRequest<T> request = SphereJsonUtils.readObject(input.getBody(), typeReference);
        final CompletionStage<ExtensionResponse<T>> resultStage = extension.apply(request);
        final ExtensionResponse<T> result = SphereClientUtils.blockingWait(resultStage, 2, TimeUnit.SECONDS);
        return transform(result);
    }

    private AwsProxyResponse transform(final ExtensionResponse<T> result) {
        final int statusCode = result.successful() ? 200 : 400;
        final String body = SphereJsonUtils.toJsonString(result);
        return new AwsProxyResponse(statusCode, Collections.emptyMap(), body);
    }
}
