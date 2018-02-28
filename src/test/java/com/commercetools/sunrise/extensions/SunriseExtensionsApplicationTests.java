package com.commercetools.sunrise.extensions;

import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.utils.CompletableFutureUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import static com.commercetools.sunrise.extensions.ExtensionHeaders.AUTH;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(ExtensionsController.class)
@ActiveProfiles({"test"})
@ContextConfiguration(classes = DefaultWiring.class)
@WebAppConfiguration
public class SunriseExtensionsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlockingSphereClient ctp;

    @Before
    public void setUp() {
        final Customer customer = SphereJsonUtils.readObjectFromResource("customer.json", Customer.class);
        when(ctp.execute(any())).thenReturn(CompletableFutureUtils.successful(customer));
    }

    @Test
    public void onCartWithEmailShouldDoNothing() throws Exception {
        final String content = IOUtils.toString(new ClassPathResource("cart-with-email.json").getInputStream());
        final RequestBuilder request = post("/carts/set-customer-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header(AUTH, "secret");
        this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"actions\":[]}")));
    }

    @Test
    public void onCartWithoutEmailShouldSetCustomerEmail() throws Exception {
        final String content = IOUtils.toString(new ClassPathResource("cart-without-email.json").getInputStream());
        final RequestBuilder request = post("/carts/set-customer-email")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
                .header(AUTH, "secret");
        MvcResult mvcResult = this.mockMvc.perform(request)
                .andExpect(request().asyncStarted())
                .andReturn();
        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actions", hasSize(1)))
                .andExpect(jsonPath("$.actions[0].action", equalTo("setCustomerEmail")))
                .andExpect(jsonPath("$.actions[0].email", equalTo("john.smith@test.commercetools.com")));
    }
}