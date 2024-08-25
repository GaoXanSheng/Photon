package com.lowdragmc.lowdraglib.utils;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.scene.ParticleManager;
import com.lowdragmc.lowdraglib.core.mixins.accessor.EntityAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.material.FluidState;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/TrackedDummyWorld.class */
public class TrackedDummyWorld extends DummyWorld {
    private Predicate<BlockPos> renderFilter;
    public final Level proxyWorld;
    public final Map<BlockPos, BlockInfo> renderedBlocks;
    public final Map<BlockPos, BlockEntity> blockEntities;
    public final Map<Integer, Entity> entities;
    public final Vector3 minPos;
    public final Vector3 maxPos;

    public void setRenderFilter(Predicate<BlockPos> renderFilter) {
        this.renderFilter = renderFilter;
    }

    public TrackedDummyWorld() {
        super(Minecraft.m_91087_().f_91073_);
        this.renderedBlocks = new HashMap();
        this.blockEntities = new HashMap();
        this.entities = new Int2ObjectArrayMap();
        this.minPos = new Vector3(2.147483647E9d, 2.147483647E9d, 2.147483647E9d);
        this.maxPos = new Vector3(-2.147483648E9d, -2.147483648E9d, -2.147483648E9d);
        this.proxyWorld = null;
    }

    public TrackedDummyWorld(Level world) {
        super(world);
        this.renderedBlocks = new HashMap();
        this.blockEntities = new HashMap();
        this.entities = new Int2ObjectArrayMap();
        this.minPos = new Vector3(2.147483647E9d, 2.147483647E9d, 2.147483647E9d);
        this.maxPos = new Vector3(-2.147483648E9d, -2.147483648E9d, -2.147483648E9d);
        this.proxyWorld = world;
    }

    public void clear() {
        this.renderedBlocks.clear();
        this.blockEntities.clear();
        this.entities.clear();
    }

    public Map<BlockPos, BlockInfo> getRenderedBlocks() {
        return this.renderedBlocks;
    }

    public void addBlocks(Map<BlockPos, BlockInfo> renderedBlocks) {
        renderedBlocks.forEach(this::addBlock);
    }

    public void addBlock(BlockPos pos, BlockInfo blockInfo) {
        if (blockInfo.getBlockState().m_60734_() == Blocks.f_50016_) {
            return;
        }
        this.renderedBlocks.put(pos, blockInfo);
        this.blockEntities.remove(pos);
        this.minPos.x = Math.min(this.minPos.x, pos.m_123341_());
        this.minPos.y = Math.min(this.minPos.y, pos.m_123342_());
        this.minPos.z = Math.min(this.minPos.z, pos.m_123343_());
        this.maxPos.x = Math.max(this.maxPos.x, pos.m_123341_());
        this.maxPos.y = Math.max(this.maxPos.y, pos.m_123342_());
        this.maxPos.z = Math.max(this.maxPos.z, pos.m_123343_());
    }

    public BlockInfo removeBlock(BlockPos pos) {
        this.blockEntities.remove(pos);
        return this.renderedBlocks.remove(pos);
    }

    public void setInnerBlockEntity(@Nonnull BlockEntity pBlockEntity) {
        this.blockEntities.put(pBlockEntity.m_58899_(), pBlockEntity);
    }

    @Override // com.lowdragmc.lowdraglib.utils.DummyWorld
    public void m_151523_(@Nonnull BlockEntity pBlockEntity) {
        this.blockEntities.put(pBlockEntity.m_58899_(), pBlockEntity);
    }

