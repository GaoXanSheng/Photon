package com.lowdragmc.lowdraglib.client;

import com.lowdragmc.lowdraglib.client.forge.ClientCommandsImpl;
import com.lowdragmc.lowdraglib.client.shader.Shaders;
import com.lowdragmc.lowdraglib.client.shader.management.ShaderManager;
import com.lowdragmc.lowdraglib.gui.compass.CompassManager;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author KilaBash
 * @date 2023/2/9
 * @implNote ClientCommands
 */
@OnlyIn(Dist.CLIENT)
public class ClientCommands {


    public static LiteralArgumentBuilder createLiteral(String command) {
        throw new AssertionError();
    }

    @SuppressWarnings("unchecked")
    public static <S> List<LiteralArgumentBuilder<S>> createClientCommands() {
        return List.of(
                (LiteralArgumentBuilder<S>) createLiteral("ldlib_client").then(createLiteral("reload_shader")
                        .executes(context -> {
                            Shaders.reload();
                            ShaderManager.getInstance().reload();
                            return 1;
                        })),
                (LiteralArgumentBuilder<S>) createLiteral("compass").then(createLiteral("dev_mode")
                        .then(Commands.argument("mode", BoolArgumentType.bool())
                                .executes(context -> {
                                    CompassManager.INSTANCE.devMode = BoolArgumentType.getBool(context, "mode");
                                    return 1;
                                })))
        );
    }
}