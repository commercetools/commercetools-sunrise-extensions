package com.commercetools.sunrise.extensions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.sphere.sdk.client.BlockingSphereClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloWorldController {
    @Autowired
    BlockingSphereClient sphereClient;

    @GetMapping(value = "/hello", produces = "text/html")
    public ResponseEntity<String> greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        return new ResponseEntity<>("hi", HttpStatus.OK);
    }
}
