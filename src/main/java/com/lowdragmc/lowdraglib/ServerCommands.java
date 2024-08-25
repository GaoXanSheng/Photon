package com.lowdragmc.lowdraglib;

import com.lowdragmc.lowdraglib.gui.factory.UIEditorFactory;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/ServerCommands.class */
public class ServerCommands {
    public static List<LiteralArgumentBuilder<CommandSourceStack>> createServerCommands() {
        return List.of(Commands.m_82127_("ldlib").then(Commands.m_82127_("ui_editor").executes(context -> {
            UIEditorFactory.INSTANCE.openUI(UIEditorFactory.INSTANCE, ((CommandSourceStack) context.getSource()).m_81375_());
            return 1;
        })).then(Commands.m_82127_("copy_block_tag").then(Commands.m_82129_("pos", BlockPosArgument.m_118239_()).executes(context2 -> {
            BlockPos pos = BlockPosArgument.m_118242_(context2, "pos");
            ServerLevel world = ((CommandSourceStack) context2.getSource()).m_81372_();
            BlockEntity blockEntity = world.m_7702_(pos);
            if (blockEntity != null) {
                CompoundTag tag = blockEntity.m_187482_();
                String value = NbtUtils.m_178063_(tag);
                ((CommandSourceStack) context2.getSource()).m_81354_(Component.m_237113_("[Copy to clipboard]").m_130948_(Style.f_131099_.m_131140_(ChatFormatting.YELLOW).m_131142_(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, value))).m_7220_(NbtUtils.m_178061_(tag)), true);
                return 1;
            }
            ((CommandSourceStack) context2.getSource()).m_81354_(Component.m_237113_("No block entity at " + pos).m_130948_(Style.f_131099_.m_131140_(ChatFormatting.RED)), true);
            return 1;
        }))).then(Commands.m_82127_("copy_entity_tag").then(Commands.m_82129_("entity", EntityArgument.m_91449_()).executes(context3 -> {
            Entity entity = EntityArgument.m_91452_(context3, "entity");
            CompoundTag tag = entity.m_20240_(new CompoundTag());
            String value = NbtUtils.m_178063_(tag);
            ((CommandSourceStack) context3.getSource()).m_81354_(Component.m_237113_("[Copy to clipboard]").m_130948_(Style.f_131099_.m_131140_(ChatFormatting.YELLOW).m_131142_(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, value))).m_7220_(NbtUtils.m_178061_(tag)), true);
            return 1;
        }))));
    }
}
