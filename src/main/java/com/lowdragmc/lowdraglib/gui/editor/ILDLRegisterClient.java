package com.lowdragmc.lowdraglib.gui.editor;

import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegisterClient;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ILDLRegisterClient.class */
public interface ILDLRegisterClient extends ILDLRegister {
    @Override // com.lowdragmc.lowdraglib.gui.editor.ILDLRegister
    default boolean isLDLRegister() {
        return getClass().isAnnotationPresent(LDLRegisterClient.class);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.ILDLRegister
    @Deprecated
    default LDLRegister getRegisterUI() {
        throw new IllegalCallerException("call a client only register");
    }

    default LDLRegisterClient getRegisterUIClient() {
        return (LDLRegisterClient) getClass().getAnnotation(LDLRegisterClient.class);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.ILDLRegister
    default String name() {
        if (isLDLRegister()) {
            return getRegisterUIClient().name();
        }
        throw new RuntimeException("not registered %s".formatted(new Object[]{getClass()}));
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.ILDLRegister
    default String group() {
        if (isLDLRegister()) {
            return getRegisterUIClient().group();
        }
        throw new RuntimeException("not registered ui %s".formatted(new Object[]{getClass()}));
    }
}
