package com.lowdragmc.lowdraglib.gui.compass.component.animation;

import com.lowdragmc.lowdraglib.utils.BlockInfo;
import com.lowdragmc.lowdraglib.utils.BlockPosFace;
import com.lowdragmc.lowdraglib.utils.EntityInfo;
import com.lowdragmc.lowdraglib.utils.XmlUtils;
import expr.Expr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/compass/component/animation/SceneAction.class */
public class SceneAction extends Action {
    private final List<Tuple<BlockAnima, BlockInfo>> addedBlocks;
    private final List<BlockAnima> removedBlocks;
    private final Map<BlockPos, BlockInfo> modifiedTags;
    private final Map<BlockPosFace, Integer> highlightedBlocks;
    private final List<Tuple<EntityInfo, Vec3>> addedEntities;
    private final List<Tuple<EntityInfo, Vec3>> modifiedEntities;
    private final List<Tuple<EntityInfo, Boolean>> removedEntities;
    private final Map<Vec3, MutableTriple<Tuple<XmlUtils.SizedIngredient, List<Component>>, Vec2, Integer>> tooltipBlocks;
    private Float rotation;
    private int duration;

    public SceneAction() {
        this.addedBlocks = new ArrayList();
        this.removedBlocks = new ArrayList();
        this.modifiedTags = new HashMap();
        this.highlightedBlocks = new HashMap();
        this.addedEntities = new ArrayList();
        this.modifiedEntities = new ArrayList();
        this.removedEntities = new ArrayList();
        this.tooltipBlocks = new HashMap();
        this.duration = -1;
    }

