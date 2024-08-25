package com.lowdragmc.photon.client.emitter.data.shape;

import com.lowdragmc.lowdraglib.client.bakedpipeline.IQuadTransformer;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.gui.editor.configurator.WrapperConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.utils.Vector3;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/shape/MeshData.class */
public class MeshData implements ITagSerializable<CompoundTag>, IConfigurable {
    public String meshName = "";
    public final List<Vector3> vertices = new ArrayList();
    public final List<Edge> edges = new ArrayList();
    public final List<Triangle> triangles = new ArrayList();
    protected double edgeSumLength;
    protected double triangleSumArea;

    public double getEdgeSumLength() {
        return this.edgeSumLength;
    }

    public double getTriangleSumArea() {
        return this.triangleSumArea;
    }

    public MeshData() {
    }

    public MeshData(ResourceLocation modelLocation) {
        loadFromModel(modelLocation);
    }

    public void clear() {
        this.vertices.clear();
        this.edges.clear();
        this.triangles.clear();
        this.edgeSumLength = 0.0d;
        this.triangleSumArea = 0.0d;
        this.meshName = "";
    }

    public void loadFromModel(ResourceLocation modelLocation) {
        Direction[] values;
        RandomSource random = RandomSource.m_216327_();
        BakedModel bakedModel = ModelFactory.getUnBakedModel(modelLocation).m_7611_(ModelFactory.getModeBakery(), (v0) -> {
            return v0.m_119204_();
        }, BlockModelRotation.X0_Y0, modelLocation);
        ArrayList<BakedQuad> quads = new ArrayList<>(bakedModel.m_213637_((BlockState) null, (Direction) null, random));
        for (Direction side : Direction.values()) {
            quads.addAll(bakedModel.m_213637_((BlockState) null, side, random));
        }
        loadFromQuads(quads);
    }

