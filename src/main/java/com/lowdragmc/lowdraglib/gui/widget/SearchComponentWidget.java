package com.lowdragmc.lowdraglib.gui.widget;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.utils.ISearch;
import com.lowdragmc.lowdraglib.utils.SearchEngine;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/SearchComponentWidget.class */
public class SearchComponentWidget<T> extends WidgetGroup {
    public final SearchEngine<T> engine;
    public final IWidgetSearch<T> search;
    public final DraggableScrollableWidgetGroup popUp;
    public final TextFieldWidget textFieldWidget;
    private int capacity;
    protected boolean isShow;

    public SearchComponentWidget(int x, int y, int width, int height, IWidgetSearch<T> search) {
        this(x, y, width, height, search, false);
    }

    public SearchComponentWidget(int x, int y, int width, int height, IWidgetSearch<T> search, boolean isServer) {
        super(x, y, width, height);
        this.capacity = 10;
        if (!isServer) {
            setClientSideWidget();
        }
        TextFieldWidget textFieldWidget = new TextFieldWidget(0, 0, width, height, null, null) { // from class: com.lowdragmc.lowdraglib.gui.widget.SearchComponentWidget.1
            @Override // com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget, com.lowdragmc.lowdraglib.gui.widget.Widget
            public void onFocusChanged(@Nullable Widget lastFocus, Widget focus) {
                if (lastFocus != null && focus != null && lastFocus.parent == focus.parent) {
                    return;
                }
                super.onFocusChanged(lastFocus, focus);
                SearchComponentWidget.this.setShow(isFocus());
            }
        };
        this.textFieldWidget = textFieldWidget;
        addWidget(textFieldWidget);
        DraggableScrollableWidgetGroup draggableScrollableWidgetGroup = new DraggableScrollableWidgetGroup(0, height, width, 0) { // from class: com.lowdragmc.lowdraglib.gui.widget.SearchComponentWidget.2
            @Override // com.lowdragmc.lowdraglib.gui.widget.Widget
            public void onFocusChanged(@Nullable Widget lastFocus, Widget focus) {
                if (lastFocus != null && focus != null && lastFocus.parent == focus.parent) {
                    return;
                }
                super.onFocusChanged(lastFocus, focus);
                SearchComponentWidget.this.setShow(isFocus());
            }
        };
        this.popUp = draggableScrollableWidgetGroup;
        addWidget(draggableScrollableWidgetGroup);
        this.popUp.setBackground(new ColorRectTexture(-1442840576));
        this.popUp.setVisible(false);
        this.popUp.setActive(true);
        this.search = search;
        this.engine = new SearchEngine<>(search, r -> {
            int size = this.popUp.getAllWidgetSize();
            this.popUp.setSize(new Size(getSize().width, Math.min(size + 1, this.capacity) * 15));
            this.popUp.waitToAdded(new ButtonWidget(0, size * 15, width, 15, new TextTexture(width.resultDisplay(isServer)).setWidth(width).setType(TextTexture.TextType.ROLL), cd -> {
                search.selectResult(width);
                this.textFieldWidget.setCurrentString(search.resultDisplay(width));
            }).setHoverBorderTexture(-1, -1));
            if (search) {
                writeUpdateInfo(-2, buf -> {
                    width.serialize(isServer, buf);
                });
            }
        });
        this.textFieldWidget.setTextResponder(s -> {
            this.popUp.clearAllWidgets();
            this.popUp.setSize(new Size(getSize().width, 0));
            this.engine.searchWord(isServer);
            if (isServer) {
                writeUpdateInfo(-1, buffer -> {
                });
            }
        });
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == -1) {
            this.popUp.clearAllWidgets();
            this.popUp.setSize(new Size(getSize().width, 0));
        } else if (id == -2) {
            T r = this.search.deserialize(buffer);
            int size = this.popUp.getAllWidgetSize();
            int width = getSize().width;
            this.popUp.setSize(new Size(getSize().width, Math.min(size + 1, this.capacity) * 15));
            this.popUp.addWidget(new ButtonWidget(0, size * 15, width, 15, new TextTexture(this.search.resultDisplay(r)).setWidth(width).setType(TextTexture.TextType.ROLL), cd -> {
                this.search.selectResult(r);
                this.textFieldWidget.setCurrentString(this.search.resultDisplay(r));
            }).setHoverBorderTexture(-1, -1));
        } else {
            super.readUpdateInfo(id, buffer);
        }
    }

    public SearchComponentWidget<T> setCapacity(int capacity) {
        this.capacity = capacity;
        this.popUp.setSize(new Size(getSize().width, Math.min(this.popUp.getAllWidgetSize(), capacity) * 15));
        return this;
    }

    public SearchComponentWidget<T> setCurrentString(String currentString) {
        this.textFieldWidget.setCurrentString(currentString);
        return this;
    }

    public String getCurrentString() {
        return this.textFieldWidget.getCurrentString();
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
        this.popUp.setVisible(isShow);
        this.popUp.setActive(isShow);
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        boolean lastVisible = this.popUp.isVisible();
        this.popUp.setVisible(false);
        super.drawInForeground(matrixStack, mouseX, mouseY, partialTicks);
        this.popUp.setVisible(lastVisible);
        if (this.isShow) {
            matrixStack.m_85837_(0.0d, 0.0d, 200.0d);
            this.popUp.drawInBackground(matrixStack, mouseX, mouseY, partialTicks);
            this.popUp.drawInForeground(matrixStack, mouseX, mouseY, partialTicks);
            matrixStack.m_85837_(0.0d, 0.0d, -200.0d);
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.widget.WidgetGroup, com.lowdragmc.lowdraglib.gui.widget.Widget
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        boolean lastVisible = this.popUp.isVisible();
        this.popUp.setVisible(false);
        super.drawInBackground(matrixStack, mouseX, mouseY, partialTicks);
        this.popUp.setVisible(lastVisible);
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/SearchComponentWidget$IWidgetSearch.class */
    public interface IWidgetSearch<T> extends ISearch<T> {
        String resultDisplay(T t);

        void selectResult(T t);

        default void serialize(T value, FriendlyByteBuf buf) {
            buf.m_130070_(resultDisplay(value));
        }

        default T deserialize(FriendlyByteBuf buf) {
            return (T) buf.m_130277_();
        }
    }
}
