package com.lowdragmc.photon.forge;

import com.lowdragmc.photon.Photon;
import com.lowdragmc.photon.PhotonCommonProxy;
import com.lowdragmc.photon.ServerCommands;
import com.lowdragmc.photon.command.FxLocationArgument;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import java.util.Objects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/forge/CommonProxyImpl.class */
public class CommonProxyImpl {
    static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARG_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, Photon.MOD_ID);
    static final RegistryObject<ArgumentTypeInfo<FxLocationArgument, ?>> FX_LOCATION_ARG_TYPE = ARG_TYPES.register("fx_location", () -> {
        return SingletonArgumentInfo.m_235451_(FxLocationArgument::new);
    });

    public CommonProxyImpl() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.register(this);
        ARG_TYPES.register(eventBus);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommand);
        PhotonCommonProxy.init();
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
        ArgumentTypeInfos.registerByClass(FxLocationArgument.class, (ArgumentTypeInfo) FX_LOCATION_ARG_TYPE.get());
    }

    public void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        List<LiteralArgumentBuilder<CommandSourceStack>> createServerCommands = ServerCommands.createServerCommands();
        Objects.requireNonNull(dispatcher);
        createServerCommands.forEach(this::register);
    }
}
