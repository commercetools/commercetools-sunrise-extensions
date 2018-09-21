package com.commercetools.sunrise.extensions;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.extensions.Trigger;
import io.sphere.sdk.extensions.TriggerBuilder;
import io.sphere.sdk.extensions.TriggerType;
import org.junit.Test;

import java.util.List;

import static com.commercetools.sunrise.extensions.boot.ExtensionsController.SET_CUSTOMER_EMAIL_ENDPOINT;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class SetCustomerEmailCartExtensionIntegrationTest extends ExtensionIntegrationTest {

    private static final List<Trigger> TRIGGERS = singletonList(TriggerBuilder.of(Cart.referenceTypeId(), singletonList(TriggerType.CREATE)).build());

    @Test
    public void onCreateCartWithoutCustomerDoesNothing() {
        withExtension(extensionDraft(SET_CUSTOMER_EMAIL_ENDPOINT, TRIGGERS), extension -> {
            final CartDraft cartDraft = cartDraft();
            assertThat(cartDraft.getCustomerId()).isNullOrEmpty();
            assertThat(cartDraft.getCustomerEmail()).isNullOrEmpty();
            withCart(cartDraft, cart -> {
                assertThat(cart.getCustomerEmail()).isNullOrEmpty();
                return cart;
            });
            return extension;
        });
    }

    @Test
    public void onCreateCartWithEmailDoesNothing() {
        withExtension(extensionDraft(SET_CUSTOMER_EMAIL_ENDPOINT, TRIGGERS), extension -> {
            withCustomer(customerDraft(), customer -> {
                final CartDraft cartDraft = CartDraftBuilder.of(cartDraft())
                        .customerId(customer.getId())
                        .customerEmail(customer.getEmail())
                        .build();
                withCart(cartDraft, cart -> {
                    assertThat(cart.getCustomerEmail()).isEqualTo(customer.getEmail());
                    return cart;
                });
                return customer;
            });
            return extension;
        });
    }

    @Test
    public void onCreateCartWithoutEmailSetsIt() {
        withExtension(extensionDraft(SET_CUSTOMER_EMAIL_ENDPOINT, TRIGGERS), extension -> {
            withCustomer(customerDraft(), customer -> {
                final CartDraft cartDraft = CartDraftBuilder.of(cartDraft())
                        .customerId(customer.getId())
                        .build();
                assertThat(cartDraft.getCustomerEmail()).isNullOrEmpty();
                withCart(cartDraft, cart -> {
                    assertThat(cart.getCustomerEmail()).isEqualTo(customer.getEmail());
                    return cart;
                });
                return customer;
            });
            return extension;
        });
    }
}

