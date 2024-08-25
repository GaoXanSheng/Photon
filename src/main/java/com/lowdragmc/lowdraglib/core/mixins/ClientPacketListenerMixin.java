package com.lowdragmc.lowdraglib.core.mixins;

import com.lowdragmc.lowdraglib.gui.compass.CompassManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPacketListener.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/ClientPacketListenerMixin.class */
public abstract class ClientPacketListenerMixin {
    @Shadow
    @Final
    private Minecraft f_104888_;

    @Inject(method = {"handleUpdateTags"}, at = {@At("RETURN")})
    private void injectCreateReload(ClientboundUpdateTagsPacket packet, CallbackInfo ci) {
        CompassManager.INSTANCE.m_6213_(this.f_104888_.m_91098_());
    }
}
