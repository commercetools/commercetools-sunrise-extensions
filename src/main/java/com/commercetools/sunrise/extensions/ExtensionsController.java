package com.commercetools.sunrise.extensions;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.customers.queries.CustomerByIdGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Controller
public class ExtensionsController {

    @Autowired
    private BlockingSphereClient sphereClient;

    @PostMapping(value = "/carts", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CompletionStage<ExtensionResponse<Cart>> carts(@RequestBody ExtensionRequest<Cart> request) {
        final Cart cart = extractCart(request);
        final boolean needsCustomerEmail = cart.getCustomerEmail() == null && cart.getCustomerId() != null;
        if (needsCustomerEmail) {
            return sphereClient.execute(CustomerByIdGet.of(cart.getCustomerId()))
                    .thenApply(customer -> new UpdatesExtensionResponse<>(SetCustomerEmail.of(customer.getEmail())));
        }
        return completedFuture(new NoOpExtensionResponse<>());
    }

    private Cart extractCart(final ExtensionRequest<Cart> request) {
        if (request.getResource() != null && request.getResource().getObj() != null) {
            return request.getResource().getObj();
        } else {
            throw new IllegalArgumentException("Wrong expected input from CTP");
        }
    }
}
