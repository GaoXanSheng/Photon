package com.lowdragmc.photon;

import com.lowdragmc.photon.command.BlockEffectCommand;
import com.lowdragmc.photon.command.EntityEffectCommand;
import com.lowdragmc.photon.command.FxLocationArgument;
import com.lowdragmc.photon.gui.ParticleEditorFactory;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/ServerCommands.class */
public class ServerCommands {
    public static List<LiteralArgumentBuilder<CommandSourceStack>> createServerCommands() {
        return List.of(Commands.literal(Photon.MOD_ID).then(Commands.literal("particle_editor").executes(context -> {
            ParticleEditorFactory.INSTANCE.openUI(ParticleEditorFactory.INSTANCE, ((CommandSourceStack) context.getSource()).getPlayerOrException());
            return 1;
        })).then(Commands.literal("fx").requires(source -> {
            return source.hasPermission(2);
        }).then(Commands.argument("location", new FxLocationArgument()).then(BlockEffectCommand.createServerCommand()).then(EntityEffectCommand.createServerCommand()))));
    }
}
