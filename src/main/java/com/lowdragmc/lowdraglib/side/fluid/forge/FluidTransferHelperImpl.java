package com.lowdragmc.lowdraglib.side.fluid.forge;

import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/fluid/forge/FluidTransferHelperImpl.class */
public class FluidTransferHelperImpl {
    public static IFluidHandler toFluidHandler(final IFluidTransfer fluidTransfer) {
        return new IFluidHandler() { // from class: com.lowdragmc.lowdraglib.side.fluid.forge.FluidTransferHelperImpl.1
            public int getTanks() {
                return IFluidTransfer.this.getTanks();
            }

            @NotNull
            public FluidStack getFluidInTank(int slot) {
                return FluidHelperImpl.toFluidStack(IFluidTransfer.this.getFluidInTank(slot));
            }

            public int getTankCapacity(int slot) {
                return (int) IFluidTransfer.this.getTankCapacity(slot);
            }

            public boolean isFluidValid(int slot, @NotNull FluidStack fluidStack) {
                return IFluidTransfer.this.isFluidValid(slot, FluidHelperImpl.toFluidStack(fluidStack));
            }

            public int fill(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
                return (int) IFluidTransfer.this.fill(FluidHelperImpl.toFluidStack(fluidStack), fluidAction == IFluidHandler.FluidAction.SIMULATE);
            }

            @NotNull
            public FluidStack drain(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
                return FluidHelperImpl.toFluidStack(IFluidTransfer.this.drain(FluidHelperImpl.toFluidStack(fluidStack), fluidAction == IFluidHandler.FluidAction.SIMULATE));
            }

            @NotNull
            public FluidStack drain(int amount, IFluidHandler.FluidAction fluidAction) {
                return FluidHelperImpl.toFluidStack(IFluidTransfer.this.drain(amount, fluidAction == IFluidHandler.FluidAction.SIMULATE));
            }
        };
    }

    public static IFluidTransfer toFluidTransfer(IFluidHandler handler) {
        if (handler instanceof IFluidTransfer) {
            IFluidTransfer fluidTransfer = (IFluidTransfer) handler;
            return fluidTransfer;
        }
        return new FluidTransferWrapper(handler);
    }

