package com.commercetools.sunrise.extensions;

import io.sphere.sdk.client.BlockingSphereClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {

    @Autowired
    private BlockingSphereClient sphereClient;

    @GetMapping(value = "/hello")
    @ResponseBody
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        return "Hello World!";
    }
}
