package com.commercetools.sunrise.extensions;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.extensions.*;
import io.sphere.sdk.extensions.commands.ExtensionCreateCommand;
import io.sphere.sdk.extensions.queries.ExtensionByKeyGet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonList;

@Slf4j
@Component
public class SetCustomerEmailCartExtensionSetup {

    @Value("${ext.customerEmail.key}")
    private String extensionKey;

    @Value("${ext.customerEmail.httpDestinationUrl}")
    private String httpDestinationUrl;

    @Value("${authenticationKey}")
    private String authenticationKey;

    @Autowired
    private BlockingSphereClient sphereClient;

    @EventListener(ContextRefreshedEvent.class)
    public void createExtensionIfMissing() {
        if (isExtensionPresent()) {
            log.info("Extension \"{}\" found in the project", extensionKey);
        } else {
            log.info("Not found extension \"{}\", creating it...", extensionKey);
            createExtension();
        }
    }

    private boolean isExtensionPresent() {
        return sphereClient.executeBlocking(ExtensionByKeyGet.of(extensionKey)) != null;
    }

    private void createExtension() {
        final Trigger trigger = TriggerBuilder.of(Cart.referenceTypeId(), singletonList(TriggerType.CREATE)).build();
        final ExtensionDraft draft = ExtensionDraftBuilder.of(extensionKey, httpDestination(), singletonList(trigger)).build();
        sphereClient.executeBlocking(ExtensionCreateCommand.of(draft));
    }

    private HttpDestination httpDestination() {
        final AzureFunctionsAuthentication authentication = AzureFunctionsAuthenticationBuilder.of(authenticationKey).build();
        return HttpDestinationBuilder.of(httpDestinationUrl, authentication).build();
    }
}
