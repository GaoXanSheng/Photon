package com.lowdragmc.lowdraglib.gui.editor.configurator;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/ValueConfigurator.class */
public abstract class ValueConfigurator<T> extends Configurator {
    protected boolean forceUpdate;
    @Nullable
    protected T value;
    @Nonnull
    protected T defaultValue;
    protected Consumer<T> onUpdate;
    protected Supplier<T> supplier;

    public void setOnUpdate(Consumer<T> onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void setSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public ValueConfigurator(String name, Supplier<T> supplier, Consumer<T> onUpdate, @Nonnull T defaultValue, boolean forceUpdate) {
        super(name);
        setClientSideWidget();
        this.supplier = supplier;
        this.onUpdate = onUpdate;
        this.defaultValue = defaultValue;
        this.forceUpdate = forceUpdate;
        this.value = supplier.get();
        this.name = name;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateValue() {
        if (this.onUpdate != null) {
            this.onUpdate.accept(this.value);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onValueUpdate(T newValue) {
        this.value = newValue;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void updateScreen() {
        super.updateScreen();
        if (this.forceUpdate) {
            onValueUpdate(this.supplier.get());
        }
    }
}
