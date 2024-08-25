package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.editor.configurator.WrapperConfigurator;
import com.lowdragmc.lowdraglib.gui.ingredient.IRecipeIngredientSlot;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.gui.util.TextFormattingUtil;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.misc.FluidStorage;
import com.lowdragmc.lowdraglib.side.fluid.FluidActionResult;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.FluidTransferHelper;
import com.lowdragmc.lowdraglib.side.fluid.IFluidStorage;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@LDLRegister(name = "fluid_slot", group = "widget.container")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/TankWidget.class */
public class TankWidget extends Widget implements IRecipeIngredientSlot, IConfigurableWidget {
    @Nullable
    protected IFluidStorage fluidTank;
    @Configurable
    protected boolean showAmount;
    @Configurable
    protected boolean allowClickFilled;
    @Configurable
    protected boolean allowClickDrained;
    @Configurable
    public boolean drawHoverOverlay;
    @Configurable
    protected boolean drawHoverTips;
    @Configurable
    protected ProgressTexture.FillDirection fillDirection;
    @Configurable
    protected IGuiTexture overlay;
    protected BiConsumer<TankWidget, List<Component>> onAddedTooltips;
    protected IngredientIO ingredientIO;
    protected FluidStack lastFluidInTank;
    protected long lastTankCapacity;
    protected Runnable changeListener;
    @NotNull
    protected List<Consumer<List<Component>>> tooltipCallback;

    @Nullable
    public IFluidStorage getFluidTank() {
        return this.fluidTank;
    }

    public TankWidget setShowAmount(boolean showAmount) {
        this.showAmount = showAmount;
        return this;
    }

    public TankWidget setAllowClickFilled(boolean allowClickFilled) {
        this.allowClickFilled = allowClickFilled;
        return this;
    }

    public TankWidget setAllowClickDrained(boolean allowClickDrained) {
        this.allowClickDrained = allowClickDrained;
        return this;
    }

    public TankWidget setDrawHoverOverlay(boolean drawHoverOverlay) {
        this.drawHoverOverlay = drawHoverOverlay;
        return this;
    }

    public TankWidget setDrawHoverTips(boolean drawHoverTips) {
        this.drawHoverTips = drawHoverTips;
        return this;
    }

    public TankWidget setFillDirection(ProgressTexture.FillDirection fillDirection) {
        this.fillDirection = fillDirection;
        return this;
    }

    public TankWidget setOverlay(IGuiTexture overlay) {
        this.overlay = overlay;
        return this;
    }

    public TankWidget setOnAddedTooltips(BiConsumer<TankWidget, List<Component>> onAddedTooltips) {
        this.onAddedTooltips = onAddedTooltips;
        return this;
    }

    public TankWidget setIngredientIO(IngredientIO ingredientIO) {
        this.ingredientIO = ingredientIO;
        return this;
    }

    public TankWidget setChangeListener(Runnable changeListener) {
        this.changeListener = changeListener;
        return this;
    }

    public TankWidget() {
        this(null, 0, 0, 18, 18, true, true);
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget
    public void initTemplate() {
        setBackground(new ResourceTexture("ldlib:textures/gui/fluid_slot.png"));
        setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP);
    }

    public TankWidget(IFluidStorage fluidTank, int x, int y, boolean allowClickContainerFilling, boolean allowClickContainerEmptying) {
        this(fluidTank, x, y, 18, 18, allowClickContainerFilling, allowClickContainerEmptying);
    }

    public TankWidget(@Nullable IFluidStorage fluidTank, int x, int y, int width, int height, boolean allowClickContainerFilling, boolean allowClickContainerEmptying) {
        super(new Position(x, y), new Size(width, height));
        this.drawHoverOverlay = true;
        this.fillDirection = ProgressTexture.FillDirection.ALWAYS_FULL;
        this.ingredientIO = IngredientIO.RENDER_ONLY;
        this.tooltipCallback = new ArrayList();
        this.fluidTank = fluidTank;
        this.showAmount = true;
        this.allowClickFilled = allowClickContainerFilling;
        this.allowClickDrained = allowClickContainerEmptying;
        this.drawHoverTips = true;
    }

