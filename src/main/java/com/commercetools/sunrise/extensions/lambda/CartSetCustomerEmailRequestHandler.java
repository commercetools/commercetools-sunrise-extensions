package com.commercetools.sunrise.extensions.lambda;

import com.commercetools.sunrise.extensions.SetCustomerEmailCartExtension;
import com.commercetools.sunrise.extensions.models.Extension;
import com.commercetools.sunrise.extensions.models.ExtensionRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.client.SphereClientFactory;

public class CartSetCustomerEmailRequestHandler extends CtpExtensionAwsLambdaRequestHandler<Cart> {

    private static final TypeReference<ExtensionRequest<Cart>> TYPE_REFERENCE = new TypeReference<ExtensionRequest<Cart>>() {
    };

    public CartSetCustomerEmailRequestHandler() {
        super(createExtension(), TYPE_REFERENCE);
    }

    private static Extension<Cart> createExtension() {
        final SphereClientConfig config = SphereClientConfig.ofEnvironmentVariables("CTP");
        final SphereClient sphereClient = SphereClientFactory.of().createClient(config);
        return new SetCustomerEmailCartExtension(sphereClient);
    }
}
