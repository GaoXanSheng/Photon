package com.lowdragmc.lowdraglib.gui.editor.ui;

import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidget;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.editor.ui.tool.WidgetToolBox;
import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.WidgetTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Arrays;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/editor/ui/UIWrapper.class */
public final class UIWrapper extends Record implements IConfigurable {
    private final MainPanel panel;
    private final IConfigurableWidget inner;

    /*  JADX ERROR: Dependency scan failed at insn: 0x0001: INVOKE_CUSTOM
        jadx.plugins.input.java.utils.JavaClassParseException: Can't encode constant CLASS as encoded value
        	at jadx.plugins.input.java.data.ConstPoolReader.readAsEncodedValue(ConstPoolReader.java:230)
        	at jadx.plugins.input.java.data.ConstPoolReader.resolveMethodCallSite(ConstPoolReader.java:117)
        	at jadx.plugins.input.java.data.ConstPoolReader.getCallSite(ConstPoolReader.java:97)
        	at jadx.plugins.input.java.data.code.JavaInsnData.getIndexAsCallSite(JavaInsnData.java:168)
        	at jadx.plugins.input.java.data.code.decoders.InvokeDecoder.decode(InvokeDecoder.java:32)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processInsn(UsageInfoVisitor.java:127)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.lambda$processInstructions$0(UsageInfoVisitor.java:84)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:82)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processInstructions(UsageInfoVisitor.java:82)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processMethod(UsageInfoVisitor.java:67)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processClass(UsageInfoVisitor.java:56)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.init(UsageInfoVisitor.java:41)
        	at jadx.core.dex.nodes.RootNode.runPreDecompileStage(RootNode.java:275)
        */
    /*  JADX ERROR: Failed to decode insn: 0x0001: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.gui.editor.ui.UIWrapper.toString():java.lang.String
        jadx.plugins.input.java.utils.JavaClassParseException: Can't encode constant CLASS as encoded value
        	at jadx.plugins.input.java.data.ConstPoolReader.readAsEncodedValue(ConstPoolReader.java:230)
        	at jadx.plugins.input.java.data.ConstPoolReader.resolveMethodCallSite(ConstPoolReader.java:117)
        	at jadx.plugins.input.java.data.ConstPoolReader.getCallSite(ConstPoolReader.java:97)
        	at jadx.plugins.input.java.data.code.JavaInsnData.getIndexAsCallSite(JavaInsnData.java:168)
        	at jadx.plugins.input.java.data.code.decoders.InvokeDecoder.decode(InvokeDecoder.java:32)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:52)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:82)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:107)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        */
    public final java.lang.String toString() {
        /*
            r2 = this;
            r0 = r2
            // decode failed: Can't encode constant CLASS as encoded value
            r1 = -1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.gui.editor.ui.UIWrapper.toString():java.lang.String");
    }

    /*  JADX ERROR: Dependency scan failed at insn: 0x0001: INVOKE_CUSTOM
        jadx.plugins.input.java.utils.JavaClassParseException: Can't encode constant CLASS as encoded value
        	at jadx.plugins.input.java.data.ConstPoolReader.readAsEncodedValue(ConstPoolReader.java:230)
        	at jadx.plugins.input.java.data.ConstPoolReader.resolveMethodCallSite(ConstPoolReader.java:117)
        	at jadx.plugins.input.java.data.ConstPoolReader.getCallSite(ConstPoolReader.java:97)
        	at jadx.plugins.input.java.data.code.JavaInsnData.getIndexAsCallSite(JavaInsnData.java:168)
        	at jadx.plugins.input.java.data.code.decoders.InvokeDecoder.decode(InvokeDecoder.java:32)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processInsn(UsageInfoVisitor.java:127)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.lambda$processInstructions$0(UsageInfoVisitor.java:84)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:82)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processInstructions(UsageInfoVisitor.java:82)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processMethod(UsageInfoVisitor.java:67)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processClass(UsageInfoVisitor.java:56)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.init(UsageInfoVisitor.java:41)
        	at jadx.core.dex.nodes.RootNode.runPreDecompileStage(RootNode.java:275)
        */
    /*  JADX ERROR: Failed to decode insn: 0x0001: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.gui.editor.ui.UIWrapper.hashCode():int
        jadx.plugins.input.java.utils.JavaClassParseException: Can't encode constant CLASS as encoded value
        	at jadx.plugins.input.java.data.ConstPoolReader.readAsEncodedValue(ConstPoolReader.java:230)
        	at jadx.plugins.input.java.data.ConstPoolReader.resolveMethodCallSite(ConstPoolReader.java:117)
        	at jadx.plugins.input.java.data.ConstPoolReader.getCallSite(ConstPoolReader.java:97)
        	at jadx.plugins.input.java.data.code.JavaInsnData.getIndexAsCallSite(JavaInsnData.java:168)
        	at jadx.plugins.input.java.data.code.decoders.InvokeDecoder.decode(InvokeDecoder.java:32)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:52)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:82)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:107)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        */
    public final int hashCode() {
        /*
            r2 = this;
            r0 = r2
            // decode failed: Can't encode constant CLASS as encoded value
            r1 = -1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.gui.editor.ui.UIWrapper.hashCode():int");
    }

    /*  JADX ERROR: Dependency scan failed at insn: 0x0002: INVOKE_CUSTOM
        jadx.plugins.input.java.utils.JavaClassParseException: Can't encode constant CLASS as encoded value
        	at jadx.plugins.input.java.data.ConstPoolReader.readAsEncodedValue(ConstPoolReader.java:230)
        	at jadx.plugins.input.java.data.ConstPoolReader.resolveMethodCallSite(ConstPoolReader.java:117)
        	at jadx.plugins.input.java.data.ConstPoolReader.getCallSite(ConstPoolReader.java:97)
        	at jadx.plugins.input.java.data.code.JavaInsnData.getIndexAsCallSite(JavaInsnData.java:168)
        	at jadx.plugins.input.java.data.code.decoders.InvokeDecoder.decode(InvokeDecoder.java:32)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processInsn(UsageInfoVisitor.java:127)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.lambda$processInstructions$0(UsageInfoVisitor.java:84)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:82)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processInstructions(UsageInfoVisitor.java:82)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processMethod(UsageInfoVisitor.java:67)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processClass(UsageInfoVisitor.java:56)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.init(UsageInfoVisitor.java:41)
        	at jadx.core.dex.nodes.RootNode.runPreDecompileStage(RootNode.java:275)
        */
    /*  JADX ERROR: Failed to decode insn: 0x0002: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.gui.editor.ui.UIWrapper.equals(java.lang.Object):boolean
        jadx.plugins.input.java.utils.JavaClassParseException: Can't encode constant CLASS as encoded value
        	at jadx.plugins.input.java.data.ConstPoolReader.readAsEncodedValue(ConstPoolReader.java:230)
        	at jadx.plugins.input.java.data.ConstPoolReader.resolveMethodCallSite(ConstPoolReader.java:117)
        	at jadx.plugins.input.java.data.ConstPoolReader.getCallSite(ConstPoolReader.java:97)
        	at jadx.plugins.input.java.data.code.JavaInsnData.getIndexAsCallSite(JavaInsnData.java:168)
        	at jadx.plugins.input.java.data.code.decoders.InvokeDecoder.decode(InvokeDecoder.java:32)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:52)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:82)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:107)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        */
    public final boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r3
            r1 = r4
            // decode failed: Can't encode constant CLASS as encoded value
            r2 = -1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.gui.editor.ui.UIWrapper.equals(java.lang.Object):boolean");
    }

    public UIWrapper(MainPanel panel, IConfigurableWidget inner) {
        this.panel = panel;
        this.inner = inner;
    }

    public MainPanel panel() {
        return this.panel;
    }

    public IConfigurableWidget inner() {
        return this.inner;
    }

    public boolean isSelected() {
        return this.panel.getSelectedUIs().contains(this);
    }

    public boolean isHover() {
        return this.panel.getHoverUI() == this;
    }

    public boolean checkAcceptable(UIWrapper uiWrapper) {
        IConfigurableWidget iConfigurableWidget = this.inner;
        if (iConfigurableWidget instanceof IConfigurableWidgetGroup) {
            IConfigurableWidgetGroup group = (IConfigurableWidgetGroup) iConfigurableWidget;
            if (group.canWidgetAccepted(uiWrapper.inner)) {
                return true;
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        Position pos = this.inner.widget().getPosition();
        Size size = this.inner.widget().getSize();
        int borderColor = 0;
        if (isSelected()) {
            borderColor = -65536;
        }
        if (isHover()) {
            if (!isSelected()) {
                borderColor = 1325400319;
            }
            Object dragging = this.panel.getGui().getModularUIGui().getDraggingElement();
            boolean drawDragging = false;
            if (this.inner.canDragIn(dragging)) {
                drawDragging = true;
            } else {
                if (dragging instanceof WidgetToolBox.IWidgetPanelDragging) {
                    WidgetToolBox.IWidgetPanelDragging widgetPanelDragging = (WidgetToolBox.IWidgetPanelDragging) dragging;
                    if (checkAcceptable(new UIWrapper(this.panel, widgetPanelDragging.get()))) {
                        drawDragging = true;
                    }
                }
                if (dragging instanceof UIWrapper[]) {
                    UIWrapper[] uiWrappers = (UIWrapper[]) dragging;
                    if (Arrays.stream(uiWrappers).allMatch(this::checkAcceptable)) {
                        drawDragging = true;
                    }
                }
            }
            if (drawDragging) {
                borderColor = -11163051;
            }
        }
        if (borderColor != 0) {
            new ColorBorderTexture(1, borderColor).draw(poseStack, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isHover()) {
            Object dragging = this.panel.getGui().getModularUIGui().getDraggingElement();
            if (dragging instanceof WidgetToolBox.IWidgetPanelDragging) {
                WidgetToolBox.IWidgetPanelDragging widgetPanelDragging = (WidgetToolBox.IWidgetPanelDragging) dragging;
                UIWrapper uiWrapper = new UIWrapper(this.panel, widgetPanelDragging.get());
                IConfigurableWidget iConfigurableWidget = this.inner;
                if (iConfigurableWidget instanceof IConfigurableWidgetGroup) {
                    IConfigurableWidgetGroup group = (IConfigurableWidgetGroup) iConfigurableWidget;
                    if (checkAcceptable(uiWrapper)) {
                        WidgetGroup parent = uiWrapper.inner.widget().getParent();
                        if (parent != null) {
                            parent.onWidgetRemoved(uiWrapper.inner);
                        }
                        Position position = new Position((int) mouseX, (int) mouseY).subtract(group.widget().getPosition());
                        uiWrapper.inner.widget().setSelfPosition(new Position(position.x - (uiWrapper.inner.widget().getSize().width / 2), position.y - (uiWrapper.inner.widget().getSize().height / 2)));
                        group.acceptWidget(uiWrapper.inner);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            IConfigurableWidget iConfigurableWidget2 = this.inner;
            if (iConfigurableWidget2 instanceof IConfigurableWidgetGroup) {
                IConfigurableWidgetGroup group2 = (IConfigurableWidgetGroup) iConfigurableWidget2;
                if (dragging instanceof UIWrapper[]) {
                    UIWrapper[] uiWrappers = (UIWrapper[]) dragging;
                    if (Arrays.stream(uiWrappers).allMatch(this::checkAcceptable)) {
                        for (UIWrapper uiWrapper2 : uiWrappers) {
                            WidgetGroup parent2 = uiWrapper2.inner.widget().getParent();
                            if (parent2 != null) {
                                parent2.onWidgetRemoved(uiWrapper2.inner);
                            }
                            Position position2 = new Position((int) mouseX, (int) mouseY).subtract(this.inner.widget().getPosition());
                            uiWrapper2.inner.widget().setSelfPosition(new Position(position2.x - (uiWrapper2.inner.widget().getSize().width / 2), position2.y - (uiWrapper2.inner.widget().getSize().height / 2)));
                            group2.acceptWidget(uiWrapper2.inner);
                        }
                        return true;
                    }
                }
            }
            return this.inner.handleDragging(dragging);
        }
        return false;
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    public void buildConfigurator(ConfiguratorGroup father) {
        if (this.inner.isLDLRegister()) {
            ConfiguratorGroup common = new ConfiguratorGroup(this.inner.getTranslateKey(), false);
            common.setCanCollapse(false);
            father.addConfigurators(common);
            father = common;
        }
        this.inner.buildConfigurator(father);
    }

    public void remove() {
        WidgetGroup parent = this.inner.widget().getParent();
        if (this.inner.widget() != this.panel.root) {
            parent.waitToRemoved(this.inner.widget());
        }
    }

    public void onDragPosition(int deltaX, int deltaY) {
        inner().widget().addSelfPosition(deltaX, deltaY);
    }

    public void onDragSize(int deltaX, int deltaY) {
        Widget selected = inner().widget();
        selected.setSize(new Size(selected.getSize().width + deltaX, selected.getSize().getHeight() + deltaY));
    }

    public boolean is(IConfigurableWidget configurableWidget) {
        return this.inner == configurableWidget;
    }

    public IGuiTexture toDraggingTexture(int mouseX, int mouseY) {
        return new WidgetTexture(mouseX, mouseY, this.inner.widget());
    }
}
