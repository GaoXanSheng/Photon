package com.lowdragmc.photon.client.fx;

import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author KilaBash
 * @date 2023/6/5
 * @implNote BlockEffect
 */
@OnlyIn(Dist.CLIENT)
public class BlockEffect extends FXEffect {
    public static Map<BlockPos, List<BlockEffect>> CACHE = new HashMap<>();
    public final BlockPos pos;
    @Setter
    private boolean checkState;
    // runtime
    private BlockState lastState;

    public BlockEffect(FX fx, Level level, BlockPos pos) {
        super(fx, level);
        this.pos = pos;
    }

    @Override
    public boolean updateEmitter(IParticleEmitter emitter) {
        if (!level.isLoaded(pos) || lastState.getBlock() != level.getBlockState(pos).getBlock() || (checkState && level.getBlockState(pos) != lastState)) {
            emitter.remove(forcedDeath);
            return forcedDeath;
        }
        return false;
    }

    @Override
    public void start() {
        this.emitters.clear();
        this.emitters.addAll(fx.generateEmitters());
        if (this.emitters.isEmpty()) return;
        if (!allowMulti) {
            var effects = CACHE.computeIfAbsent(pos, p -> new ArrayList<>());
            var iter = effects.iterator();
            while (iter.hasNext()) {
                var effect = iter.next();
                boolean removed = false;
                if (effect.emitters.stream().noneMatch(e -> e.self().isAlive())) {
                    iter.remove();
                    removed = true;
                }
                if (effect.fx.equals(fx) && !removed) {
                    return;
                }
            }
            effects.add(this);
        }
        var realPos= new Vector3(pos).add(xOffset + 0.5, yOffset + 0.5, zOffset + 0.5);
        for (var emitter : emitters) {
            if (!emitter.isSubEmitter()) {
                emitter.reset();
                emitter.self().setDelay(delay);
                emitter.emmitToLevel(this, level, realPos.x, realPos.y, realPos.z, xRotation, yRotation, zRotation);
            }
        }
        lastState = level.getBlockState(pos);
    }

}
