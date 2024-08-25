package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/CycleButtonWidget.class */
public class CycleButtonWidget extends Widget {
    protected Int2ObjectFunction<IGuiTexture> texture;
    protected IntConsumer onChanged;
    protected IntSupplier indexSupplier;
    protected int range;
    protected int index;

    public CycleButtonWidget setTexture(Int2ObjectFunction<IGuiTexture> texture) {
        this.texture = texture;
        return this;
    }

    public CycleButtonWidget setOnChanged(IntConsumer onChanged) {
        this.onChanged = onChanged;
        return this;
    }

    public CycleButtonWidget setIndexSupplier(IntSupplier indexSupplier) {
        this.indexSupplier = indexSupplier;
        return this;
    }

    public CycleButtonWidget(int xPosition, int yPosition, int width, int height, int range, Int2ObjectFunction<IGuiTexture> texture, IntConsumer onChanged) {
        super(xPosition, yPosition, width, height);
        this.texture = texture;
        this.onChanged = onChanged;
        this.range = range;
        setBackground((IGuiTexture) texture.get(0));
    }

    public void setIndex(int index) {
        this.index = index;
        setBackground((IGuiTexture) this.texture.get(index));
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        if (this.indexSupplier != null) {
            this.index = this.indexSupplier.getAsInt();
        }
        buffer.m_130130_(this.index);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        this.index = buffer.m_130242_();
        setBackground((IGuiTexture) this.texture.get(this.index));
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void detectAndSendChanges() {
        int newIndex;
        super.detectAndSendChanges();
        if (!this.isClientSideWidget && this.indexSupplier != null && (newIndex = this.indexSupplier.getAsInt()) != this.index) {
            this.index = newIndex;
            writeUpdateInfo(1, buf -> {
                buf.m_130130_(this.index);
            });
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void updateScreen() {
        int newIndex;
        super.updateScreen();
        if (this.isClientSideWidget && this.indexSupplier != null && (newIndex = this.indexSupplier.getAsInt()) != this.index) {
            this.index = newIndex;
            setBackground((IGuiTexture) this.texture.get(this.index));
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            this.index++;
            if (this.index >= this.range) {
                this.index = 0;
            }
            setBackground((IGuiTexture) this.texture.get(this.index));
            if (this.onChanged != null) {
                this.onChanged.accept(this.index);
            }
            writeClientAction(1, buf -> {
                buf.m_130130_(this.index);
            });
            playButtonClickSound();
            return true;
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            this.index = buffer.m_130242_();
            if (this.onChanged != null) {
                this.onChanged.accept(this.index);
            }
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == 1) {
            this.index = buffer.m_130242_();
            setBackground((IGuiTexture) this.texture.get(this.index));
            return;
        }
        super.readUpdateInfo(id, buffer);
    }
}
