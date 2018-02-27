package com.commercetools.sunrise.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sphere.sdk.json.SphereJsonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return SphereJsonUtils.newObjectMapper();
    }
}
