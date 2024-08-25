package com.lowdragmc.photon.command;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.photon.PhotonNetworking;
import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/command/EntityEffectCommand.class */
public class EntityEffectCommand extends EffectCommand {
    protected List<Entity> entities;
    private int[] ids = new int[0];

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createServerCommand() {
        return Commands.m_82127_("entity").then(Commands.m_82129_("entities", EntityArgument.m_91460_()).executes(c -> {
            return execute(c, 0);
        }).then(Commands.m_82129_("offset", Vec3Argument.m_120847_(false)).executes(c2 -> {
            return execute(c2, 1);
        }).then(Commands.m_82129_("rotation", Vec3Argument.m_120847_(false)).executes(c3 -> {
            return execute(c3, 2);
        }).then(Commands.m_82129_("delay", IntegerArgumentType.integer(0)).executes(c4 -> {
            return execute(c4, 3);
        }).then(Commands.m_82129_("force death", BoolArgumentType.bool()).executes(c5 -> {
            return execute(c5, 4);
        }).then(Commands.m_82129_("allow multi", BoolArgumentType.bool()).executes(c6 -> {
            return execute(c6, 5);
        })))))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int execute(CommandContext<CommandSourceStack> context, int feature) throws CommandSyntaxException {
        EntityEffectCommand command = new EntityEffectCommand();
        command.setLocation(ResourceLocationArgument.m_107011_(context, "location"));
        command.setEntities(EntityArgument.m_91461_(context, "entities").stream().map(e -> {
            return e;
        }).toList());
        if (feature >= 1) {
            command.setOffset(Vec3Argument.m_120844_(context, "offset"));
        }
        if (feature >= 2) {
            command.setRotation(Vec3Argument.m_120844_(context, "rotation"));
        }
        if (feature >= 3) {
            command.setDelay(IntegerArgumentType.getInteger(context, "delay"));
        }
        if (feature >= 4) {
            command.setForcedDeath(BoolArgumentType.getBool(context, "force death"));
        }
        if (feature >= 5) {
            command.setAllowMulti(BoolArgumentType.getBool(context, "allow multi"));
        }
        PhotonNetworking.NETWORK.sendToAll(command);
        return 1;
    }

    @Override // com.lowdragmc.photon.command.EffectCommand, com.lowdragmc.lowdraglib.networking.IPacket
    public void encode(FriendlyByteBuf buf) {
        super.encode(buf);
        buf.m_130130_(this.entities.size());
        for (Entity entity : this.entities) {
            buf.m_130130_(entity.m_19879_());
        }
    }

    @Override // com.lowdragmc.photon.command.EffectCommand, com.lowdragmc.lowdraglib.networking.IPacket
    public void decode(FriendlyByteBuf buf) {
        super.decode(buf);
        this.ids = new int[buf.m_130242_()];
        for (int i = 0; i < this.ids.length; i++) {
            this.ids[i] = buf.m_130242_();
        }
    }

    @Override // com.lowdragmc.lowdraglib.networking.IPacket
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        int[] iArr;
        Level level = handler.getLevel();
        FX fx = FXHelper.getFX(this.location);
        if (fx != null) {
            for (int id : this.ids) {
                Entity entity = level.m_6815_(id);
                if (entity != null) {
                    EntityEffect effect = new EntityEffect(fx, level, entity);
                    effect.setOffset(this.offset.f_82479_, this.offset.f_82480_, this.offset.f_82481_);
                    effect.setRotation(this.rotation.f_82479_, this.offset.f_82480_, this.offset.f_82481_);
                    effect.setDelay(this.delay);
                    effect.setForcedDeath(this.forcedDeath);
                    effect.setAllowMulti(this.allowMulti);
                    effect.start();
                }
            }
        }
    }
}
