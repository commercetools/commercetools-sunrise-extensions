package com.commercetools.sunrise.extensions;

import com.commercetools.sunrise.extensions.models.*;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.queries.CustomerByIdGet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
public class SetCustomerEmailCartExtension implements Extension<Cart> {

    private final SphereClient sphereClient;

    public SetCustomerEmailCartExtension(SphereClient sphereClient) {
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<ExtensionResponse<Cart>> apply(@RequestBody ExtensionRequest<Cart> request) {
        log.info("Starting cart extension");
        final Cart cart = request.getResourceObj();
        if (needsCustomerEmail(cart)) {
            log.info("Setting customer email to cart...");
            return setCustomerEmail(cart.getCustomerId());
        }
        log.info("Nothing to do");
        return completedFuture(new NoOpExtensionResponse<>());
    }

    private boolean needsCustomerEmail(Cart cart) {
        return cart.getCustomerEmail() == null && cart.getCustomerId() != null;
    }

    private CompletionStage<ExtensionResponse<Cart>> setCustomerEmail(final String customerId) {
        final CompletionStage<ExtensionResponse<Cart>> response = sphereClient.execute(CustomerByIdGet.of(customerId))
                .thenApply(customer -> new UpdatesExtensionResponse<>(SetCustomerEmail.of(customer.getEmail())));
        response.thenAccept(customer -> log.info("Customer email set successfully"));
        return response;
    }
}