    public void loadFromQuads(List<BakedQuad> quads) {
        clear();
        double sumLength = 0.0d;
        double sumArea = 0.0d;
        for (BakedQuad quad : quads) {
            int[] vertices = quad.m_111303_();
            Vector3[] points = new Vector3[4];
            for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
                int offset = (vertexIndex * IQuadTransformer.STRIDE) + IQuadTransformer.POSITION;
                points[vertexIndex] = new Vector3(Float.intBitsToFloat(vertices[offset]) - 0.5d, Float.intBitsToFloat(vertices[offset + 1]) - 0.5d, Float.intBitsToFloat(vertices[offset + 2]) - 0.5d);
                this.vertices.add(points[vertexIndex]);
            }
            sumLength = sumLength + addEdge(points[0], points[1]) + addEdge(points[1], points[2]) + addEdge(points[2], points[3]) + addEdge(points[3], points[0]) + addEdge(points[1], points[3]);
            sumArea = sumArea + addTriangle(points[0], points[1], points[2]) + addTriangle(points[2], points[3], points[0]);
        }
        this.edgeSumLength = sumLength;
        this.triangleSumArea = sumArea;
    }

    @Nullable
    public Vector3 getRandomVertex(float t) {
        if (this.vertices.isEmpty()) {
            return null;
        }
        return this.vertices.get((int) (this.vertices.size() * t));
    }

    @Nullable
    public Edge getRandomEdge(float t) {
        if (this.edges.isEmpty()) {
            return null;
        }
        double l = t * this.edgeSumLength;
        double cl = 0.0d;
        for (Edge edge : this.edges) {
            if (l <= edge.length + cl) {
                return edge;
            }
            cl += edge.length;
        }
        return this.edges.get(this.edges.size() - 1);
    }

    @Nullable
    public Triangle getRandomTriangle(float t) {
        if (this.triangles.isEmpty()) {
            return null;
        }
        double a = t * this.triangleSumArea;
        double ca = 0.0d;
        for (Triangle triangle : this.triangles) {
            if (a <= triangle.area + ca) {
                return triangle;
            }
            ca += triangle.area;
        }
        return this.triangles.get(this.triangles.size() - 1);
    }

    private double addEdge(Vector3 a, Vector3 b) {
        Edge ab = new Edge(a, b);
        if (ab.length > 0.0d) {
            this.edges.add(ab);
        }
        return ab.length;
    }

    private double addTriangle(Vector3 a, Vector3 b, Vector3 c) {
        Triangle abc = new Triangle(a, b, c);
        if (abc.area > 0.0d) {
            this.triangles.add(abc);
        }
        return abc.area;
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    /* renamed from: serializeNBT  reason: avoid collision after fix types in other method */
    public CompoundTag mo129serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (Vector3 vertex : this.vertices) {
            saveVector3(list, vertex);
        }
        tag.m_128365_("vertices", list);
        ListTag list2 = new ListTag();
        for (Edge edge : this.edges) {
            saveVector3(list2, edge.a);
            saveVector3(list2, edge.b);
        }
        tag.m_128365_("edges", list2);
        ListTag list3 = new ListTag();
        for (Triangle triangle : this.triangles) {
            saveVector3(list3, triangle.a);
            saveVector3(list3, triangle.b);
            saveVector3(list3, triangle.c);
        }
        tag.m_128365_("triangles", list3);
        tag.m_128350_("sl", (float) this.edgeSumLength);
        tag.m_128350_("sa", (float) this.triangleSumArea);
        tag.m_128359_("meshName", this.meshName);
        return tag;
    }

    private void saveVector3(ListTag list, Vector3 vec) {
        list.add(FloatTag.m_128566_((float) vec.x));
        list.add(FloatTag.m_128566_((float) vec.y));
        list.add(FloatTag.m_128566_((float) vec.z));
    }

    private Vector3 loadVector3(ListTag list, int index) {
        return new Vector3(list.m_128775_(index), list.m_128775_(index + 1), list.m_128775_(index + 2));
    }

    @Override // com.lowdragmc.lowdraglib.syncdata.ITagSerializable
    public void deserializeNBT(CompoundTag tag) {
        clear();
        ListTag list = tag.m_128437_("vertices", 5);
        for (int i = 0; i < list.size(); i += 3) {
            this.vertices.add(loadVector3(list, i));
        }
        ListTag list2 = tag.m_128437_("edges", 5);
        for (int i2 = 0; i2 < list2.size(); i2 += 6) {
            this.edges.add(new Edge(loadVector3(list2, i2), loadVector3(list2, i2 + 3)));
        }
        ListTag list3 = tag.m_128437_("triangles", 5);
        for (int i3 = 0; i3 < list3.size(); i3 += 9) {
            this.triangles.add(new Triangle(loadVector3(list3, i3), loadVector3(list3, i3 + 3), loadVector3(list3, i3 + 6)));
        }
        this.edgeSumLength = tag.m_128457_("sl");
        this.triangleSumArea = tag.m_128457_("sa");
        this.meshName = tag.m_128461_("meshName");
    }

    @Override // com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable
    public void buildConfigurator(ConfiguratorGroup father) {
        WrapperConfigurator wrapper = new WrapperConfigurator("", new ButtonWidget(0, 0, 200, 10, new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture().setRadius(5.0f), new TextTexture("meshName").setType(TextTexture.TextType.ROLL_ALWAYS).setWidth(200)), cd -> {
            File path = new File(Editor.INSTANCE.getWorkSpace(), "assets/ldlib/models");
            DialogWidget.showFileDialog(Editor.INSTANCE, "select a model", path, true, DialogWidget.suffixFilter(".json"), r -> {
                if (path != null && path.isFile()) {
                    String lastName = this.meshName;
                    ResourceLocation modelLocation = new ResourceLocation("ldlib:" + path.getPath().replace(path.getPath(), "").substring(1).replace(".json", "").replace('\\', '/'));
                    loadFromModel(modelLocation);
                    this.meshName = lastName;
                }
            });
        }));
        wrapper.setTips("click to select a minecraft model file.");
        father.addConfigurators(wrapper);
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/shape/MeshData$Edge.class */
    public static class Edge {
        public final Vector3 a;
        public final Vector3 b;
        public final double length;

        public Edge(Vector3 a, Vector3 b) {
            this.a = a;
            this.b = b;
            this.length = a.copy().subtract(b).mag();
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/shape/MeshData$Triangle.class */
    public static class Triangle {
        public final Vector3 a;
        public final Vector3 b;
        public final Vector3 c;
        public final double area;

        public Triangle(Vector3 a, Vector3 b, Vector3 c) {
            this.a = a;
            this.b = b;
            this.c = c;
            double nx = ((b.y - a.y) * (c.z - a.z)) - ((b.z - a.z) * (c.y - a.y));
            double ny = ((b.z - a.z) * (c.x - a.x)) - ((b.x - a.x) * (c.z - a.z));
            double nz = ((b.x - a.x) * (c.y - a.y)) - ((b.y - a.y) * (c.x - a.x));
            this.area = 0.5d * Math.sqrt((nx * nx) + (ny * ny) + (nz * nz));
        }
    }
}
