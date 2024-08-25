package com.lowdragmc.lowdraglib.gui.compass.component;

import com.lowdragmc.lowdraglib.client.utils.RenderUtils;
import com.lowdragmc.lowdraglib.gui.compass.CompassView;
import com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent;
import com.lowdragmc.lowdraglib.gui.compass.LayoutPageWidget;
import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.SceneWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import com.lowdragmc.lowdraglib.utils.BlockPosFace;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.lowdragmc.lowdraglib.utils.XmlUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/SceneComponent.class */
public class SceneComponent extends AbstractComponent {
    protected final List<Map<BlockPos, BlockInfo>> pages = new ArrayList();
    protected final List<Map<BlockPos, String>> hoverInfos = new ArrayList();
    private boolean draggable = false;
    private boolean scalable = false;
    private boolean ortho = false;
    private float zoom = -1.0f;
    private float yaw = 25.0f;
    private int height = CompassView.LIST_WIDTH;
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int minZ = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    int maxZ = Integer.MIN_VALUE;

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent, com.lowdragmc.lowdraglib.gui.compass.ILayoutComponent
    public ILayoutComponent fromXml(Element element) {
        super.fromXml(element);
        this.draggable = XmlUtils.getAsBoolean(element, "draggable", this.draggable);
        this.scalable = XmlUtils.getAsBoolean(element, "scalable", this.scalable);
        this.ortho = !XmlUtils.getAsString(element, "camera", "ortho").equals("perspective");
        this.zoom = XmlUtils.getAsFloat(element, "zoom", this.zoom);
        this.yaw = XmlUtils.getAsFloat(element, "yaw", this.yaw);
        this.height = XmlUtils.getAsInt(element, "height", this.height);
        NodeList nodeList = element.getChildNodes();
        BlockPos offset = BlockPos.f_121853_;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element page = (Element) node;
                if (page.getNodeName().equals("page")) {
                    addPage(page, offset);
                    offset = offset.m_7918_(500, 0, 500);
                }
            }
        }
        return this;
    }

    private void addPage(Element element, BlockPos offset) {
        Map<BlockPos, BlockInfo> blocks = new HashMap<>();
        Map<BlockPos, String> contents = new HashMap<>();
        Object2BooleanOpenHashMap object2BooleanOpenHashMap = new Object2BooleanOpenHashMap();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element block = (Element) node;
                if (block.getNodeName().equals("block")) {
                    BlockPos pos = XmlUtils.getAsBlockPos(block, "pos", BlockPos.f_121853_).m_121955_(offset);
                    BlockInfo blockInfo = XmlUtils.getBlockInfo(block);
                    boolean itemTips = XmlUtils.getAsBoolean(block, "item-tips", false);
                    String content = XmlUtils.getContent(block, true);
                    blocks.put(pos, blockInfo);
                    contents.put(pos, content);
                    object2BooleanOpenHashMap.put(pos, itemTips);
                }
            }
        }
        this.pages.add(blocks);
        this.hoverInfos.add(contents);
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.AbstractComponent
    @OnlyIn(Dist.CLIENT)
    protected LayoutPageWidget addWidgets(LayoutPageWidget currentPage) {
        if (this.pages.isEmpty()) {
            return currentPage;
        }
        AtomicInteger pageNum = new AtomicInteger(0);
        WidgetGroup group = new WidgetGroup(0, 0, (currentPage.getSize().width - this.leftMargin) - this.rightMargin, this.height);
        TrackedDummyWorld world = new TrackedDummyWorld();
        for (Map<BlockPos, BlockInfo> blocks : this.pages) {
            world.addBlocks(blocks);
        }
        SceneWidget sceneWidget = new SceneWidget((((currentPage.getSize().width - this.leftMargin) - this.rightMargin) / 2) - (2 * this.height), 0, 4 * this.height, this.height, world);
        sceneWidget.setOnAddedTooltips(scene, list -> {
            BlockPosFace hoverPosFace = pageNum.getHoverPosFace();
            if (hoverPosFace == null) {
                return;
            }
            String hover = this.hoverInfos.get(pageNum.get()).getOrDefault(hoverPosFace.pos, "");
            if (!hover.isEmpty()) {
                list.add(Component.m_237113_(hover));
            }
        }).setHoverTips(true).useOrtho(this.ortho).setOrthoRange(0.5f).setScalable(this.scalable).setDraggable(this.draggable).setRenderFacing(false).setRenderSelect(false);
        sceneWidget.getRenderer().setFov(30.0f);
        group.addWidget(sceneWidget);
        sceneWidget.setRenderedCore(this.pages.stream().flatMap(page -> {
            return page.keySet().stream();
        }).toList(), null);
        sceneWidget.setBeforeWorldRender(renderer -> {
            PoseStack matrixStack = new PoseStack();
            matrixStack.m_85836_();
            RenderUtils.moveToFace(matrixStack, (this.minX + this.maxX) / 2.0f, this.minY, (this.minZ + this.maxZ) / 2.0f, Direction.DOWN);
            RenderUtils.rotateToFace(matrixStack, Direction.UP, null);
            int width = (this.maxX - this.minX) + 3;
            int height = (this.maxZ - this.minZ) + 3;
            new ResourceTexture("ldlib:textures/gui/darkened_slot.png").draw(matrixStack, 0, 0, width / (-2.0f), height / (-2.0f), width, height);
            matrixStack.m_85849_();
        });
        setPage(this.pages.get(0), sceneWidget);
        ButtonWidget left = (ButtonWidget) new ButtonWidget(20, this.height - 16, 12, 7, Icons.LEFT, null).setClientSideWidget();
        group.addWidget(left);
        ButtonWidget right = (ButtonWidget) new ButtonWidget((((currentPage.getSize().width - this.leftMargin) - this.rightMargin) - 20) - 12, this.height - 16, 12, 7, Icons.RIGHT, null).setClientSideWidget();
        group.addWidget(right);
        left.setVisible(pageNum.get() - 1 >= 0);
        right.setVisible(pageNum.get() + 1 < this.pages.size());
        left.setOnPressCallback(cd -> {
            if (pageNum.get() - 1 >= 0) {
                setPage(this.pages.get(pageNum.addAndGet(-1)), pageNum);
            }
            sceneWidget.setVisible(pageNum.get() - 1 >= 0);
            left.setVisible(pageNum.get() + 1 < this.pages.size());
        });
        right.setOnPressCallback(cd2 -> {
            if (pageNum.get() + 1 < this.pages.size()) {
                setPage(this.pages.get(pageNum.addAndGet(1)), pageNum);
            }
            sceneWidget.setVisible(pageNum.get() - 1 >= 0);
            left.setVisible(pageNum.get() + 1 < this.pages.size());
        });
        group.addWidget(new ButtonWidget(((((currentPage.getSize().width - this.leftMargin) - this.rightMargin) - 24) / 2) + 6, this.height - 19, 12, 12, Icons.ROTATION, cd3 -> {
            float current = sceneWidget.getRotationPitch();
            sceneWidget.setCameraYawAndPitchAnima(sceneWidget.getRotationYaw(), current + 90.0f, 20);
        }).setClientSideWidget());
        if (this.hoverInfo != null) {
            group.setHoverTooltips(this.hoverInfo);
        }
        return currentPage.addStreamWidget(wrapper(group));
    }

    private void setPage(Map<BlockPos, BlockInfo> blocks, SceneWidget sceneWidget) {
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.minZ = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
        for (BlockPos vPos : blocks.keySet()) {
            this.minX = Math.min(this.minX, vPos.m_123341_());
            this.minY = Math.min(this.minY, vPos.m_123342_());
            this.minZ = Math.min(this.minZ, vPos.m_123343_());
            this.maxX = Math.max(this.maxX, vPos.m_123341_());
            this.maxY = Math.max(this.maxY, vPos.m_123342_());
            this.maxY = Math.max(this.maxY, vPos.m_123342_());
            this.maxZ = Math.max(this.maxZ, vPos.m_123343_());
        }
        Vector3f center = new Vector3f(((this.minX + this.maxX) / 2.0f) + 0.5f, ((this.minY + this.maxY) / 2.0f) + 0.5f, ((this.minZ + this.maxZ) / 2.0f) + 0.5f);
        if (this.zoom > 0.0f) {
            sceneWidget.setZoom(this.zoom);
        } else {
            sceneWidget.setZoom((float) (15.0d * Math.sqrt(Math.max(Math.max(Math.max((this.maxX - this.minX) + 1, (this.maxY - this.minY) + 1), (this.maxZ - this.minZ) + 1), 1))));
        }
        sceneWidget.setCenter(center);
        sceneWidget.setCameraYawAndPitch(this.yaw, sceneWidget.getRotationPitch());
    }
}
