package com.lowdragmc.lowdraglib.client.forge;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientCommandsImpl {
    public static LiteralArgumentBuilder createLiteral(String command) {
        return Commands.literal(command);
    }
}
