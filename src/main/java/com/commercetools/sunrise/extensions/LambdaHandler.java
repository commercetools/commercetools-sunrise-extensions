package com.commercetools.sunrise.extensions;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        return handler.proxy(input, context);
    }
}