    public TankWidget setFluidTank(IFluidStorage fluidTank) {
        this.fluidTank = fluidTank;
        if (this.isClientSideWidget) {
            setClientSideWidget();
        }
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public TankWidget setClientSideWidget() {
        super.setClientSideWidget();
        if (this.fluidTank != null) {
            this.fluidTank.getFluid();
            this.lastFluidInTank = this.fluidTank.getFluid().copy();
        } else {
            this.lastFluidInTank = null;
        }
        this.lastTankCapacity = this.fluidTank != null ? this.fluidTank.getCapacity() : 0L;
        return this;
    }

    public TankWidget setBackground(IGuiTexture background) {
        super.setBackground(background);
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.ingredient.IRecipeIngredientSlot
    public Object getJEIIngredient() {
        if (this.lastFluidInTank == null || this.lastFluidInTank.isEmpty()) {
            return null;
        }
        if (LDLib.isReiLoaded()) {
            if (this.lastFluidInTank.isEmpty()) {
                return null;
            }
            return EntryStacks.of(dev.architectury.fluid.FluidStack.create(this.lastFluidInTank.getFluid(), this.lastFluidInTank.getAmount(), this.lastFluidInTank.getTag()));
        } else if (LDLib.isEmiLoaded()) {
            if (this.lastFluidInTank.isEmpty()) {
                return null;
            }
            return new FluidEmiStack(this.lastFluidInTank.getFluid(), this.lastFluidInTank.getTag(), this.lastFluidInTank.getAmount());
        } else if (this.lastFluidInTank.isEmpty()) {
            return null;
        } else {
            return FluidHelper.toRealFluidStack(this.lastFluidInTank);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.ingredient.IRecipeIngredientSlot
    public IngredientIO getIngredientIO() {
        return this.ingredientIO;
    }

    private List<Component> getToolTips(List<Component> list) {
        if (this.onAddedTooltips != null) {
            this.onAddedTooltips.accept(this, list);
        }
        for (Consumer<List<Component>> callback : this.tooltipCallback) {
            callback.accept(list);
        }
        return list;
    }

    @Override // com.lowdragmc.lowdraglib.gui.ingredient.IRecipeIngredientSlot
    public void addTooltipCallback(Consumer<List<Component>> callback) {
        this.tooltipCallback.add(callback);
    }

    @Override // com.lowdragmc.lowdraglib.gui.ingredient.IRecipeIngredientSlot
    public void clearTooltipCallback() {
        this.tooltipCallback.clear();
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(matrixStack, mouseX, mouseY, partialTicks);
        if (this.isClientSideWidget && this.fluidTank != null) {
            FluidStack fluidStack = this.fluidTank.getFluid();
            if (this.fluidTank.getCapacity() != this.lastTankCapacity) {
                this.lastTankCapacity = this.fluidTank.getCapacity();
            }
            if (!fluidStack.isFluidEqual(this.lastFluidInTank)) {
                this.lastFluidInTank = fluidStack.copy();
            } else if (fluidStack.getAmount() != this.lastFluidInTank.getAmount()) {
                this.lastFluidInTank.setAmount(fluidStack.getAmount());
            }
        }
        Position pos = getPosition();
        Size size = getSize();
        if (this.lastFluidInTank != null) {
            RenderSystem.m_69461_();
            if (!this.lastFluidInTank.isEmpty()) {
                double progress = (this.lastFluidInTank.getAmount() * 1.0d) / Math.max(Math.max(this.lastFluidInTank.getAmount(), this.lastTankCapacity), 1L);
                float drawnU = (float) this.fillDirection.getDrawnU(progress);
                float drawnV = (float) this.fillDirection.getDrawnV(progress);
                float drawnWidth = (float) this.fillDirection.getDrawnWidth(progress);
                float drawnHeight = (float) this.fillDirection.getDrawnHeight(progress);
                int width = size.width - 2;
                int height = size.height - 2;
                int x = pos.x + 1;
                int y = pos.y + 1;
                DrawerHelper.drawFluidForGui(matrixStack, this.lastFluidInTank, this.lastFluidInTank.getAmount(), (int) (x + (drawnU * width)), (int) (y + (drawnV * height)), (int) (width * drawnWidth), (int) (height * drawnHeight));
            }
            if (this.showAmount && !this.lastFluidInTank.isEmpty()) {
                matrixStack.m_85836_();
                matrixStack.m_85841_(0.5f, 0.5f, 1.0f);
                String s = TextFormattingUtil.formatLongToCompactStringBuckets(this.lastFluidInTank.getAmount(), 3) + "B";
                Font fontRenderer = Minecraft.m_91087_().f_91062_;
                fontRenderer.m_92750_(matrixStack, s, (((pos.x + (size.width / 3.0f)) * 2.0f) - fontRenderer.m_92895_(s)) + 21.0f, (pos.y + (size.height / 3.0f) + 6.0f) * 2.0f, 16777215);
                matrixStack.m_85849_();
            }
            RenderSystem.m_69478_();
            RenderSystem.m_157429_(1.0f, 1.0f, 1.0f, 1.0f);
        }
        if (this.overlay != null) {
            this.overlay.draw(matrixStack, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }
        if (this.drawHoverOverlay && isMouseOverElement(mouseX, mouseY) && getHoverElement(mouseX, mouseY) == this) {
            RenderSystem.m_69444_(true, true, true, false);
            DrawerHelper.drawSolidRect(matrixStack, getPosition().x + 1, getPosition().y + 1, getSize().width - 2, getSize().height - 2, -2130706433);
            RenderSystem.m_69444_(true, true, true, true);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.drawHoverTips && isMouseOverElement(mouseX, mouseY) && getHoverElement(mouseX, mouseY) == this) {
            List<Component> tooltips = new ArrayList<>();
            if (this.lastFluidInTank != null && !this.lastFluidInTank.isEmpty()) {
                tooltips.add(FluidHelper.getDisplayName(this.lastFluidInTank));
                tooltips.add(Component.m_237110_("ldlib.fluid.amount", new Object[]{Long.valueOf(this.lastFluidInTank.getAmount()), Long.valueOf(this.lastTankCapacity)}).m_130946_(" " + FluidHelper.getUnit()));
                if (!Platform.isForge()) {
                    tooltips.add(Component.m_237113_("§6mB:§r %d/%d".formatted(new Object[]{Long.valueOf((this.lastFluidInTank.getAmount() * 1000) / FluidHelper.getBucket()), Long.valueOf((this.lastTankCapacity * 1000) / FluidHelper.getBucket())})).m_130946_(" mB"));
                }
                tooltips.add(Component.m_237110_("ldlib.fluid.temperature", new Object[]{Integer.valueOf(FluidHelper.getTemperature(this.lastFluidInTank))}));
                tooltips.add(Component.m_237115_(FluidHelper.isLighterThanAir(this.lastFluidInTank) ? "ldlib.fluid.state_gas" : "ldlib.fluid.state_liquid"));
            } else {
                tooltips.add(Component.m_237115_("ldlib.fluid.empty"));
                tooltips.add(Component.m_237110_("ldlib.fluid.amount", new Object[]{0, Long.valueOf(this.lastTankCapacity)}).m_130946_(" " + FluidHelper.getUnit()));
                if (!Platform.isForge()) {
                    tooltips.add(Component.m_237113_("§6mB:§r %d/%d".formatted(new Object[]{0, Long.valueOf((this.lastTankCapacity * 1000) / FluidHelper.getBucket())})).m_130946_(" mB"));
                }
            }
            if (this.gui != null) {
                tooltips.addAll(this.tooltipTexts);
                this.gui.getModularUIGui().setHoverTooltip(getToolTips(tooltips), ItemStack.f_41583_, null, null);
            }
            RenderSystem.m_157429_(1.0f, 1.0f, 1.0f, 1.0f);
            return;
        }
        super.drawInForeground(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void detectAndSendChanges() {
        if (this.fluidTank != null) {
            FluidStack fluidStack = this.fluidTank.getFluid();
            if (this.fluidTank.getCapacity() != this.lastTankCapacity) {
                this.lastTankCapacity = this.fluidTank.getCapacity();
                writeUpdateInfo(0, buffer -> {
                    buffer.m_130103_(this.lastTankCapacity);
                });
            }
            if (!fluidStack.isFluidEqual(this.lastFluidInTank)) {
                this.lastFluidInTank = fluidStack.copy();
                CompoundTag fluidStackTag = fluidStack.saveToTag(new CompoundTag());
                writeUpdateInfo(2, buffer2 -> {
                    buffer2.m_130079_(fluidStackTag);
                });
            } else if (fluidStack.getAmount() != this.lastFluidInTank.getAmount()) {
                this.lastFluidInTank.setAmount(fluidStack.getAmount());
                writeUpdateInfo(3, buffer3 -> {
                    buffer3.m_130103_(this.lastFluidInTank.getAmount());
                });
            } else {
                super.detectAndSendChanges();
                return;
            }
            if (this.changeListener != null) {
                this.changeListener.run();
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void writeInitialData(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.fluidTank != null);
        if (this.fluidTank != null) {
            this.lastTankCapacity = this.fluidTank.getCapacity();
            buffer.m_130103_(this.lastTankCapacity);
            FluidStack fluidStack = this.fluidTank.getFluid();
            this.lastFluidInTank = fluidStack.copy();
            buffer.m_130079_(fluidStack.saveToTag(new CompoundTag()));
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readInitialData(FriendlyByteBuf buffer) {
        if (buffer.readBoolean()) {
            this.lastTankCapacity = buffer.m_130258_();
            readUpdateInfo(2, buffer);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == 0) {
            this.lastTankCapacity = buffer.m_130258_();
        } else if (id == 1) {
            this.lastFluidInTank = null;
        } else if (id == 2) {
            this.lastFluidInTank = FluidStack.loadFromTag(buffer.m_130260_());
        } else if (id == 3 && this.lastFluidInTank != null) {
            this.lastFluidInTank.setAmount(buffer.m_130258_());
        } else if (id == 4) {
            ItemStack currentStack = this.gui.getModularUIContainer().m_142621_();
            int newStackSize = buffer.m_130242_();
            currentStack.m_41764_(newStackSize);
            this.gui.getModularUIContainer().m_142503_(currentStack);
        } else {
            super.readUpdateInfo(id, buffer);
            return;
        }
        if (this.changeListener != null) {
            this.changeListener.run();
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            boolean isShiftKeyDown = buffer.readBoolean();
            int clickResult = tryClickContainer(isShiftKeyDown);
            if (clickResult >= 0) {
                writeUpdateInfo(4, buf -> {
                    buf.m_130130_(clickResult);
                });
            }
        }
    }

    private int tryClickContainer(boolean isShiftKeyDown) {
        if (this.fluidTank == null) {
            return -1;
        }
        Player player = this.gui.entityPlayer;
        ItemStack currentStack = this.gui.getModularUIContainer().m_142621_();
        IFluidTransfer handler = FluidTransferHelper.getFluidTransfer(this.gui.entityPlayer, this.gui.getModularUIContainer());
        if (handler == null) {
            return -1;
        }
        int maxAttempts = isShiftKeyDown ? currentStack.m_41613_() : 1;
        if (this.allowClickFilled && this.fluidTank.getFluidAmount() > 0) {
            boolean performedFill = false;
            FluidStack initialFluid = this.fluidTank.getFluid();
            int i = 0;
            while (true) {
                if (i >= maxAttempts) {
                    break;
                }
                FluidActionResult result = FluidTransferHelper.tryFillContainer(currentStack, this.fluidTank, Integer.MAX_VALUE, null, false);
                if (!result.isSuccess()) {
                    break;
                }
                ItemStack remainingStack = FluidTransferHelper.tryFillContainer(currentStack, this.fluidTank, Integer.MAX_VALUE, null, true).getResult();
                currentStack.m_41774_(1);
                performedFill = true;
                if (remainingStack.m_41619_() || player.m_36356_(remainingStack)) {
                    i++;
                } else {
                    Block.m_49840_(player.m_9236_(), player.m_20097_(), remainingStack);
                    break;
                }
            }
            if (performedFill) {
                SoundEvent soundevent = FluidHelper.getFillSound(initialFluid);
                if (soundevent != null) {
                    player.f_19853_.m_6263_((Player) null, player.m_20182_().f_82479_, player.m_20182_().f_82480_ + 0.5d, player.m_20182_().f_82481_, soundevent, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                this.gui.getModularUIContainer().m_142503_(currentStack);
                return currentStack.m_41613_();
            }
        }
        if (this.allowClickDrained) {
            boolean performedEmptying = false;
            int i2 = 0;
            while (true) {
                if (i2 >= maxAttempts) {
                    break;
                }
                FluidActionResult result2 = FluidTransferHelper.tryEmptyContainer(currentStack, this.fluidTank, Integer.MAX_VALUE, null, false);
                if (!result2.isSuccess()) {
                    break;
                }
                ItemStack remainingStack2 = FluidTransferHelper.tryEmptyContainer(currentStack, this.fluidTank, Integer.MAX_VALUE, null, true).getResult();
                currentStack.m_41774_(1);
                performedEmptying = true;
                if (remainingStack2.m_41619_() || player.m_150109_().m_36054_(remainingStack2)) {
                    i2++;
                } else {
                    Block.m_49840_(player.m_9236_(), player.m_20097_(), remainingStack2);
                    break;
                }
            }
            FluidStack filledFluid = this.fluidTank.getFluid();
            if (performedEmptying) {
                SoundEvent soundevent2 = FluidHelper.getEmptySound(filledFluid);
                if (soundevent2 != null) {
                    player.f_19853_.m_6263_((Player) null, player.m_20182_().f_82479_, player.m_20182_().f_82480_ + 0.5d, player.m_20182_().f_82481_, soundevent2, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                this.gui.getModularUIContainer().m_142503_(currentStack);
                return currentStack.m_41613_();
            }
            return -1;
        }
        return -1;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if ((this.allowClickDrained || this.allowClickFilled) && isMouseOverElement(mouseX, mouseY) && button == 0 && FluidTransferHelper.getFluidTransfer(this.gui.entityPlayer, this.gui.getModularUIContainer()) != null) {
            boolean isShiftKeyDown = isShiftDown();
            writeClientAction(1, writer -> {
                writer.writeBoolean(isShiftKeyDown);
            });
            playButtonClickSound();
            return true;
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    public void buildConfigurator(ConfiguratorGroup father) {
        FluidStorage handler = new FluidStorage(5000L);
        handler.fill(FluidStack.create((Fluid) Fluids.f_76193_, 3000L), false);
        father.addConfigurators(new WrapperConfigurator("ldlib.gui.editor.group.preview", new TankWidget() { // from class: com.lowdragmc.lowdraglib.gui.widget.TankWidget.1
            @Override // com.lowdragmc.lowdraglib.gui.widget.TankWidget, com.lowdragmc.lowdraglib.gui.widget.Widget
            public /* bridge */ /* synthetic */ Widget setClientSideWidget() {
                return super.setClientSideWidget();
            }

            @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
            public void updateScreen() {
                super.updateScreen();
                setHoverTooltips(TankWidget.this.tooltipTexts);
                this.backgroundTexture = TankWidget.this.backgroundTexture;
                this.hoverTexture = TankWidget.this.hoverTexture;
                this.showAmount = TankWidget.this.showAmount;
                this.drawHoverTips = TankWidget.this.drawHoverTips;
                this.fillDirection = TankWidget.this.fillDirection;
                this.overlay = TankWidget.this.overlay;
            }
        }.setAllowClickDrained(false).setAllowClickFilled(false).setFluidTank(handler)));
        super.buildConfigurator(father);
    }
}
