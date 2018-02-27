package com.commercetools.sunrise.extensions;

import io.sphere.sdk.client.BlockingSphereClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ExtensionsController.class)
public class SunriseExtensionsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlockingSphereClient service;

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {
        final MockHttpServletRequestBuilder post = post("/carts");
        post.contentType(MediaType.APPLICATION_JSON);
        post.content("{}");
        this.mockMvc.perform(post).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"actions\":[]}")));
    }
}