package com.commercetools.sunrise.extensions;

import com.commercetools.sunrise.extensions.boot.DefaultWiring;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartDeleteCommand;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.client.SphereClientUtils;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.customers.commands.CustomerDeleteCommand;
import io.sphere.sdk.extensions.*;
import io.sphere.sdk.extensions.commands.ExtensionCreateCommand;
import io.sphere.sdk.extensions.commands.ExtensionDeleteCommand;
import io.sphere.sdk.extensions.queries.ExtensionQuery;
import io.sphere.sdk.queries.QueryExecutionUtils;
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
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = {DefaultWiring.class, RestTemplate.class})
@ActiveProfiles({"ci", "it"})
public abstract class ExtensionIntegrationTest {

    private static final String NGROK_API = "http://127.0.0.1:4040/api/tunnels/sunrise";
    private static final CurrencyUnit CURRENCY = Monetary.getCurrency("EUR");

    @Autowired
    private BlockingSphereClient ctp;

    @Autowired
    private RestTemplate ws;

    @Value("${authenticationKey}")
    private String authenticationKey;

    protected void withCart(final CartDraft draft, final Function<Cart, Cart> test) {
        final Cart cart = ctp.executeBlocking(CartCreateCommand.of(draft));
        final Cart finalCart = test.apply(cart);
        ctp.executeBlocking(CartDeleteCommand.of(finalCart));
    }

    protected void withCustomer(final CustomerDraft draft, final Function<Customer, Customer> test) {
        final Customer customer = ctp.executeBlocking(CustomerCreateCommand.of(draft)).getCustomer();
        final Customer finalCustomer = test.apply(customer);
        ctp.executeBlocking(CustomerDeleteCommand.of(finalCustomer));
    }

    protected void withExtension(final ExtensionDraft draft, final Function<Extension, Extension> test) {
        final Extension extension = ctp.executeBlocking(ExtensionCreateCommand.of(draft));
        final Extension finalExtension = test.apply(extension);
        ctp.executeBlocking(ExtensionDeleteCommand.of(finalExtension));
    }

    protected ExtensionDraft extensionDraft(final String endpoint, final List<Trigger> triggers) {
        final TunnelResource tunnelResource = ws.getForObject(NGROK_API, TunnelResource.class);
        final String url = tunnelResource.getPublic_url() + endpoint;
        final AzureFunctionsAuthentication authentication = AzureFunctionsAuthenticationBuilder.of(authenticationKey).build();
        final HttpDestination destination = HttpDestinationBuilder.of(url, authentication).build();
        return ExtensionDraftBuilder.of("it" + randomKey(), destination, triggers).build();
    }

    protected CustomerDraft customerDraft() {
        final String email = randomKey() + "@email.com";
        return CustomerDraftBuilder.of(email, "1234").build();
    }

    protected CartDraft cartDraft() {
        return CartDraftBuilder.of(CURRENCY).build();
    }

    protected void removeAllRegisteredExtensions() {
        final CompletionStage<List<Extension>> extensionsStage = QueryExecutionUtils.queryAll(ctp, ExtensionQuery.of());
        final List<Extension> extensions = SphereClientUtils.blockingWait(extensionsStage, Duration.ofSeconds(30));
        extensions.forEach(extension -> ctp.executeBlocking(ExtensionDeleteCommand.of(extension)));
    }

    private String randomKey() {
        return UUID.randomUUID().toString();
    }
}

