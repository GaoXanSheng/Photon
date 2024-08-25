package com.lowdragmc.lowdraglib.side.fluid;

import com.google.common.base.Preconditions;
import com.lowdragmc.lowdraglib.misc.BucketPickupTransfer;
import com.lowdragmc.lowdraglib.misc.ContainerTransfer;
import com.lowdragmc.lowdraglib.misc.FluidBlockTransfer;
import com.lowdragmc.lowdraglib.misc.ItemHandlerHelper;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.misc.LiquidBlockContainerTransfer;
import com.lowdragmc.lowdraglib.side.fluid.forge.FluidTransferHelperImpl;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.side.item.ItemTransferHelper;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/fluid/FluidTransferHelper.class */
public class FluidTransferHelper {
    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static IFluidTransfer getFluidTransfer(Level level, BlockPos pos, @Nullable Direction direction) {
        return FluidTransferHelperImpl.getFluidTransfer(level, pos, direction);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static IFluidTransfer getFluidTransfer(IItemTransfer itemTransfer, int slot) {
        return FluidTransferHelperImpl.getFluidTransfer(itemTransfer, slot);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static IFluidTransfer getFluidTransfer(Player player, AbstractContainerMenu screenHandler) {
        return FluidTransferHelperImpl.getFluidTransfer(player, screenHandler);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static IFluidTransfer getFluidTransfer(Player player, InteractionHand hand) {
        return FluidTransferHelperImpl.getFluidTransfer(player, hand);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static IFluidTransfer getFluidTransfer(Player player, int slot) {
        return FluidTransferHelperImpl.getFluidTransfer(player, slot);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static void exportToTarget(IFluidTransfer source, int maxAmount, Predicate<FluidStack> filter, Level level, BlockPos pos, @Nullable Direction direction) {
        FluidTransferHelperImpl.exportToTarget(source, maxAmount, filter, level, pos, direction);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static void importToTarget(IFluidTransfer target, int maxAmount, Predicate<FluidStack> filter, Level level, BlockPos pos, @Nullable Direction direction) {
        FluidTransferHelperImpl.importToTarget(target, maxAmount, filter, level, pos, direction);
    }

    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static ItemStack getContainerItem(ItemStackTransfer copyContainer, IFluidTransfer handler) {
        return FluidTransferHelperImpl.getContainerItem(copyContainer, handler);
    }

    @Deprecated
    public static IFluidTransfer getFluidTransfer(@NotNull ItemStack itemStack) {
        return getFluidTransfer(new ItemStackTransfer(itemStack), 0);
    }

    public static boolean interactWithFluidHandler(@Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction side) {
        Preconditions.checkNotNull(level);
        Preconditions.checkNotNull(pos);
        IFluidTransfer handler = getFluidTransfer(level, pos, side);
        if (handler != null) {
            return interactWithFluidHandler(player, hand, handler);
        }
        return false;
    }

    public static boolean interactWithFluidHandler(@Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull IFluidTransfer handler) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(hand);
        Preconditions.checkNotNull(handler);
        ItemStack heldItem = player.m_21120_(hand);
        if (!heldItem.m_41619_()) {
            ContainerTransfer playerInventory = new ContainerTransfer(player.m_150109_());
            FluidActionResult fluidActionResult = tryFillContainerAndStow(heldItem, handler, playerInventory, Integer.MAX_VALUE, player, true);
            if (!fluidActionResult.isSuccess()) {
                fluidActionResult = tryEmptyContainerAndStow(heldItem, handler, playerInventory, Integer.MAX_VALUE, player, true);
            }
            if (fluidActionResult.isSuccess()) {
                player.m_21008_(hand, fluidActionResult.getResult());
                return true;
            }
            return false;
        }
        return false;
    }

    @Nonnull
    public static FluidActionResult tryFillContainer(@Nonnull ItemStack container, IFluidTransfer fluidSource, int maxAmount, @Nullable Player player, boolean doFill) {
        SoundEvent soundevent;
        ItemStack containerCopy = ItemTransferHelper.copyStackWithSize(container, 1);
        ItemStackTransfer itemStorage = new ItemStackTransfer(containerCopy);
        IFluidTransfer handler = getFluidTransfer(itemStorage, 0);
        if (handler != null) {
            FluidStack simulatedTransfer = tryFluidTransfer(handler, fluidSource, maxAmount, false);
            if (!simulatedTransfer.isEmpty()) {
                if (doFill) {
                    tryFluidTransfer(handler, fluidSource, maxAmount, true);
                    if (player != null && (soundevent = FluidHelper.getFillSound(simulatedTransfer)) != null) {
                        player.f_19853_.m_6263_((Player) null, player.m_20185_(), player.m_20186_() + 0.5d, player.m_20189_(), soundevent, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                } else {
                    handler.fill(simulatedTransfer, true);
                }
                return new FluidActionResult(getContainerItem(itemStorage, handler));
            }
        }
        return FluidActionResult.FAILURE;
    }

    @Nonnull
    public static FluidActionResult tryEmptyContainer(@Nonnull ItemStack container, IFluidTransfer fluidDestination, int maxAmount, @Nullable Player player, boolean doDrain) {
        SoundEvent soundevent;
        ItemStack containerCopy = ItemTransferHelper.copyStackWithSize(container, 1);
        ItemStackTransfer itemStorage = new ItemStackTransfer(containerCopy);
        IFluidTransfer handler = getFluidTransfer(itemStorage, 0);
        if (handler != null) {
            FluidStack transfer = tryFluidTransfer(fluidDestination, handler, maxAmount, doDrain);
            if (transfer.isEmpty()) {
                return FluidActionResult.FAILURE;
            }
            if (doDrain && player != null && (soundevent = FluidHelper.getEmptySound(transfer)) != null) {
                player.f_19853_.m_6263_((Player) null, player.m_20185_(), player.m_20186_() + 0.5d, player.m_20189_(), soundevent, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return new FluidActionResult(getContainerItem(itemStorage, handler));
        }
        return FluidActionResult.FAILURE;
    }

    @Nonnull
    public static FluidActionResult tryFillContainerAndStow(@Nonnull ItemStack container, IFluidTransfer fluidSource, IItemTransfer inventory, int maxAmount, @Nullable Player player, boolean doFill) {
        if (container.m_41619_()) {
            return FluidActionResult.FAILURE;
        }
        if (player != null && player.m_150110_().f_35937_) {
            if (tryFillContainer(container, fluidSource, maxAmount, player, doFill).isSuccess()) {
                return new FluidActionResult(container);
            }
        } else if (container.m_41613_() == 1) {
            FluidActionResult filledReal = tryFillContainer(container, fluidSource, maxAmount, player, doFill);
            if (filledReal.isSuccess()) {
                return filledReal;
            }
        } else {
            FluidActionResult filledSimulated = tryFillContainer(container, fluidSource, maxAmount, player, false);
            if (filledSimulated.isSuccess() && (ItemTransferHelper.insertItemStacked(inventory, filledSimulated.getResult(), true).m_41619_() || player != null)) {
                ItemStack remainder = ItemTransferHelper.insertItemStacked(inventory, tryFillContainer(container, fluidSource, maxAmount, player, doFill).getResult(), !doFill);
                if (!remainder.m_41619_() && player != null && doFill) {
                    ItemTransferHelper.giveItemToPlayer(player, remainder);
                }
                ItemStack containerCopy = container.m_41777_();
                containerCopy.m_41774_(1);
                return new FluidActionResult(containerCopy);
            }
        }
        return FluidActionResult.FAILURE;
    }

    @Nonnull
    public static FluidActionResult tryEmptyContainerAndStow(@Nonnull ItemStack container, IFluidTransfer fluidDestination, IItemTransfer inventory, int maxAmount, @Nullable Player player, boolean doDrain) {
        if (container.m_41619_()) {
            return FluidActionResult.FAILURE;
        }
        if (player != null && player.m_150110_().f_35937_) {
            if (tryEmptyContainer(container, fluidDestination, maxAmount, player, doDrain).isSuccess()) {
                return new FluidActionResult(container);
            }
        } else if (container.m_41613_() == 1) {
            FluidActionResult emptiedReal = tryEmptyContainer(container, fluidDestination, maxAmount, player, doDrain);
            if (emptiedReal.isSuccess()) {
                return emptiedReal;
            }
        } else {
            FluidActionResult emptiedSimulated = tryEmptyContainer(container, fluidDestination, maxAmount, player, false);
            if (emptiedSimulated.isSuccess() && (ItemTransferHelper.insertItemStacked(inventory, emptiedSimulated.getResult(), true).m_41619_() || player != null)) {
                ItemStack remainder = ItemTransferHelper.insertItemStacked(inventory, tryEmptyContainer(container, fluidDestination, maxAmount, player, doDrain).getResult(), !doDrain);
                if (!remainder.m_41619_() && player != null && doDrain) {
                    ItemTransferHelper.giveItemToPlayer(player, remainder);
                }
                ItemStack containerCopy = container.m_41777_();
                containerCopy.m_41774_(1);
                return new FluidActionResult(containerCopy);
            }
        }
        return FluidActionResult.FAILURE;
    }

    @Nonnull
    public static FluidStack tryFluidTransfer(IFluidTransfer fluidDestination, IFluidTransfer fluidSource, int maxAmount, boolean doTransfer) {
        FluidStack drainable = fluidSource.drain(maxAmount, true);
        if (!drainable.isEmpty()) {
            return tryFluidTransfer_Internal(fluidDestination, fluidSource, drainable, doTransfer);
        }
        return FluidStack.empty();
    }

    @Nonnull
    public static FluidStack tryFluidTransfer(IFluidTransfer fluidDestination, IFluidTransfer fluidSource, FluidStack resource, boolean doTransfer) {
        FluidStack drainable = fluidSource.drain(resource, true);
        if (!drainable.isEmpty() && resource.isFluidEqual(drainable)) {
            return tryFluidTransfer_Internal(fluidDestination, fluidSource, drainable, doTransfer);
        }
        return FluidStack.empty();
    }

    @Nonnull
    private static FluidStack tryFluidTransfer_Internal(IFluidTransfer fluidDestination, IFluidTransfer fluidSource, FluidStack drainable, boolean doTransfer) {
        long fillableAmount = fluidDestination.fill(drainable, true);
        if (fillableAmount > 0) {
            drainable.setAmount(fillableAmount);
            FluidStack drained = fluidSource.drain(drainable, !doTransfer);
            if (!drained.isEmpty()) {
                drained.setAmount(fluidDestination.fill(drained, !doTransfer));
                return drained;
            }
        }
        return FluidStack.empty();
    }

    public static FluidStack getFluidContained(@Nonnull ItemStack container) {
        IFluidTransfer handler;
        if (!container.m_41619_() && (handler = getFluidTransfer(new ItemStackTransfer(ItemTransferHelper.copyStackWithSize(container, 1)), 0)) != null) {
            FluidStack contained = handler.drain(2147483647L, true);
            if (!contained.isEmpty()) {
                return contained;
            }
            return null;
        }
        return null;
    }

    @Nonnull
    public static FluidActionResult tryPickUpFluid(@Nonnull ItemStack emptyContainer, @Nullable Player playerIn, Level level, BlockPos pos, Direction side) {
        IFluidTransfer targetFluidHandler;
        if (emptyContainer.m_41619_() || level == null || pos == null) {
            return FluidActionResult.FAILURE;
        }
        BlockState state = level.m_8055_(pos);
        LiquidBlock m_60734_ = state.m_60734_();
        if (m_60734_ instanceof LiquidBlock) {
            LiquidBlock fluidBlock = m_60734_;
            targetFluidHandler = new FluidBlockTransfer(fluidBlock, level, pos);
        } else if (m_60734_ instanceof BucketPickup) {
            targetFluidHandler = new BucketPickupTransfer((BucketPickup) m_60734_, level, pos);
        } else {
            IFluidTransfer fluidHandler = getFluidTransfer(level, pos, side);
            if (fluidHandler != null) {
                return FluidActionResult.FAILURE;
            }
            targetFluidHandler = fluidHandler;
        }
        return tryFillContainer(emptyContainer, targetFluidHandler, Integer.MAX_VALUE, playerIn, true);
    }

    @Nonnull
    public static FluidActionResult tryPlaceFluid(@Nullable Player player, Level level, InteractionHand hand, BlockPos pos, @Nonnull ItemStack container, FluidStack resource) {
        ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1);
        ItemStackTransfer itemStorage = new ItemStackTransfer(containerCopy);
        IFluidTransfer handler = getFluidTransfer(itemStorage, 0);
        if (handler != null && tryPlaceFluid(player, level, hand, pos, handler, resource)) {
            return new FluidActionResult(itemStorage.getStackInSlot(0));
        }
        return FluidActionResult.FAILURE;
    }

    public static boolean tryPlaceFluid(@Nullable Player player, Level level, InteractionHand hand, BlockPos pos, IFluidTransfer fluidSource, FluidStack resource) {
        Fluid fluid;
        IFluidTransfer handler;
        if (level == null || pos == null || (fluid = resource.getFluid()) == Fluids.f_76191_ || !FluidHelper.canBePlacedInWorld(resource, level, pos) || fluidSource.drain(resource, true).isEmpty()) {
            return false;
        }
        BlockPlaceContext context = new BlockPlaceContext(new UseOnContext(player, hand, new BlockHitResult(Vec3.f_82478_, Direction.UP, pos, false)));
        BlockState destBlockState = level.m_8055_(pos);
        Material destMaterial = destBlockState.m_60767_();
        boolean isDestNonSolid = !destMaterial.m_76333_();
        boolean isDestReplaceable = destBlockState.m_60629_(context);
        boolean canDestContainFluid = (destBlockState.m_60734_() instanceof LiquidBlockContainer) && destBlockState.m_60734_().m_6044_(level, pos, destBlockState, fluid);
        if (!level.m_46859_(pos) && !isDestNonSolid && !isDestReplaceable && !canDestContainFluid) {
            return false;
        }
        if (level.m_6042_().f_63857_() && FluidHelper.doesVaporize(resource, level, pos)) {
            FluidStack result = fluidSource.drain(resource, false);
            if (!result.isEmpty()) {
                return true;
            }
            return false;
        }
        if (canDestContainFluid) {
            handler = new LiquidBlockContainerTransfer(destBlockState.m_60734_(), level, pos);
        } else {
            handler = getFluidBlockHandler(fluid, level, pos);
        }
        FluidStack result2 = tryFluidTransfer(handler, fluidSource, resource, true);
        if (!result2.isEmpty()) {
            SoundEvent soundevent = FluidHelper.getEmptySound(resource);
            if (soundevent != null) {
                level.m_5594_(player, pos, soundevent, SoundSource.BLOCKS, 1.0f, 1.0f);
                return true;
            }
            return true;
        }
        return false;
    }

    private static IFluidTransfer getFluidBlockHandler(Fluid fluid, Level level, BlockPos pos) {
        BlockState state = fluid.m_76145_().m_76188_();
        return new LiquidBlockContainerTransfer.BlockWrapper(state, level, pos);
    }

    public static void destroyBlockOnFluidPlacement(Level level, BlockPos pos) {
        if (!level.f_46443_) {
            BlockState destBlockState = level.m_8055_(pos);
            Material destMaterial = destBlockState.m_60767_();
            boolean isDestNonSolid = !destMaterial.m_76333_();
            if ((isDestNonSolid || 0 != 0) && !destMaterial.m_76332_()) {
                level.m_46961_(pos, true);
            }
        }
    }

    public static long transferFluids(@Nonnull IFluidTransfer sourceHandler, @Nonnull IFluidTransfer destHandler, long transferLimit, @Nonnull Predicate<FluidStack> fluidFilter) {
        long fluidLeftToTransfer = transferLimit;
        for (int i = 0; i < sourceHandler.getTanks(); i++) {
            FluidStack currentFluid = sourceHandler.getFluidInTank(i).copy();
            if (!currentFluid.isEmpty() && fluidFilter.test(currentFluid)) {
                currentFluid.setAmount(fluidLeftToTransfer);
                FluidStack drained = sourceHandler.drain(currentFluid, true);
                if (drained.isEmpty()) {
                    continue;
                } else {
                    long canInsertAmount = destHandler.fill(drained.copy(), true);
                    if (canInsertAmount > 0) {
                        drained.setAmount(canInsertAmount);
                        FluidStack drained2 = sourceHandler.drain(drained, false);
                        if (!drained2.isEmpty()) {
                            destHandler.fill(drained2, false);
                            fluidLeftToTransfer -= drained2.getAmount();
                            if (fluidLeftToTransfer == 0) {
                                break;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return transferLimit - fluidLeftToTransfer;
    }
}
