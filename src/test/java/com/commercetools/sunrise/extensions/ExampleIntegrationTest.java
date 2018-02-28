package com.commercetools.sunrise.extensions;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartDeleteCommand;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.customers.commands.CustomerDeleteCommand;
import io.sphere.sdk.extensions.*;
import io.sphere.sdk.extensions.commands.ExtensionCreateCommand;
import io.sphere.sdk.extensions.commands.ExtensionDeleteCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = {DefaultWiring.class, RestTemplate.class})
@ActiveProfiles("integration")
public class ExampleIntegrationTest {

    private static final String NGROK_API = "http://127.0.0.1:4040/api/tunnels/sunrise";
    private static final CurrencyUnit CURRENCY = Monetary.getCurrency("EUR");

    @Autowired
    private BlockingSphereClient ctp;

    @Autowired
    private RestTemplate ws;

    @Value("${authenticationKey}")
    private String authenticationKey;

    @Test
    public void onCreateCartWithoutCustomerDoesNothing() {
        withExtension(extensionDraft(), extension -> {
            final CartDraft cartDraft = CartDraftBuilder.of(CURRENCY).build();
            withCart(cartDraft, cart -> {
                assertThat(cart.getCustomerEmail()).isNullOrEmpty();
                return cart;
            });
            return extension;
        });
    }

    @Test
    public void onCreateCartWithEmailDoesNothing() {
        withExtension(extensionDraft(), extension -> {
            final String email = "it@mail.com";
            final CustomerDraft customerDraft = CustomerDraftBuilder.of(email, "1234").build();
            withCustomer(customerDraft, customer -> {
                final CartDraft draft = CartDraftBuilder.of(CURRENCY)
                        .customerId(customer.getId())
                        .customerEmail(email)
                        .build();
                withCart(draft, cart -> {
                    assertThat(cart.getCustomerEmail()).isEqualTo(email);
                    return cart;
                });
                return customer;
            });
            return extension;
        });
    }

    @Test
    public void onCreateCartWithoutEmailSetsIt() {
        withExtension(extensionDraft(), extension -> {
            final String email = "it@mail.com";
            final CustomerDraft customerDraft = CustomerDraftBuilder.of(email, "1234").build();
            withCustomer(customerDraft, customer -> {
                final CartDraft draft = CartDraftBuilder.of(CURRENCY)
                        .customerId(customer.getId())
                        .build();
                withCart(draft, cart -> {
                    assertThat(cart.getCustomerEmail()).isEqualTo(email);
                    return cart;
                });
                return customer;
            });
            return extension;
        });
    }


    private void withCart(final CartDraft draft, final Function<Cart, Cart> test) {
        final Cart cart = ctp.executeBlocking(CartCreateCommand.of(draft));
        final Cart finalCart = test.apply(cart);
        ctp.executeBlocking(CartDeleteCommand.of(finalCart));
    }

    private void withCustomer(final CustomerDraft draft, final Function<Customer, Customer> test) {
        final Customer customer = ctp.executeBlocking(CustomerCreateCommand.of(draft)).getCustomer();
        final Customer finalCustomer = test.apply(customer);
        ctp.executeBlocking(CustomerDeleteCommand.of(finalCustomer));
    }

    private void withExtension(final ExtensionDraft draft, final Function<Extension, Extension> test) {
        final Extension extension = ctp.executeBlocking(ExtensionCreateCommand.of(draft));
        final Extension finalExtension = test.apply(extension);
        ctp.executeBlocking(ExtensionDeleteCommand.of(finalExtension));
    }

    private ExtensionDraft extensionDraft() {
        final TunnelResource tunnelResource = ws.getForObject(NGROK_API, TunnelResource.class);
        final String url = tunnelResource.getPublic_url() + SetCustomerEmailExtensionsController.ENDPOINT;
        final AzureFunctionsAuthentication authentication = AzureFunctionsAuthenticationBuilder.of(authenticationKey).build();
        final HttpDestination destination = HttpDestinationBuilder.of(url, authentication).build();
        final Trigger trigger = TriggerBuilder.of(Cart.referenceTypeId(), singletonList(TriggerType.CREATE)).build();
        return ExtensionDraftBuilder.of("it", destination, singletonList(trigger)).build();
    }
}

