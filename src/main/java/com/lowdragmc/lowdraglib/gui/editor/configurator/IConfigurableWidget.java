package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.data.Resources;
import com.lowdragmc.lowdraglib.gui.editor.data.resource.TexturesResource;
import com.lowdragmc.lowdraglib.gui.editor.runtime.AnnotationDetector;
import com.lowdragmc.lowdraglib.gui.editor.runtime.PersistedParser;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.UIResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/IConfigurableWidget.class */
public interface IConfigurableWidget extends IConfigurable {
    public static final Function<String, AnnotationDetector.Wrapper<LDLRegister, IConfigurableWidget>> CACHE = Util.m_143827_(type -> {
        for (AnnotationDetector.Wrapper<LDLRegister, IConfigurableWidget> wrapper : AnnotationDetector.REGISTER_WIDGETS) {
            if (wrapper.annotation().name().equals(type)) {
                return wrapper;
            }
        }
        return null;
    });

    @FunctionalInterface
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/configurator/IConfigurableWidget$IIdProvider.class */
    public interface IIdProvider extends Supplier<String> {
    }

    default Widget widget() {
        return (Widget) this;
    }

    default void initTemplate() {
    }

    default boolean canDragIn(Object dragging) {
        if ((dragging instanceof IGuiTexture) || (dragging instanceof String) || (dragging instanceof IIdProvider) || (dragging instanceof Integer)) {
            return true;
        }
        return false;
    }

    default boolean handleDragging(Object dragging) {
        if (dragging instanceof IGuiTexture) {
            IGuiTexture guiTexture = (IGuiTexture) dragging;
            widget().setBackground(guiTexture);
            return true;
        } else if (dragging instanceof String) {
            String string = (String) dragging;
            widget().setHoverTooltips(string);
            return true;
        } else if (dragging instanceof IIdProvider) {
            IIdProvider idProvider = (IIdProvider) dragging;
            widget().setId(idProvider.get());
            return true;
        } else if (dragging instanceof Integer) {
            Integer color = (Integer) dragging;
            widget().setBackground(new ColorRectTexture(color.intValue()));
            return true;
        } else {
            return false;
        }
    }

    static CompoundTag serializeNBT(IConfigurableWidget widget, Resources resources, boolean isProject) {
        UIResourceTexture.setCurrentResource(resources.resources.get(TexturesResource.RESOURCE_NAME), isProject);
        CompoundTag tag = widget.serializeInnerNBT();
        UIResourceTexture.clearCurrentResource();
        return tag;
    }

    static void deserializeNBT(IConfigurableWidget widget, CompoundTag tag, Resources resources, boolean isProject) {
        UIResourceTexture.setCurrentResource(resources.resources.get(TexturesResource.RESOURCE_NAME), isProject);
        widget.deserializeInnerNBT(tag);
        UIResourceTexture.clearCurrentResource();
    }

    default CompoundTag serializeInnerNBT() {
        CompoundTag tag = new CompoundTag();
        PersistedParser.serializeNBT(tag, getClass(), this);
        return tag;
    }

    default void deserializeInnerNBT(CompoundTag nbt) {
        PersistedParser.deserializeNBT(nbt, new HashMap(), getClass(), this);
    }

    default CompoundTag serializeWrapper() {
        CompoundTag tag = new CompoundTag();
        tag.m_128359_("type", name());
        tag.m_128365_("data", serializeInnerNBT());
        return tag;
    }

    @Nullable
    static IConfigurableWidget deserializeWrapper(CompoundTag tag) {
        String type = tag.m_128461_("type");
        AnnotationDetector.Wrapper<LDLRegister, IConfigurableWidget> wrapper = CACHE.apply(type);
        if (wrapper != null) {
            IConfigurableWidget child = wrapper.creator().get();
            child.deserializeInnerNBT(tag.m_128469_("data"));
            return child;
        }
        return null;
    }
}
