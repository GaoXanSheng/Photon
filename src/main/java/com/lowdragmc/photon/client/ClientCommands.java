package com.lowdragmc.photon.client;

import com.lowdragmc.photon.client.emitter.PhotonParticleRenderType;
import com.lowdragmc.photon.client.fx.BlockEffect;
import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.lowdragmc.photon.core.mixins.accessor.ParticleEngineAccessor;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/ClientCommands.class */
public class ClientCommands {
    public static <S> List<LiteralArgumentBuilder<S>> createClientCommands() {
        return List.of(com.lowdragmc.lowdraglib.client.ClientCommands.createLiteral("photon_client").then(com.lowdragmc.lowdraglib.client.ClientCommands.createLiteral("clear_particles").executes(context -> {
            ParticleEngineAccessor particleEngineAccessor = Minecraft.m_91087_().f_91061_;
            if (particleEngineAccessor instanceof ParticleEngineAccessor) {
                ParticleEngineAccessor accessor = particleEngineAccessor;
                accessor.getParticles().entrySet().removeIf(entry -> {
                    return entry.getKey() instanceof PhotonParticleRenderType;
                });
            }
            EntityEffect.CACHE.clear();
            BlockEffect.CACHE.clear();
            return 1;
        })).then(com.lowdragmc.lowdraglib.client.ClientCommands.createLiteral("clear_client_fx_cache").executes(context2 -> {
            if (Minecraft.m_91087_().f_91074_ != null) {
                Minecraft.m_91087_().f_91074_.m_213846_(Component.m_237113_("clear client cache fx: " + FXHelper.clearCache()));
                return 1;
            }
            FXHelper.clearCache();
            return 1;
        })));
    }
}
