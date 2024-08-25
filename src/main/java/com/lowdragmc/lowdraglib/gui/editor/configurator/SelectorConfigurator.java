package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.widget.SelectorWidget;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/SelectorConfigurator.class */
public class SelectorConfigurator<T> extends ValueConfigurator<T> {
    protected List<T> candidates;
    protected Function<T, String> mapping;
    protected Map<String, T> nameMap;
    protected SelectorWidget selector;
    protected int max;
    protected boolean isUp;

    public SelectorWidget getSelector() {
        return this.selector;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setUp(boolean isUp) {
        this.isUp = isUp;
    }

    public SelectorConfigurator(String name, Supplier<T> supplier, Consumer<T> onUpdate, @Nonnull T defaultValue, boolean forceUpdate, List<T> candidates, Function<T, String> mapping) {
        super(name, supplier, onUpdate, defaultValue, forceUpdate);
        this.max = 5;
        this.isUp = true;
        this.candidates = candidates;
        this.mapping = mapping;
        this.nameMap = new HashMap();
        for (T candidate : candidates) {
            this.nameMap.put(mapping.apply(candidate), candidate);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ValueConfigurator
    public void onValueUpdate(T newValue) {
        if (newValue == null) {
            newValue = this.defaultValue;
        }
        if (newValue.equals(this.value)) {
            return;
        }
        super.onValueUpdate(newValue);
        this.selector.setValue(this.mapping.apply(newValue));
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void init(int width) {
        super.init(width);
        SelectorWidget value = new SelectorWidget(this.leftWidth, 2, ((width - this.leftWidth) - 3) - this.rightWidth, 10, this.nameMap.keySet().stream().toList(), -1).setOnChanged(s -> {
            this.value = this.nameMap.get(s);
            updateValue();
        }).setMaxCount(this.max).setIsUp(this.isUp).setButtonBackground(ColorPattern.T_GRAY.rectTexture().setRadius(5.0f)).setBackground(ColorPattern.BLACK.rectTexture()).setValue(this.mapping.apply(this.value));
        this.selector = value;
        addWidget(value);
    }
}
