package com.commercetools.sunrise.extensions;

import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.utils.CompletableFutureUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExtensionsControllerTest extends ControllerTest {

    @MockBean
    private BlockingSphereClient ctp;

    @Before
    public void setUp() {
        final Customer customer = SphereJsonUtils.readObjectFromResource("customer.json", Customer.class);
        when(ctp.execute(any())).thenReturn(CompletableFutureUtils.successful(customer));
    }

    @Test
    public void onCartWithEmailShouldDoNothing() throws Exception {
        testUrlWithInput("/carts/set-customer-email", "cart-with-email.json")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actions", hasSize(0)));
    }

    @Test
    public void onCartWithoutEmailShouldSetCustomerEmail() throws Exception {
        testUrlWithInput("/carts/set-customer-email", "cart-without-email.json")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actions", hasSize(1)))
                .andExpect(jsonPath("$.actions[0].action", equalTo("setCustomerEmail")))
                .andExpect(jsonPath("$.actions[0].email", equalTo("john.smith@test.commercetools.com")));
    }
}