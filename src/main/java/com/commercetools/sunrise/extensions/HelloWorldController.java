package com.commercetools.sunrise.extensions;

import io.sphere.sdk.client.BlockingSphereClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {

    @Autowired
    private BlockingSphereClient sphereClient;

    @PostMapping(value = "/hello", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String greeting(@RequestBody ExtensionRequest request) {
        return "Hello " + request.getAction();
    }
}