    public static IFluidTransfer getFluidTransfer(Level level, BlockPos pos, @Nullable Direction direction) {
        BlockEntity blockEntity;
        BlockState state = level.m_8055_(pos);
        if (state.m_155947_() && (blockEntity = level.m_7702_(pos)) != null) {
            LazyOptional<IFluidHandler> handler = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction);
            if (handler.isPresent()) {
                return toFluidTransfer((IFluidHandler) handler.orElse((Object) null));
            }
            return null;
        }
        return null;
    }

    public static IFluidTransfer getFluidTransfer(IItemTransfer itemTransfer, int slot) {
        ItemStack itemStack = itemTransfer.getStackInSlot(slot);
        if (!itemStack.m_41619_()) {
            LazyOptional<IFluidHandlerItem> handler = itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            if (handler.isPresent()) {
                return toFluidTransfer((IFluidHandler) handler.orElse((Object) null));
            }
            return null;
        }
        return null;
    }

    public static IFluidTransfer getFluidTransfer(Player player, AbstractContainerMenu screenHandler) {
        ItemStack itemStack = screenHandler.m_142621_();
        if (!itemStack.m_41619_()) {
            LazyOptional<IFluidHandlerItem> handler = itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            if (handler.isPresent()) {
                return toFluidTransfer((IFluidHandler) handler.orElse((Object) null));
            }
            return null;
        }
        return null;
    }

    public static IFluidTransfer getFluidTransfer(Player player, InteractionHand hand) {
        ItemStack itemStack = player.m_21120_(hand);
        if (!itemStack.m_41619_()) {
            LazyOptional<IFluidHandlerItem> handler = itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            if (handler.isPresent()) {
                return toFluidTransfer((IFluidHandler) handler.orElse((Object) null));
            }
            return null;
        }
        return null;
    }

    public static IFluidTransfer getFluidTransfer(Player player, int slot) {
        ItemStack itemStack = player.m_150109_().m_8020_(slot);
        if (!itemStack.m_41619_()) {
            LazyOptional<IFluidHandlerItem> handler = itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            if (handler.isPresent()) {
                return toFluidTransfer((IFluidHandler) handler.orElse((Object) null));
            }
            return null;
        }
        return null;
    }

    public static ItemStack getContainerItem(ItemStackTransfer copyContainer, IFluidTransfer handler) {
        if (handler instanceof FluidTransferWrapper) {
            FluidTransferWrapper wrapper = (FluidTransferWrapper) handler;
            IFluidHandlerItem handler2 = wrapper.getHandler();
            if (handler2 instanceof IFluidHandlerItem) {
                IFluidHandlerItem fluidHandlerItem = handler2;
                return fluidHandlerItem.getContainer();
            }
        }
        return copyContainer.getStackInSlot(0);
    }

    public static void exportToTarget(IFluidTransfer source, int maxAmount, Predicate<com.lowdragmc.lowdraglib.side.fluid.FluidStack> filter, Level level, BlockPos pos, @Nullable Direction direction) {
        BlockEntity blockEntity;
        BlockState state = level.m_8055_(pos);
        if (state.m_155947_() && (blockEntity = level.m_7702_(pos)) != null) {
            Optional<IFluidHandler> cap = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction).resolve();
            if (cap.isPresent()) {
                IFluidHandler target = cap.get();
                for (int srcIndex = 0; srcIndex < source.getTanks(); srcIndex++) {
                    com.lowdragmc.lowdraglib.side.fluid.FluidStack currentFluid = source.getFluidInTank(srcIndex);
                    if (!currentFluid.isEmpty() && filter.test(currentFluid)) {
                        com.lowdragmc.lowdraglib.side.fluid.FluidStack toDrain = currentFluid.copy();
                        toDrain.setAmount(maxAmount);
                        int filled = target.fill(FluidHelperImpl.toFluidStack(source.drain(toDrain, true)), IFluidHandler.FluidAction.SIMULATE);
                        if (filled > 0) {
                            maxAmount -= filled;
                            com.lowdragmc.lowdraglib.side.fluid.FluidStack toDrain2 = currentFluid.copy();
                            toDrain2.setAmount(filled);
                            target.fill(FluidHelperImpl.toFluidStack(source.drain(toDrain2, false)), IFluidHandler.FluidAction.EXECUTE);
                        }
                        if (maxAmount <= 0) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void importToTarget(IFluidTransfer target, int maxAmount, Predicate<com.lowdragmc.lowdraglib.side.fluid.FluidStack> filter, Level level, BlockPos pos, @Nullable Direction direction) {
        BlockEntity blockEntity;
        BlockState state = level.m_8055_(pos);
        if (state.m_155947_() && (blockEntity = level.m_7702_(pos)) != null) {
            Optional<IFluidHandler> cap = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction).resolve();
            if (cap.isPresent()) {
                IFluidHandler source = cap.get();
                for (int srcIndex = 0; srcIndex < source.getTanks(); srcIndex++) {
                    FluidStack currentFluid = source.getFluidInTank(srcIndex);
                    if (!currentFluid.isEmpty() && filter.test(FluidHelperImpl.toFluidStack(currentFluid))) {
                        FluidStack toDrain = currentFluid.copy();
                        toDrain.setAmount(maxAmount);
                        long filled = target.fill(FluidHelperImpl.toFluidStack(source.drain(toDrain, IFluidHandler.FluidAction.SIMULATE)), true);
                        if (filled > 0) {
                            maxAmount = (int) (maxAmount - filled);
                            FluidStack toDrain2 = currentFluid.copy();
                            toDrain2.setAmount((int) filled);
                            target.fill(FluidHelperImpl.toFluidStack(source.drain(toDrain2, IFluidHandler.FluidAction.EXECUTE)), false);
                        }
                        if (maxAmount <= 0) {
                            return;
                        }
                    }
                }
            }
        }
    }
}
