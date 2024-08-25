package com.lowdragmc.lowdraglib.test.forge;

import com.lowdragmc.lowdraglib.test.TestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/test/forge/TestBlockEntityImpl.class */
public class TestBlockEntityImpl {
    public static RegistryObject<BlockEntityType<TestBlockEntity>> TYPE;

    public static BlockEntityType<?> TYPE() {
        return (BlockEntityType) TYPE.get();
    }
}
