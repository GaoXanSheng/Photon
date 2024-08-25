package com.lowdragmc.lowdraglib.gui.compass.component;

import com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent;
import com.lowdragmc.lowdraglib.gui.compass.LayoutPageWidget;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextBoxWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.XmlUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.w3c.dom.Element;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/HeaderComponent.class */
public class HeaderComponent extends AbstractComponent {
    protected Header header = Header.h1;
    protected String text = "";
    protected int space = 1;
    protected int fontSize = 9;
    protected int fontColor = -1;
    protected boolean isCenter = false;
    protected boolean isShadow = true;

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/HeaderComponent$Header.class */
    public enum Header {
        h1(16, 3, true, 200),
        h2(13, 2, true, 140),
        h3(10, 1, false, 80);
        
        public final int fontSize;
        public final int space;
        public final boolean isCenter;
        public final int width;

        Header(int fontSize, int space, boolean isCenter, int width) {
            this.fontSize = fontSize;
            this.space = space;
            this.isCenter = isCenter;
            this.width = width;
        }
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent, com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent
    public ILayoutComponent fromXml(Element element) {
        this.header = Header.valueOf(element.getTagName());
        this.isCenter = this.header.isCenter;
        this.fontSize = this.header.fontSize;
        this.space = this.header.space;
        this.bottomMargin = 3;
        this.text = XmlUtils.getContent(element, true);
        this.space = XmlUtils.getAsInt(element, "space", this.space);
        this.fontSize = XmlUtils.getAsInt(element, "font-size", this.fontSize);
        this.fontColor = XmlUtils.getAsColor(element, "font-color", this.fontColor);
        if (element.hasAttribute("isCenter")) {
            this.isCenter = XmlUtils.getAsBoolean(element, "isCenter", true);
        }
        if (element.hasAttribute("isShadow")) {
            this.isShadow = XmlUtils.getAsBoolean(element, "isShadow", true);
        }
        return super.fromXml(element);
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent
    @OnlyIn(Dist.CLIENT)
    protected LayoutPageWidget addWidgets(LayoutPageWidget currentPage) {
        int pageWidth = width(currentPage);
        List<String> textLines = new ArrayList<>();
        Font font = Minecraft.m_91087_().f_91062_;
        List<String> content = Arrays.stream(I18n.m_118938_(this.text, new Object[0]).split("\n")).toList();
        for (String textLine : content) {
            if (textLine.isEmpty()) {
                textLines.add(" ");
            } else {
                textLines.addAll(font.m_92865_().m_92432_(textLine, pageWidth, Style.f_131099_).stream().map((v0) -> {
                    return v0.getString();
                }).toList());
            }
        }
        LayoutPageWidget currentPage2 = currentPage.addStreamWidget(wrapper(new TextBoxWidget(0, 0, pageWidth, textLines).setShadow(this.isShadow).setCenter(this.isCenter).setFontColor(this.fontColor).setFontSize(this.fontSize).setSpace(this.space)));
        WidgetGroup group = new WidgetGroup(0, 0, pageWidth, 3);
        group.addWidget(new ImageWidget(this.isCenter ? (pageWidth - this.header.width) / 2 : 0, 0, this.header.width, 2, ColorPattern.WHITE.rectTexture()));
        return currentPage2.addStreamWidget(wrapper(group));
    }
}
