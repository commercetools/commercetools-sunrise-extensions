package com.commercetools.sunrise.extensions.boot;

import com.commercetools.sunrise.extensions.SetCustomerEmailCartExtension;
import io.sphere.sdk.client.SphereClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.commercetools.sunrise.extensions")
public class DefaultWiring {
    @Bean
    public SetCustomerEmailCartExtension setCustomerEmailCartExtension(final SphereClient sphereClient) {
        return new SetCustomerEmailCartExtension(sphereClient);
    }
}
