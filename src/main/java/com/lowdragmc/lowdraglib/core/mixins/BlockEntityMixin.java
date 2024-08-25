package com.lowdragmc.lowdraglib.core.mixins;

import com.lowdragmc.lowdraglib.networking.s2c.SPacketManagedPayload;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAsyncAutoSyncBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAutoPersistBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAutoSyncBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IManagedBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockEntity.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/BlockEntityMixin.class */
public abstract class BlockEntityMixin {
    @Inject(method = {"getUpdateTag"}, at = {@At("RETURN")})
    private void injectGetUpdateTag(CallbackInfoReturnable<CompoundTag> cir) {
        if (this instanceof IAutoSyncBlockEntity) {
            IAutoSyncBlockEntity autoSyncBlockEntity = (IAutoSyncBlockEntity) this;
            CompoundTag tag = (CompoundTag) cir.getReturnValue();
            tag.m_128365_(autoSyncBlockEntity.getSyncTag(), SPacketManagedPayload.of(autoSyncBlockEntity, true).serializeNBT());
        }
    }

    @Inject(method = {"saveAdditional"}, at = {@At("RETURN")})
    private void injectSaveAdditional(CompoundTag pTag, CallbackInfo ci) {
        if (this instanceof IAutoPersistBlockEntity) {
            IAutoPersistBlockEntity autoPersistBlockEntity = (IAutoPersistBlockEntity) this;
            autoPersistBlockEntity.saveManagedPersistentData(pTag, false);
        }
    }

    @Inject(method = {"load"}, at = {@At("RETURN")})
    private void injectLoad(CompoundTag pTag, CallbackInfo ci) {
        if (this instanceof IAutoSyncBlockEntity) {
            IAutoSyncBlockEntity autoSyncBlockEntity = (IAutoSyncBlockEntity) this;
            CompoundTag m_128423_ = pTag.m_128423_(autoSyncBlockEntity.getSyncTag());
            if (m_128423_ instanceof CompoundTag) {
                CompoundTag tag = m_128423_;
                new SPacketManagedPayload(tag).processPacket(autoSyncBlockEntity);
                return;
            }
        }
        if (this instanceof IAutoPersistBlockEntity) {
            IAutoPersistBlockEntity autoPersistBlockEntity = (IAutoPersistBlockEntity) this;
            autoPersistBlockEntity.loadManagedPersistentData(pTag);
        }
    }

    @Inject(method = {"setRemoved"}, at = {@At("RETURN")})
    private void injectSetRemoved(CallbackInfo ci) {
        if (this instanceof IAsyncAutoSyncBlockEntity) {
            IAsyncAutoSyncBlockEntity autoSyncBlockEntity = (IAsyncAutoSyncBlockEntity) this;
            autoSyncBlockEntity.onInValid();
        }
    }

    @Inject(method = {"clearRemoved"}, at = {@At("RETURN")})
    private void injectClearRemoved(CallbackInfo ci) {
        if (this instanceof IManagedBlockEntity) {
            IManagedBlockEntity managed = (IManagedBlockEntity) this;
            managed.getRootStorage().init();
            if (managed instanceof IAsyncAutoSyncBlockEntity) {
                IAsyncAutoSyncBlockEntity autoSyncBlockEntity = (IAsyncAutoSyncBlockEntity) managed;
                autoSyncBlockEntity.onValid();
            }
        }
    }
}