    @Override // com.lowdragmc.lowdraglib.utils.DummyWorld
    public boolean m_6933_(@Nonnull BlockPos pos, @Nonnull BlockState state, int a, int b) {
        this.renderedBlocks.put(pos, BlockInfo.fromBlockState(state));
        this.blockEntities.remove(pos);
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.utils.DummyWorld
    public BlockEntity m_7702_(@Nonnull BlockPos pos) {
        if (this.renderFilter == null || this.renderFilter.test(pos)) {
            return this.proxyWorld != null ? this.proxyWorld.m_7702_(pos) : this.blockEntities.computeIfAbsent(pos, p -> {
                return this.renderedBlocks.getOrDefault(p, BlockInfo.EMPTY).getBlockEntity(this, p);
            });
        }
        return null;
    }

    @Override // com.lowdragmc.lowdraglib.utils.DummyWorld
    public BlockState m_8055_(@Nonnull BlockPos pos) {
        if (this.renderFilter == null || this.renderFilter.test(pos)) {
            return this.proxyWorld != null ? this.proxyWorld.m_8055_(pos) : this.renderedBlocks.getOrDefault(pos, BlockInfo.EMPTY).getBlockState();
        }
        return Blocks.f_50016_.m_49966_();
    }

    public boolean m_7967_(Entity entity) {
        EquipmentSlot[] values;
        ((EntityAccessor) entity).invokeSetLevel(this);
        if (entity instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) entity;
            itemFrame.m_31805_(withUnsafeNBTDiscarded(itemFrame.m_31822_()));
        }
        if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                armorStand.m_8061_(equipmentSlot, withUnsafeNBTDiscarded(armorStand.m_6844_(equipmentSlot)));
            }
        }
        this.entities.put(Integer.valueOf(entity.m_19879_()), entity);
        return true;
    }

    public static ItemStack withUnsafeNBTDiscarded(ItemStack stack) {
        if (stack.m_41783_() == null) {
            return stack;
        }
        ItemStack copy = stack.m_41777_();
        Stream filter = stack.m_41783_().m_128431_().stream().filter(TrackedDummyWorld::isUnsafeItemNBTKey);
        Objects.requireNonNull(copy);
        filter.forEach(this::m_41749_);
        if (copy.m_41783_().m_128456_()) {
            copy.m_41751_((CompoundTag) null);
        }
        return copy;
    }

    public static boolean isUnsafeItemNBTKey(String name) {
        if (name.equals("StoredEnchantments") || name.equals("Enchantments") || name.contains("Potion") || name.contains("Damage") || name.equals("display")) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lowdragmc.lowdraglib.utils.DummyWorld
    public LevelEntityGetter<Entity> m_142646_() {
        return super.m_142646_();
    }

    @Override // com.lowdragmc.lowdraglib.utils.DummyWorld
    public Entity m_6815_(int id) {
        for (Entity entity : this.entities.values()) {
            if (entity.m_19879_() == id && entity.m_6084_()) {
                return entity;
            }
        }
        return super.m_6815_(id);
    }

    public Vector3 getSize() {
        return new Vector3((this.maxPos.x - this.minPos.x) + 1.0d, (this.maxPos.y - this.minPos.y) + 1.0d, (this.maxPos.z - this.minPos.z) + 1.0d);
    }

    public Vector3 getMinPos() {
        return this.minPos;
    }

    public Vector3 getMaxPos() {
        return this.maxPos;
    }

    @Override // com.lowdragmc.lowdraglib.utils.DummyWorld
    public ChunkSource m_7726_() {
        return this.proxyWorld == null ? super.m_7726_() : this.proxyWorld.m_7726_();
    }

    @Override // com.lowdragmc.lowdraglib.utils.DummyWorld
    public FluidState m_6425_(BlockPos pPos) {
        return this.proxyWorld == null ? super.m_6425_(pPos) : this.proxyWorld.m_6425_(pPos);
    }

    public int m_6171_(@Nonnull BlockPos blockPos, @Nonnull ColorResolver colorResolver) {
        return this.proxyWorld == null ? super.m_6171_(blockPos, colorResolver) : this.proxyWorld.m_6171_(blockPos, colorResolver);
    }

    @Override // com.lowdragmc.lowdraglib.utils.DummyWorld
    @Nonnull
    public Holder<Biome> m_204166_(@Nonnull BlockPos pos) {
        return this.proxyWorld == null ? super.m_204166_(pos) : this.proxyWorld.m_204166_(pos);
    }

    @Override // com.lowdragmc.lowdraglib.utils.DummyWorld
    public void setParticleManager(ParticleManager particleManager) {
        super.setParticleManager(particleManager);
        Level level = this.proxyWorld;
        if (level instanceof DummyWorld) {
            DummyWorld dummyWorld = (DummyWorld) level;
            dummyWorld.setParticleManager(particleManager);
        }
    }

    @Override // com.lowdragmc.lowdraglib.utils.DummyWorld
    @Nullable
    public ParticleManager getParticleManager() {
        ParticleManager particleManager = super.getParticleManager();
        if (particleManager == null) {
            Level level = this.proxyWorld;
            if (level instanceof DummyWorld) {
                DummyWorld dummyWorld = (DummyWorld) level;
                return dummyWorld.getParticleManager();
            }
        }
        return particleManager;
    }

    public void tickWorld() {
        Iterator<Entity> iter = this.entities.values().iterator();
        while (iter.hasNext()) {
            Entity entity = iter.next();
            entity.f_19797_++;
            entity.m_146867_();
            entity.m_8119_();
            if (entity.m_20186_() <= -0.5d) {
                entity.m_146870_();
            }
            if (!entity.m_6084_()) {
                iter.remove();
            }
        }
        for (Map.Entry<BlockPos, BlockInfo> entry : this.renderedBlocks.entrySet()) {
            BlockState blockState = entry.getValue().getBlockState();
            BlockEntity blockEntity = m_7702_(entry.getKey());
            if (blockEntity != null && blockEntity.m_58903_().m_155262_(blockState)) {
                try {
                    BlockEntityTicker ticker = blockState.m_155944_(this, blockEntity.m_58903_());
                    if (ticker != null) {
                        ticker.m_155252_(this, entry.getKey(), blockState, blockEntity);
                    }
                } catch (Exception e) {
                    LDLib.LOGGER.error("error while update DummyWorld tick, pos {} type {}", new Object[]{entry.getKey(), blockEntity.m_58903_(), e});
                }
            }
        }
    }

    public List<Entity> geAllEntities() {
        ArrayList<Entity> entities = new ArrayList<>((Collection<? extends Entity>) this.entities.values());
        Level level = this.proxyWorld;
        if (level instanceof TrackedDummyWorld) {
            TrackedDummyWorld trackedDummyWorld = (TrackedDummyWorld) level;
            entities.addAll(trackedDummyWorld.geAllEntities());
        }
        return entities;
    }
}
