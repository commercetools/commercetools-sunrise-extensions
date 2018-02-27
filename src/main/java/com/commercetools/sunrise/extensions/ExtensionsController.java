package com.commercetools.sunrise.extensions;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.customers.queries.CustomerByIdGet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Controller
@Slf4j
public class ExtensionsController {

    @Autowired
    private BlockingSphereClient sphereClient;

    @PostMapping(value = "/carts", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CompletionStage<ExtensionResponse<Cart>> carts(@RequestBody ExtensionRequest<Cart> request) {
        log.info("Starting cart extension");
        final Cart cart = request.getResourceObj();
        final boolean needsCustomerEmail = cart.getCustomerEmail() == null && cart.getCustomerId() != null;
        if (needsCustomerEmail) {
            log.info("Setting customer email to cart...");
            return setCustomerEmail(cart.getCustomerId());
        }
        log.info("Nothing to do");
        return completedFuture(new NoOpExtensionResponse<>());
    }

    private CompletionStage<ExtensionResponse<Cart>> setCustomerEmail(final String customerId) {
        final CompletionStage<ExtensionResponse<Cart>> response = sphereClient.execute(CustomerByIdGet.of(customerId))
                .thenApply(customer -> new UpdatesExtensionResponse<>(SetCustomerEmail.of(customer.getEmail())));
        response.thenAccept(customer -> log.info("Customer email set successfully"));
        return response;
    }
}