    public SceneAction(Element element) {
        super(element);
        this.addedBlocks = new ArrayList();
        this.removedBlocks = new ArrayList();
        this.modifiedTags = new HashMap();
        this.highlightedBlocks = new HashMap();
        this.addedEntities = new ArrayList();
        this.modifiedEntities = new ArrayList();
        this.removedEntities = new ArrayList();
        this.tooltipBlocks = new HashMap();
        this.duration = -1;
        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element data = (Element) node;
                String nodeName = data.getNodeName();
                BlockPos blockPos = XmlUtils.getAsBlockPos(data, "pos", BlockPos.f_121853_);
                Vec3 pos = XmlUtils.getAsVec3(data, "pos", Vec3.f_82478_);
                boolean z = true;
                switch (nodeName.hashCode()) {
                    case -1140076541:
                        if (nodeName.equals("tooltip")) {
                            z = true;
                            break;
                        }
                        break;
                    case -1068795718:
                        if (nodeName.equals("modify")) {
                            z = true;
                            break;
                        }
                        break;
                    case -934610812:
                        if (nodeName.equals("remove")) {
                            z = true;
                            break;
                        }
                        break;
                    case -681210700:
                        if (nodeName.equals("highlight")) {
                            z = true;
                            break;
                        }
                        break;
                    case -484476596:
                        if (nodeName.equals("remove-entity")) {
                            z = true;
                            break;
                        }
                        break;
                    case -40300674:
                        if (nodeName.equals("rotation")) {
                            z = true;
                            break;
                        }
                        break;
                    case 96417:
                        if (nodeName.equals("add")) {
                            z = false;
                            break;
                        }
                        break;
                    case 228265615:
                        if (nodeName.equals("add-entity")) {
                            z = true;
                            break;
                        }
                        break;
                    case 1796108374:
                        if (nodeName.equals("modify-entity")) {
                            z = true;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                        this.addedBlocks.add(new Tuple<>(new BlockAnima(blockPos, XmlUtils.getAsVec3(data, "offset", new Vec3(0.0d, 0.7d, 0.0d)), XmlUtils.getAsInt(data, "duration", 15)), XmlUtils.getBlockInfo(data)));
                        continue;
                    case true:
                        this.removedBlocks.add(new BlockAnima(blockPos, XmlUtils.getAsVec3(data, "offset", new Vec3(0.0d, 0.7d, 0.0d)), XmlUtils.getAsInt(data, "duration", 15)));
                        continue;
                    case true:
                        this.modifiedTags.put(blockPos, XmlUtils.getBlockInfo(data));
                        continue;
                    case true:
                        this.addedEntities.add(new Tuple<>(XmlUtils.getEntityInfo(data), pos));
                        continue;
                    case true:
                        this.modifiedEntities.add(new Tuple<>(XmlUtils.getEntityInfo(data), XmlUtils.getAsVec3(data, "pos", null)));
                        continue;
                    case Expr.ATAN2 /* 5 */:
                        this.removedEntities.add(new Tuple<>(XmlUtils.getEntityInfo(data), Boolean.valueOf(XmlUtils.getAsBoolean(data, "force", false))));
                        continue;
                    case Expr.MAX /* 6 */:
                        this.rotation = Float.valueOf(XmlUtils.getAsFloat(data, "degree", 0.0f));
                        continue;
                    case Expr.MIN /* 7 */:
                        this.highlightedBlocks.put(new BlockPosFace(blockPos, XmlUtils.getAsEnum(data, "face", Direction.class, null)), Integer.valueOf(XmlUtils.getAsInt(data, "duration", 40)));
                        continue;
                    case true:
                        this.tooltipBlocks.put(XmlUtils.getAsVec3(data, "pos", new Vec3(0.0d, 0.0d, 0.0d)), MutableTriple.of(new Tuple(XmlUtils.getIngredient(data), new ArrayList(XmlUtils.getComponents(data, Style.f_131099_))), XmlUtils.getAsVec2(data, "screen-offset", new Vec2(0.3f, 0.3f)), Integer.valueOf(XmlUtils.getAsInt(data, "duration", 40))));
                        continue;
                }
            }
        }
    }

    public SceneAction rotation(Float rotation) {
        this.rotation = rotation;
        return this;
    }

    public SceneAction addedBlock(BlockPos pos, BlockInfo blockInfo, Vec3 offset, int duration) {
        this.addedBlocks.add(new Tuple<>(new BlockAnima(pos, offset, duration), blockInfo));
        return this;
    }

    public SceneAction removedBlock(BlockPos pos, Vec3 offset, int duration) {
        this.removedBlocks.add(new BlockAnima(pos, offset, duration));
        return this;
    }

    public SceneAction modifiedTag(BlockPos pos, BlockInfo blockInfo) {
        this.modifiedTags.put(pos, blockInfo);
        return this;
    }

    public SceneAction highlightedBlock(BlockPos pos, Direction face, int duration) {
        this.highlightedBlocks.put(new BlockPosFace(pos, face), Integer.valueOf(duration));
        return this;
    }

    public SceneAction addedEntity(EntityInfo entityInfo, Vec3 pos) {
        this.addedEntities.add(new Tuple<>(entityInfo, pos));
        return this;
    }

    public SceneAction modifiedEntity(EntityInfo entityInfo, Vec3 pos) {
        this.modifiedEntities.add(new Tuple<>(entityInfo, pos));
        return this;
    }

    public SceneAction removedEntity(EntityInfo entityInfo, boolean force) {
        this.removedEntities.add(new Tuple<>(entityInfo, Boolean.valueOf(force)));
        return this;
    }

    public SceneAction tooltip(Vec3 pos, Tuple<XmlUtils.SizedIngredient, List<Component>> tooltip, Vec2 screenOffset, int duration) {
        this.tooltipBlocks.put(pos, MutableTriple.of(tooltip, screenOffset, Integer.valueOf(duration)));
        return this;
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.animation.Action
    public int getDuration() {
        if (this.duration == -1) {
            this.duration = 0;
            for (Tuple<BlockAnima, BlockInfo> tuple : this.addedBlocks) {
                this.duration = Math.max(this.duration, ((BlockAnima) tuple.m_14418_()).duration());
            }
            for (BlockAnima block : this.removedBlocks) {
                this.duration = Math.max(this.duration, block.duration());
            }
            for (Map.Entry<BlockPosFace, Integer> entry : this.highlightedBlocks.entrySet()) {
                this.duration = Math.max(this.duration, entry.getValue().intValue());
            }
            for (Map.Entry<Vec3, MutableTriple<Tuple<XmlUtils.SizedIngredient, List<Component>>, Vec2, Integer>> entry2 : this.tooltipBlocks.entrySet()) {
                this.duration = Math.max(this.duration, ((Integer) entry2.getValue().getRight()).intValue());
            }
        }
        return this.duration + 5;
    }

    @Override // com.lowdragmc.lowdraglib.gui.compass.component.animation.Action
    public void performAction(AnimationFrame frame, CompassScene scene, boolean anima) {
        for (Tuple<BlockAnima, BlockInfo> tuple : this.addedBlocks) {
            BlockInfo blockInfo = (BlockInfo) tuple.m_14419_();
            blockInfo.clearBlockEntityCache();
            scene.addBlock(((BlockAnima) tuple.m_14418_()).pos(), blockInfo, anima ? (BlockAnima) tuple.m_14418_() : null);
        }
        for (BlockInfo blockInfo2 : this.modifiedTags.values()) {
            blockInfo2.clearBlockEntityCache();
        }
        for (BlockAnima block : this.removedBlocks) {
            scene.removeBlock(block.pos(), anima ? block : null);
        }
        for (Map.Entry<BlockPos, BlockInfo> entry : this.modifiedTags.entrySet()) {
            scene.addBlock(entry.getKey(), entry.getValue(), null);
        }
        for (Tuple<EntityInfo, Vec3> tuple2 : this.addedEntities) {
            Vec3 pos = (Vec3) tuple2.m_14419_();
            scene.addEntity((EntityInfo) tuple2.m_14418_(), pos, false);
        }
        for (Tuple<EntityInfo, Vec3> tuple3 : this.modifiedEntities) {
            Vec3 pos2 = (Vec3) tuple3.m_14419_();
            scene.addEntity((EntityInfo) tuple3.m_14418_(), pos2, true);
        }
        for (Tuple<EntityInfo, Boolean> tuple4 : this.removedEntities) {
            scene.removeEntity((EntityInfo) tuple4.m_14418_(), ((Boolean) tuple4.m_14419_()).booleanValue());
        }
        if (anima) {
            for (Map.Entry<BlockPosFace, Integer> entry2 : this.highlightedBlocks.entrySet()) {
                scene.highlightBlock(entry2.getKey(), entry2.getValue().intValue());
            }
        }
        if (anima) {
            for (Map.Entry<Vec3, MutableTriple<Tuple<XmlUtils.SizedIngredient, List<Component>>, Vec2, Integer>> entry3 : this.tooltipBlocks.entrySet()) {
                scene.addTooltip(entry3.getKey(), (Tuple) entry3.getValue().getLeft(), (Vec2) entry3.getValue().getMiddle(), (Integer) entry3.getValue().getRight());
            }
        }
        if (this.rotation != null) {
            scene.rotate(this.rotation.floatValue(), anima);
        }
    }
}
