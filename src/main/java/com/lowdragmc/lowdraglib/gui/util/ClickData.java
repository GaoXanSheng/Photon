package com.lowdragmc.lowdraglib.gui.util;

import com.lowdragmc.lowdraglib.core.mixins.accessor.MouseHandlerAccessor;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/util/ClickData.class */
public class ClickData {
    public final int button;
    public final boolean isShiftClick;
    public final boolean isCtrlClick;
    public final boolean isRemote;

    private ClickData(int button, boolean isShiftClick, boolean isCtrlClick, boolean isRemote) {
        this.button = button;
        this.isShiftClick = isShiftClick;
        this.isCtrlClick = isCtrlClick;
        this.isRemote = isRemote;
    }

    @OnlyIn(Dist.CLIENT)
    public ClickData() {
        int i;
        MouseHandlerAccessor mouseHandlerAccessor = Minecraft.m_91087_().f_91067_;
        long id = Minecraft.m_91087_().m_91268_().m_85439_();
        if (mouseHandlerAccessor instanceof MouseHandlerAccessor) {
            MouseHandlerAccessor accessor = mouseHandlerAccessor;
            i = accessor.getActiveButton();
        } else {
            i = -1;
        }
        this.button = i;
        this.isShiftClick = InputConstants.m_84830_(id, 340) || InputConstants.m_84830_(id, 344);
        this.isCtrlClick = InputConstants.m_84830_(id, 341) || InputConstants.m_84830_(id, 345);
        this.isRemote = true;
    }

    @OnlyIn(Dist.CLIENT)
    public void writeToBuf(FriendlyByteBuf buf) {
        buf.m_130130_(this.button);
        buf.writeBoolean(this.isShiftClick);
        buf.writeBoolean(this.isCtrlClick);
    }

    public static ClickData readFromBuf(FriendlyByteBuf buf) {
        int button = buf.m_130242_();
        boolean shiftClick = buf.readBoolean();
        boolean ctrlClick = buf.readBoolean();
        return new ClickData(button, shiftClick, ctrlClick, false);
    }
}
