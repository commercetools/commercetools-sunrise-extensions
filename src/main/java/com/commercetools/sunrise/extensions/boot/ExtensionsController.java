package com.commercetools.sunrise.extensions.boot;

import com.commercetools.sunrise.extensions.SetCustomerEmailCartExtension;
import com.commercetools.sunrise.extensions.models.ExtensionRequest;
import com.commercetools.sunrise.extensions.models.ExtensionResponse;
import io.sphere.sdk.carts.Cart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CompletionStage;

@Slf4j
@Controller
public class ExtensionsController {

    public static final String ENDPOINT = "/carts/set-customer-email";

    @Autowired
    private SetCustomerEmailCartExtension setCustomerEmailCartExtension;

    @PostMapping(value = ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CompletionStage<ExtensionResponse<Cart>> carts(@RequestBody ExtensionRequest<Cart> request) {
        return setCustomerEmailCartExtension.apply(request);
    }
}
