package com.commercetools.sunrise.extensions;

import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.utils.CompletableFutureUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.commercetools.sunrise.extensions.SetCustomerEmailExtensionsController.ENDPOINT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SetCustomerEmailExtensionsControllerTest extends ControllerTest {

    @MockBean
    private BlockingSphereClient ctp;

    @Before
    public void setUp() {
        final Customer customer = SphereJsonUtils.readObjectFromResource("customer.json", Customer.class);
        when(ctp.execute(any())).thenReturn(CompletableFutureUtils.successful(customer));
    }

    @Test
    public void onCartWithEmailDoesNothing() throws Exception {
        testUrlWithInput(ENDPOINT, "input-cart-with-email.json")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actions", hasSize(0)));
    }

    @Test
    public void onCartWithoutCustomerDoesNothing() throws Exception {
        testUrlWithInput(ENDPOINT, "input-cart-without-customer.json")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actions", hasSize(0)));
    }

    @Test
    public void onCartWithoutEmailSetsCustomerEmail() throws Exception {
        testUrlWithInput(ENDPOINT, "input-cart-without-email.json")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actions", hasSize(1)))
                .andExpect(jsonPath("$.actions[0].action", equalTo("setCustomerEmail")))
                .andExpect(jsonPath("$.actions[0].email", equalTo("jane.doe@email.com")));
    }
}