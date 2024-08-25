package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/NumberConfigurator.class */
public class NumberConfigurator extends ValueConfigurator<Number> {
    protected boolean isDecimal;
    protected TextFieldWidget textFieldWidget;
    protected ImageWidget image;
    protected Number min;
    protected Number max;
    protected Number wheel;
    protected boolean colorBackground;

    public void setColorBackground(boolean colorBackground) {
        this.colorBackground = colorBackground;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public NumberConfigurator(String name, Supplier<Number> supplier, Consumer<Number> onUpdate, @Nonnull Number defaultValue, boolean forceUpdate) {
        super(name, supplier, onUpdate, defaultValue, forceUpdate);
        if (this.value == 0) {
            this.value = defaultValue;
        }
        this.isDecimal = (this.value instanceof Double) || (this.value instanceof Float);
        setRange((Number) this.value, (Number) this.value);
        if (this.isDecimal) {
            setWheel(Double.valueOf(0.1d));
        } else {
            setWheel(1);
        }
    }

    public NumberConfigurator setRange(Number min, Number max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public NumberConfigurator setWheel(Number wheel) {
        if (wheel.doubleValue() == 0.0d) {
            return this;
        }
        this.wheel = wheel;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.ValueConfigurator
    public void onValueUpdate(Number newValue) {
        if (newValue == null) {
            newValue = (Number) this.defaultValue;
        }
        if (newValue.equals(this.value)) {
            return;
        }
        super.onValueUpdate((NumberConfigurator) newValue);
        if (this.isDecimal) {
            this.textFieldWidget.setCurrentString(Float.valueOf(((Number) this.value).floatValue()));
        } else {
            this.textFieldWidget.setCurrentString(Long.valueOf(((Number) this.value).longValue()));
        }
    }

    private IGuiTexture getCommonColor() {
        return this.colorBackground ? new ColorRectTexture(((Number) this.value).intValue()).setRadius(5.0f).setRadius(5.0f) : ColorPattern.T_GRAY.rectTexture().setRadius(5.0f);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.Configurator
    public void init(int width) {
        super.init(width);
        ImageWidget imageWidget = new ImageWidget(this.leftWidth, 2, ((width - this.leftWidth) - 3) - this.rightWidth, 10, getCommonColor());
        this.image = imageWidget;
        addWidget(imageWidget);
        this.image.setDraggingConsumer(o -> {
            return o instanceof Number;
        }, o2 -> {
            this.image.setImage(ColorPattern.GREEN.rectTexture().setRadius(5.0f));
        }, o3 -> {
            this.image.setImage(getCommonColor());
        }, o4 -> {
            if (o4 instanceof Number) {
                Number number = (Number) o4;
                onValueUpdate(number);
                updateValue();
            }
            this.image.setImage(getCommonColor());
        });
        TextFieldWidget textFieldWidget = new TextFieldWidget(this.leftWidth + 3, 2, ((width - this.leftWidth) - 6) - this.rightWidth, 10, null, this::onNumberUpdate);
        this.textFieldWidget = textFieldWidget;
        addWidget(textFieldWidget);
        this.textFieldWidget.setClientSideWidget();
        if (this.isDecimal) {
            this.textFieldWidget.setCurrentString(Float.valueOf(((Number) this.value).floatValue()));
        } else {
            this.textFieldWidget.setCurrentString(Long.valueOf(((Number) this.value).longValue()));
        }
        this.textFieldWidget.setBordered(false);
        if (this.isDecimal) {
            this.textFieldWidget.setNumbersOnly(this.min.floatValue(), this.max.floatValue());
        } else {
            this.textFieldWidget.setNumbersOnly(this.min.longValue(), this.max.longValue());
        }
        this.textFieldWidget.setWheelDur(this.wheel.floatValue());
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [T, java.lang.Byte] */
    /* JADX WARN: Type inference failed for: r1v17, types: [T, java.lang.Double] */
    /* JADX WARN: Type inference failed for: r1v2, types: [T, java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r1v23, types: [java.lang.Float, T] */
    /* JADX WARN: Type inference failed for: r1v29, types: [T, java.lang.Long] */
    /* JADX WARN: Type inference failed for: r1v37, types: [T, java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r1v5, types: [java.lang.Float, T] */
    private void onNumberUpdate(String s) {
        Number newValue = Float.valueOf(this.isDecimal ? Float.parseFloat(s) : (float) Long.parseLong(s));
        if ((this.value instanceof Integer) && !((Number) this.value).equals(Integer.valueOf(newValue.intValue()))) {
            this.value = Integer.valueOf(newValue.intValue());
            updateValue();
        } else if ((this.value instanceof Long) && !((Number) this.value).equals(Long.valueOf(newValue.longValue()))) {
            this.value = Long.valueOf(newValue.longValue());
            updateValue();
        } else if ((this.value instanceof Float) && !((Number) this.value).equals(Float.valueOf(newValue.floatValue()))) {
            this.value = Float.valueOf(newValue.floatValue());
            updateValue();
        } else if ((this.value instanceof Double) && !((Number) this.value).equals(Double.valueOf(newValue.doubleValue()))) {
            this.value = Double.valueOf(newValue.doubleValue());
            updateValue();
        } else if ((this.value instanceof Byte) && !((Number) this.value).equals(Byte.valueOf(newValue.byteValue()))) {
            this.value = Byte.valueOf(newValue.byteValue());
            updateValue();
        } else if (this.value == 0) {
            if (this.isDecimal) {
                this.value = Float.valueOf(newValue.floatValue());
            } else {
                this.value = Integer.valueOf(newValue.intValue());
            }
            updateValue();
        }
        if (this.colorBackground) {
            this.image.setImage(getCommonColor());
        }
    }
}
