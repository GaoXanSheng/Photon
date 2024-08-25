package com.lowdragmc.lowdraglib.jei;

import java.util.Collection;
import javax.annotation.Nonnull;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/jei/AbstractIngredient.class */
public abstract class AbstractIngredient<T> implements IIngredientType<T>, IIngredientHelper<T>, IIngredientRenderer<T> {
    public abstract Collection<T> getAllIngredients();

    public void registerIngredients(IModIngredientRegistration registry) {
        registry.register(this, getAllIngredients(), this, this);
    }

    @Nonnull
    public IIngredientType<T> getIngredientType() {
        return this;
    }
}
