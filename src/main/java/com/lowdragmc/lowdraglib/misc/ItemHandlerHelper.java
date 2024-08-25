package com.lowdragmc.lowdraglib.misc;

import net.minecraft.world.item.ItemStack;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/misc/ItemHandlerHelper.class */
public class ItemHandlerHelper {
    public static boolean canItemStacksStack(ItemStack first, ItemStack second) {
        if (!first.m_41619_() && first.m_41656_(second) && first.m_41782_() == second.m_41782_()) {
            return !first.m_41782_() || first.m_41783_().equals(second.m_41783_());
        }
        return false;
    }

    public static ItemStack copyStackWithSize(ItemStack stack, int size) {
        if (size == 0) {
            return ItemStack.f_41583_;
        }
        ItemStack copy = stack.m_41777_();
        copy.m_41764_(size);
        return copy;
    }
}
