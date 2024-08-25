package com.lowdragmc.photon.command;

import com.lowdragmc.lowdraglib.LDLib;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/command/FxLocationArgument.class */
public class FxLocationArgument extends ResourceLocationArgument {
    public /* bridge */ /* synthetic */ Object parse(StringReader stringReader) throws CommandSyntaxException {
        return super.parse(stringReader);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (LDLib.isClient()) {
            return SharedSuggestionProvider.m_82957_(Minecraft.m_91087_().m_91098_().m_214159_("fx", arg -> {
                return arg.m_135815_().endsWith(".fx");
            }).keySet().stream().map(rl -> {
                return new ResourceLocation(rl.m_135827_(), rl.m_135815_().substring(3, rl.m_135815_().length() - 3));
            }), builder);
        }
        return super.listSuggestions(context, builder);
    }
}
