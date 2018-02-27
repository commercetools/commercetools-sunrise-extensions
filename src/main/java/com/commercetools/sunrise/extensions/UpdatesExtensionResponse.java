package com.commercetools.sunrise.extensions;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.utils.SphereInternalUtils;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@Data
public class UpdatesExtensionResponse<T> implements ExtensionResponse<Cart> {
    private List<UpdateAction<T>> actions;

    @SafeVarargs
    public UpdatesExtensionResponse(@NonNull final UpdateAction<T> action, final UpdateAction<T> ... furtherActions) {
        final List<UpdateAction<T>> actions = new ArrayList<>(1 + furtherActions.length);
        actions.add(action);
        actions.addAll(asList(furtherActions));
        this.actions = Collections.unmodifiableList(actions);
    }
}
