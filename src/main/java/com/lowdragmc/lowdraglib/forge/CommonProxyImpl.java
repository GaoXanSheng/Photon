package com.lowdragmc.lowdraglib.forge;

import com.lowdragmc.lowdraglib.CommonProxy;
import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.ServerCommands;
import com.lowdragmc.lowdraglib.plugin.ILDLibPlugin;
import com.lowdragmc.lowdraglib.plugin.LDLibPlugin;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.test.TestBlock;
import com.lowdragmc.lowdraglib.test.TestBlockEntity;
import com.lowdragmc.lowdraglib.test.TestItem;
import com.lowdragmc.lowdraglib.test.forge.TestBlockEntityImpl;
import com.lowdragmc.lowdraglib.utils.ReflectionUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.datafixers.types.Type;
import java.util.List;
import java.util.Objects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/forge/CommonProxyImpl.class */
public class CommonProxyImpl {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "ldlib");
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "ldlib");
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "ldlib");

    public CommonProxyImpl() {
        if (Platform.isDevEnv()) {
            BLOCKS.register("test", () -> {
                return TestBlock.BLOCK;
            });
            ITEMS.register("test", () -> {
                return TestItem.ITEM;
            });
            TestBlockEntityImpl.TYPE = BLOCK_ENTITY_TYPES.register("test", () -> {
                return BlockEntityType.Builder.m_155273_(TestBlockEntity::new, new Block[]{TestBlock.BLOCK}).m_58966_((Type) null);
            });
        }
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommand);
        CommonProxy.init();
        ReflectionUtils.findAnnotationClasses(LDLibPlugin.class, clazz -> {
            try {
                Object patt2556$temp = clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
                if (patt2556$temp instanceof ILDLibPlugin) {
                    ILDLibPlugin plugin = (ILDLibPlugin) patt2556$temp;
                    plugin.onLoad();
                }
            } catch (Throwable th) {
            }
        }, () -> {
        });
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
    }

    @SubscribeEvent
    public void loadComplete(FMLLoadCompleteEvent e) {
        e.enqueueWork(TypedPayloadRegistries::postInit);
    }

    public void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        List<LiteralArgumentBuilder<CommandSourceStack>> createServerCommands = ServerCommands.createServerCommands();
        Objects.requireNonNull(dispatcher);
        createServerCommands.forEach(this::register);
    }
}
