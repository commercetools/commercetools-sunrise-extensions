package com.commercetools.sunrise.extensions;

import io.sphere.sdk.client.BlockingSphereClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static com.commercetools.sunrise.extensions.ExtensionHeaders.AUTH;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ExtensionsController.class)
@ActiveProfiles({"test"})
@ContextConfiguration(classes = DefaultWiring.class)
public class SunriseExtensionsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlockingSphereClient service;

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {
        final RequestBuilder request =
                post("/carts/set-customer-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .header(AUTH, "secret");
        this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"actions\":[]}")));
    }
}