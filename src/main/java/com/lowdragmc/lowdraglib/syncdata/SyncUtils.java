package com.lowdragmc.lowdraglib.syncdata;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/SyncUtils.class */
public class SyncUtils {
    public static boolean isChanged(@NotNull Object oldValue, @NotNull Object newValue) {
        if (oldValue instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) oldValue;
            return ((newValue instanceof ItemStack) && ItemStack.m_41728_(itemStack, (ItemStack) newValue)) ? false : true;
        } else if (!(oldValue instanceof FluidStack)) {
            return !oldValue.equals(newValue);
        } else {
            FluidStack fluidStack = (FluidStack) oldValue;
            return ((newValue instanceof FluidStack) && fluidStack.isFluidStackEqual((FluidStack) newValue)) ? false : true;
        }
    }

    public static Object copyWhenNecessary(Object value) {
        if (value instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) value;
            return itemStack.m_41777_();
        } else if (value instanceof FluidStack) {
            FluidStack fluidStack = (FluidStack) value;
            return fluidStack.copy();
        } else if (value instanceof BlockPos) {
            BlockPos blockPos = (BlockPos) value;
            return blockPos.m_7949_();
        } else {
            return value;
        }
    }

    public static boolean isArrayLikeChanged(@NotNull Object oldValue, @NotNull Object newValue, int oldSize, boolean isArray) {
        if (isArray) {
            return (Array.getLength(newValue) == oldSize && Objects.deepEquals(oldValue, newValue)) ? false : true;
        } else if (newValue instanceof Collection) {
            Collection<?> collection = (Collection) newValue;
            if (collection.size() != oldSize) {
                return true;
            }
            Object[] array = (Object[]) oldValue;
            int i = 0;
            for (Object item : collection) {
                int i2 = i;
                i++;
                Object oldItem = array[i2];
                if (oldItem == null && item != null) {
                    return true;
                }
                if (oldItem != null && item == null) {
                    return true;
                }
                if (oldItem != null && isChanged(oldItem, item)) {
                    return true;
                }
            }
            return false;
        } else {
            throw new IllegalArgumentException("Value %s is not an array or collection".formatted(new Object[]{newValue}));
        }
    }

    public static Object copyArrayLike(Object value, boolean isArray) {
        if (isArray) {
            Class<?> componentType = value.getClass().getComponentType();
            if (componentType.isPrimitive()) {
                Object result = Array.newInstance(componentType, Array.getLength(value));
                System.arraycopy(value, 0, result, 0, Array.getLength(value));
                return result;
            }
            Object[] array = (Object[]) value;
            Object[] result2 = new Object[array.length];
            for (int i = 0; i < array.length; i++) {
                result2[i] = copyWhenNecessary(array[i]);
            }
            return result2;
        } else if (value instanceof Collection) {
            Collection<?> collection = (Collection) value;
            Object[] result3 = new Object[collection.size()];
            int i2 = 0;
            for (Object o : collection) {
                int i3 = i2;
                i2++;
                result3[i3] = copyWhenNecessary(o);
            }
            return result3;
        } else {
            throw new IllegalArgumentException("Value %s is not an array or collection".formatted(new Object[]{value}));
        }
    }
}
