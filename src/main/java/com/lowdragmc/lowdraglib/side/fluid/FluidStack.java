package com.lowdragmc.lowdraglib.side.fluid;

import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/fluid/FluidStack.class */
public class FluidStack {
    private static final FluidStack EMPTY = new FluidStack(Fluids.f_76191_, 0, null);
    private boolean isEmpty;
    private long amount;
    private CompoundTag tag;
    private Fluid fluid;

    public static FluidStack empty() {
        return EMPTY;
    }

    public static FluidStack create(Fluid fluid, long amount, CompoundTag nbt) {
        return new FluidStack(fluid, amount, nbt);
    }

    public static FluidStack create(Fluid fluid, long amount) {
        return create(fluid, amount, null);
    }

    public static FluidStack create(FluidStack stack, long amount) {
        return create(stack.getFluid(), amount, stack.getTag());
    }

    public void setFluid(Fluid fluid) {
        this.fluid = fluid;
    }

    private FluidStack(Fluid fluid, long amount, CompoundTag nbt) {
        this.fluid = fluid;
        this.amount = amount;
        if (nbt != null) {
            this.tag = nbt.m_6426_();
        }
        updateEmpty();
    }

    public static FluidStack loadFromTag(CompoundTag nbt) {
        if (nbt == null) {
            return EMPTY;
        }
        if (!nbt.m_128425_("FluidName", 8)) {
            return EMPTY;
        }
        ResourceLocation fluidName = new ResourceLocation(nbt.m_128461_("FluidName"));
        Fluid fluid = (Fluid) Registry.f_122822_.m_7745_(fluidName);
        if (fluid == Fluids.f_76191_) {
            return EMPTY;
        }
        FluidStack stack = create(fluid, nbt.m_128454_("Amount"));
        if (nbt.m_128425_("Tag", 10)) {
            stack.setTag(nbt.m_128469_("Tag"));
        }
        return stack;
    }

    public static FluidStack readFromBuf(FriendlyByteBuf buf) {
        Fluid fluid = (Fluid) Registry.f_122822_.m_7745_(new ResourceLocation(buf.m_130277_()));
        int amount = buf.m_130242_();
        CompoundTag tag = buf.m_130260_();
        return fluid == Fluids.f_76191_ ? EMPTY : create(fluid, amount, tag);
    }

    protected void updateEmpty() {
        this.isEmpty = getRawFluid() == Fluids.f_76191_ || this.amount <= 0;
    }

    public CompoundTag saveToTag(CompoundTag nbt) {
        nbt.m_128359_("FluidName", Registry.f_122822_.m_7981_(this.fluid).toString());
        nbt.m_128356_("Amount", this.amount);
        if (this.tag != null) {
            nbt.m_128365_("Tag", this.tag);
        }
        return nbt;
    }

    public void writeToBuf(FriendlyByteBuf buf) {
        buf.m_130070_(Registry.f_122822_.m_7981_(this.fluid).toString());
        buf.m_130103_(getAmount());
        buf.m_130079_(this.tag);
    }

    public Fluid getFluid() {
        return this.isEmpty ? Fluids.f_76191_ : this.fluid;
    }

    public final Fluid getRawFluid() {
        return this.fluid;
    }

    public long getAmount() {
        if (this.isEmpty) {
            return 0L;
        }
        return this.amount;
    }

    public void setAmount(long amount) {
        if (this.fluid == Fluids.f_76191_) {
            throw new IllegalStateException("Can't modify the empty stack.");
        }
        this.amount = amount;
        updateEmpty();
    }

    public boolean hasTag() {
        return this.tag != null;
    }

    public CompoundTag getTag() {
        return this.tag;
    }

    public void setTag(CompoundTag tag) {
        if (getRawFluid() == Fluids.f_76191_) {
            throw new IllegalStateException("Can't modify the empty stack.");
        }
        this.tag = tag;
    }

    public boolean isEmpty() {
        return getAmount() == 0 || this == empty();
    }

    public Component getDisplayName() {
        return FluidHelper.getDisplayName(this);
    }

    public FluidStack copy() {
        return create(getFluid(), getAmount(), getTag());
    }

    public FluidStack copy(long amount) {
        return create(getFluid(), amount, getTag());
    }

    public boolean isFluidEqual(@Nonnull FluidStack other) {
        return getFluid() == other.getFluid() && Objects.equals(getTag(), other.getTag());
    }

    public boolean isFluidStackEqual(@Nonnull FluidStack other) {
        return isFluidEqual(other) && getAmount() == other.getAmount();
    }

    public void grow(long amount) {
        setAmount(getAmount() + amount);
    }

    public void shrink(long amount) {
        setAmount(getAmount() - amount);
    }
}
