package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.fluid.FluidTransferHelper;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import expr.Expr;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/BlockSelectorWidget.class */
public class BlockSelectorWidget extends WidgetGroup {
    private Consumer<BlockState> onBlockStateUpdate;
    private Block block;
    private final IItemTransfer handler;
    private final TextFieldWidget blockField;
    private final Map<Property, Comparable> properties;

    public BlockSelectorWidget(int x, int y, int width, boolean isState) {
        super(x, y, width, 20);
        setClientSideWidget();
        this.properties = new HashMap();
        this.blockField = (TextFieldWidget) new TextFieldWidget(22, 0, width - (isState ? 46 : 26), 20, null, s -> {
            Block block;
            if (s != null && !s.isEmpty() && this.block != (block = (Block) Registry.f_122824_.m_7745_(new ResourceLocation(s)))) {
                this.block = block;
                onUpdate();
            }
        }).setResourceLocationOnly().setHoverTooltips("ldlib.gui.tips.block_selector");
        ItemStackTransfer itemStackTransfer = new ItemStackTransfer(1);
        this.handler = itemStackTransfer;
        addWidget(new PhantomSlotWidget(itemStackTransfer, 0, 1, 1).setClearSlotOnRightClick(true).setChangeListener(() -> {
            ItemStack stack = this.handler.getStackInSlot(0);
            if (!stack.m_41619_()) {
                BlockItem m_41720_ = stack.m_41720_();
                if (m_41720_ instanceof BlockItem) {
                    BlockItem itemBlock = m_41720_;
                    setBlock(itemBlock.m_40614_().m_49966_());
                    onUpdate();
                    return;
                }
            }
            IFluidTransfer fluidTransfer = FluidTransferHelper.getFluidTransfer(this.handler, 0);
            if (fluidTransfer != null && fluidTransfer.getTanks() > 0) {
                Fluid fluid = fluidTransfer.getFluidInTank(0).getFluid();
                setBlock(fluid.m_76145_().m_76188_());
                onUpdate();
            } else if (this.block != null) {
                setBlock(null);
                onUpdate();
            }
        }).setBackgroundTexture(new ColorBorderTexture(1, -1)));
        addWidget(this.blockField);
        if (isState) {
            addWidget(new ButtonWidget(width - 21, 0, 20, 20, new ItemStackTexture(Items.f_41938_, Items.f_41870_, Items.f_41934_, Items.f_41936_, Items.f_41874_), cd -> {
                DialogWidget onClosed = new DialogWidget(getGui().mainGroup, this.isClientSideWidget).setOnClosed(this::onUpdate);
                DraggableScrollableWidgetGroup group = new DraggableScrollableWidgetGroup(0, 0, getGui().mainGroup.getSize().width, getGui().mainGroup.getSize().height).setYScrollBarWidth(4).setYBarStyle(null, new ColorRectTexture(-1)).setXScrollBarHeight(4).setXBarStyle(null, new ColorRectTexture(-1)).setBackground(new ColorRectTexture(-1893588446));
                onClosed.addWidget(group);
                int i = this.properties.size() - 1;
                for (Map.Entry<Property, Comparable> entry : this.properties.entrySet()) {
                    Property property = entry.getKey();
                    Comparable value = entry.getValue();
                    group.addWidget(new SelectorWidget(3, 3 + (i * 20), 100, 15, property.m_6908_().stream().map(v -> {
                        return property.m_6940_((Comparable) v);
                    }).toList(), -1).setValue(property.m_6940_(value)).setOnChanged(newValue -> {
                        this.properties.put(property, (Comparable) property.m_6215_(property).get());
                    }).setButtonBackground(ResourceBorderTexture.BUTTON_COMMON).setBackground(new ColorRectTexture(-5592406)));
                    group.addWidget(new LabelWidget((int) Expr.COS, 6 + (i * 20), property.m_61708_()));
                    i--;
                }
            }).setHoverBorderTexture(1, -1).setHoverTooltips("ldlib.gui.tips.block_meta"));
        }
    }

    public BlockState getBlock() {
        BlockState state;
        if (this.block == null) {
            state = null;
        } else {
            state = this.block.m_49966_();
            for (Map.Entry<Property, Comparable> entry : this.properties.entrySet()) {
                state = (BlockState) state.m_61124_(entry.getKey(), entry.getValue());
            }
        }
        return state;
    }

    public BlockSelectorWidget setBlock(BlockState blockState) {
        this.properties.clear();
        if (blockState == null) {
            this.block = null;
            this.handler.setStackInSlot(0, ItemStack.f_41583_);
            this.blockField.setCurrentString("");
        } else {
            this.block = blockState.m_60734_();
            new ItemStack(this.block);
            this.handler.setStackInSlot(0, new ItemStack(this.block));
            this.blockField.setCurrentString(Registry.f_122824_.m_7981_(this.block));
            for (Property<?> property : blockState.m_60734_().m_49965_().m_61092_()) {
                this.properties.put(property, blockState.m_61143_(property));
            }
        }
        return this;
    }

    public BlockSelectorWidget setOnBlockStateUpdate(Consumer<BlockState> onBlockStateUpdate) {
        this.onBlockStateUpdate = onBlockStateUpdate;
        return this;
    }

    private void onUpdate() {
        this.handler.setStackInSlot(0, this.block == null ? ItemStack.f_41583_ : new ItemStack(this.block));
        if (this.onBlockStateUpdate != null) {
            this.onBlockStateUpdate.accept(getBlock());
        }
    }
}
