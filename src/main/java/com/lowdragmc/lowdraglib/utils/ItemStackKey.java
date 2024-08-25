package com.lowdragmc.lowdraglib.utils;

import java.util.Arrays;
import net.minecraft.world.item.ItemStack;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/ItemStackKey.class */
public final class ItemStackKey {
    private final ItemStack[] itemStack;
    private final int hashCode = makeHashCode();

    public ItemStackKey(ItemStack... itemStack) {
        this.itemStack = (ItemStack[]) Arrays.stream(itemStack).map(item -> {
            ItemStack copied = item.m_41777_();
            copied.m_41764_(1);
            return copied;
        }).toArray(x$0 -> {
            return new ItemStack[x$0];
        });
    }

    public ItemStack[] getItemStack() {
        return this.itemStack;
    }

    public boolean equals(Object o) {
        ItemStack[] itemStackArr;
        if (this == o) {
            return true;
        }
        if (o instanceof ItemStackKey) {
            ItemStackKey that = (ItemStackKey) o;
            if (that.itemStack.length == this.itemStack.length) {
                for (ItemStack a : that.itemStack) {
                    if (Arrays.stream(this.itemStack).noneMatch(b -> {
                        return ItemStack.m_41728_(a, b);
                    })) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return this.hashCode;
    }

    private int makeHashCode() {
        ItemStack[] itemStackArr;
        int itemsHash = 0;
        for (ItemStack stack : this.itemStack) {
            itemsHash = itemsHash + stack.m_41720_().hashCode() + stack.m_41773_() + (stack.m_41783_() == null ? 0 : stack.m_41783_().hashCode());
        }
        return itemsHash;
    }

    public String toString() {
        return Arrays.toString(this.itemStack);
    }
}
