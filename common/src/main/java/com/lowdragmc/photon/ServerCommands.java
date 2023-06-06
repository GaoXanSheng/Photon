package com.lowdragmc.photon;

import com.lowdragmc.photon.command.BlockEffectCommand;
import com.lowdragmc.photon.gui.ParticleEditorFactory;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;

import java.util.List;

/**
 * @author KilaBash
 * @date 2023/2/9
 * @implNote ServerCommands
 */
public class ServerCommands {
    public static List<LiteralArgumentBuilder<CommandSourceStack>> createServerCommands() {
        return List.of(
                Commands.literal("photon")
                        .then(Commands.literal("particle_editor")
                                .executes(context -> {
                                    ParticleEditorFactory.INSTANCE.openUI(ParticleEditorFactory.INSTANCE, context.getSource().getPlayerOrException());
                                    return 1;
                                })
                        )
                        .then(Commands.literal("fx")
                                .then(Commands.argument("location", ResourceLocationArgument.id())
                                        .then(BlockEffectCommand.createCommand())
                                ))

        );
    }
}
