package com.lowdragmc.photon.client.particle;

import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.emitter.PhotonParticleRenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.function.TriFunction;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/particle/TrailParticle.class */
public abstract class TrailParticle extends LParticle {
    protected int maxTail;
    protected float width;
    private float minimumVertexDistance;
    private float squareDist;
    protected boolean dieWhenRemoved;
    protected UVMode uvMode;
    protected BiPredicate<TrailParticle, Vector3> onAddTail;
    protected Predicate<TrailParticle> onRemoveTails;
    protected TriFunction<TrailParticle, Integer, Float, Float> dynamicTailWidth;
    protected TriFunction<TrailParticle, Integer, Float, Vector4f> dynamicTailColor;
    protected TriFunction<LParticle, Integer, Float, Vector4f> dynamicTailUVs;
    protected LinkedList<Vector3> tails;

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/particle/TrailParticle$UVMode.class */
    public enum UVMode {
        Stretch,
        Tile
    }

    public void setMaxTail(int maxTail) {
        this.maxTail = maxTail;
    }

    public int getMaxTail() {
        return this.maxTail;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getWidth() {
        return this.width;
    }

    public float getMinimumVertexDistance() {
        return this.minimumVertexDistance;
    }

    public float getSquareDist() {
        return this.squareDist;
    }

    public void setDieWhenRemoved(boolean dieWhenRemoved) {
        this.dieWhenRemoved = dieWhenRemoved;
    }

    public boolean isDieWhenRemoved() {
        return this.dieWhenRemoved;
    }

    public void setUvMode(UVMode uvMode) {
        this.uvMode = uvMode;
    }

    public UVMode getUvMode() {
        return this.uvMode;
    }

    public void setOnAddTail(BiPredicate<TrailParticle, Vector3> onAddTail) {
        this.onAddTail = onAddTail;
    }

    public void setOnRemoveTails(Predicate<TrailParticle> onRemoveTails) {
        this.onRemoveTails = onRemoveTails;
    }

    public void setDynamicTailWidth(TriFunction<TrailParticle, Integer, Float, Float> dynamicTailWidth) {
        this.dynamicTailWidth = dynamicTailWidth;
    }

    public void setDynamicTailColor(TriFunction<TrailParticle, Integer, Float, Vector4f> dynamicTailColor) {
        this.dynamicTailColor = dynamicTailColor;
    }

    public void setDynamicTailUVs(TriFunction<LParticle, Integer, Float, Vector4f> dynamicTailUVs) {
        this.dynamicTailUVs = dynamicTailUVs;
    }

    public LinkedList<Vector3> getTails() {
        return this.tails;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TrailParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.minimumVertexDistance = 0.01f;
        this.squareDist = this.minimumVertexDistance * this.minimumVertexDistance;
        this.dieWhenRemoved = true;
        this.uvMode = UVMode.Stretch;
        this.tails = new LinkedList<>();
        this.maxTail = 40;
        this.width = 0.5f;
        this.cull = false;
    }

    protected TrailParticle(ClientLevel level, double x, double y, double z, double sX, double sY, double sZ) {
        super(level, x, y, z, sX, sY, sZ);
        this.minimumVertexDistance = 0.01f;
        this.squareDist = this.minimumVertexDistance * this.minimumVertexDistance;
        this.dieWhenRemoved = true;
        this.uvMode = UVMode.Stretch;
        this.tails = new LinkedList<>();
        this.cull = false;
    }

    public void setMinimumVertexDistance(float minimumVertexDistance) {
        this.minimumVertexDistance = minimumVertexDistance;
        this.squareDist = minimumVertexDistance * minimumVertexDistance;
    }

    protected boolean shouldAddTail(Vector3 newTail) {
        if (this.onAddTail != null) {
            return this.onAddTail.test(this, newTail);
        }
        return true;
    }

    protected void removeTails() {
        if (isRemoved()) {
            this.tails.pollFirst();
        } else if (this.onRemoveTails == null || !this.onRemoveTails.test(this)) {
            while (this.tails.size() > this.maxTail) {
                this.tails.removeFirst();
            }
        }
    }

    @Override // com.lowdragmc.photon.client.particle.LParticle
    public void m_5989_() {
        Vector3 last;
        if (this.delay > 0) {
            this.delay--;
            return;
        }
        updateOrigin();
        Vector3 tail = getTail();
        if (!isRemoved() && shouldAddTail(tail)) {
            boolean shouldAdd = true;
            if (this.squareDist > 0.0f && (last = this.tails.pollLast()) != null) {
                double distLast = tail.copy().subtract(last).magSquared();
                if (distLast < this.squareDist) {
                    shouldAdd = false;
                    this.tails.addLast(last);
                } else {
                    Vector3 last2 = this.tails.peekLast();
                    if (last2 != null) {
                        double distLast2 = tail.copy().subtract(last2).magSquared();
                        if (distLast2 < this.squareDist) {
                            shouldAdd = false;
                        } else if (distLast < distLast2) {
                            this.tails.addLast(last);
                        }
                    } else {
                        this.tails.addLast(last);
                    }
                }
            }
            if (shouldAdd) {
                addNewTail(tail);
            }
        }
        removeTails();
        int i = this.f_107224_;
        this.f_107224_ = i + 1;
        if (i >= this.f_107225_ && this.f_107225_ > 0) {
            m_107274_();
        }
        update();
        if (this.f_107225_ > 0) {
            this.t = (1.0f * this.f_107224_) / this.f_107225_;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addNewTail(Vector3 tail) {
        this.tails.add(tail);
    }

    public boolean m_107276_() {
        if (this.f_107220_) {
            return (this.dieWhenRemoved || this.tails.isEmpty()) ? false : true;
        }
        return true;
    }

    protected Vector3 getTail() {
        return new Vector3(this.f_107209_, this.f_107210_, this.f_107211_);
    }

    @Override // com.lowdragmc.photon.client.particle.LParticle
    public void renderInternal(@Nonnull VertexConsumer buffer, @Nonnull Camera camera, float partialTicks) {
        Vector3 pos = getPos(partialTicks);
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;
        Vector3 cameraPos = new Vector3(camera.m_90583_());
        float a = getAlpha(partialTicks);
        float r = getRed(partialTicks);
        float g = getGreen(partialTicks);
        float b = getBlue(partialTicks);
        if (this.dynamicColor != null) {
            Vector4f color = this.dynamicColor.apply(this, Float.valueOf(partialTicks));
            a *= color.m_123617_();
            r *= color.m_123601_();
            g *= color.m_123615_();
            b *= color.m_123616_();
        }
        int light = this.dynamicLight == null ? getLight(partialTicks) : this.dynamicLight.apply(this, Float.valueOf(partialTicks)).intValue();
        Vector3 fixedVec = null;
        Quaternion quaternion = getQuaternionSupplier().get();
        if (quaternion != null) {
            Vector3f rotation = quaternion.m_175217_();
            Vector3 zVec = Vector3.Z.copy();
            fixedVec = zVec.rotate(rotation.m_122239_(), Vector3.X).rotate(rotation.m_122260_(), Vector3.Y).rotate(rotation.m_122269_(), Vector3.Z);
        }
        boolean pushHead = true;
        if (this.tails.peekLast() != null && this.tails.peekLast().equals(new Vector3(x, y, z))) {
            pushHead = false;
        } else {
            this.tails.addLast(new Vector3(x, y, z));
        }
        Iterator<Vector3> iter = this.tails.iterator();
        Vector3 lastUp = null;
        Vector3 lastDown = null;
        Vector3 lastNormal = null;
        Vector3 tail = null;
        float la = a;
        float lr = r;
        float lg = g;
        float lb = b;
        int tailIndex = 0;
        while (iter.hasNext()) {
            Vector3 nextTail = iter.next();
            if (tail == null) {
                tail = nextTail;
            } else {
                float width = getWidth(tailIndex, partialTicks);
                Vector3 vec = nextTail.copy().subtract(tail);
                Vector3 toTail = fixedVec == null ? tail.copy().subtract(cameraPos) : fixedVec;
                Vector3 normal = vec.crossProduct(toTail).normalize();
                if (lastNormal == null) {
                    lastNormal = normal;
                }
                Vector3 avgNormal = lastNormal.add(normal).divide(2.0d);
                Vector3 up = tail.copy().add(avgNormal.copy().multiply(width)).subtract(cameraPos);
                Vector3 down = tail.copy().add(avgNormal.copy().multiply(-width)).subtract(cameraPos);
                float ta = a;
                float tr = r;
                float tg = g;
                float tb = b;
                if (this.dynamicTailColor != null) {
                    Vector4f color2 = (Vector4f) this.dynamicTailColor.apply(this, Integer.valueOf(tailIndex), Float.valueOf(partialTicks));
                    ta *= color2.m_123617_();
                    tr *= color2.m_123601_();
                    tg *= color2.m_123615_();
                    tb *= color2.m_123616_();
                }
                if (lastUp != null) {
                    Vector4f uvs = getUVs(tailIndex - 1, partialTicks);
                    float u0 = uvs.m_123601_();
                    float u1 = uvs.m_123616_();
                    float v0 = uvs.m_123615_();
                    float v1 = uvs.m_123617_();
                    pushBuffer(buffer, light, lastUp, lastDown, la, lr, lg, lb, u0, u1, v0, v1, ta, tr, tg, tb, up, down);
                }
                la = ta;
                lr = tr;
                lg = tg;
                lb = tb;
                lastUp = up;
                lastDown = down;
                tail = nextTail;
                lastNormal = normal;
                tailIndex++;
            }
        }
        if (tail != null && lastNormal != null) {
            float width2 = getWidth(tailIndex, partialTicks);
            Vector4f uvs2 = getUVs(tailIndex - 1, partialTicks);
            float u02 = uvs2.m_123601_();
            float u12 = uvs2.m_123616_();
            float v02 = uvs2.m_123615_();
            float v12 = uvs2.m_123617_();
            float ta2 = a;
            float tr2 = r;
            float tg2 = g;
            float tb2 = b;
            if (this.dynamicTailColor != null) {
                Vector4f color3 = (Vector4f) this.dynamicTailColor.apply(this, Integer.valueOf(tailIndex), Float.valueOf(partialTicks));
                ta2 *= color3.m_123617_();
                tr2 *= color3.m_123601_();
                tg2 *= color3.m_123615_();
                tb2 *= color3.m_123616_();
            }
            pushBuffer(buffer, light, lastUp, lastDown, la, lr, lg, lb, u02, u12, v02, v12, ta2, tr2, tg2, tb2, tail.copy().add(lastNormal.copy().multiply(width2)).subtract(cameraPos), tail.copy().add(lastNormal.copy().multiply(-width2)).subtract(cameraPos));
        }
        if (pushHead) {
            this.tails.pollLast();
        }
    }

    private void pushBuffer(@Nonnull VertexConsumer buffer, int light, Vector3 lastUp, Vector3 lastDown, float la, float lr, float lg, float lb, float u0, float u1, float v0, float v1, float ta, float tr, float tg, float tb, Vector3 up, Vector3 down) {
        buffer.m_5483_(down.x, down.y, down.z).m_7421_(u1, v1).m_85950_(tr, tg, tb, ta).m_85969_(light).m_5752_();
        buffer.m_5483_(up.x, up.y, up.z).m_7421_(u1, v0).m_85950_(tr, tg, tb, ta).m_85969_(light).m_5752_();
        buffer.m_5483_(lastUp.x, lastUp.y, lastUp.z).m_7421_(u0, v0).m_85950_(lr, lg, lb, la).m_85969_(light).m_5752_();
        buffer.m_5483_(lastUp.x, lastUp.y, lastUp.z).m_7421_(u0, v0).m_85950_(lr, lg, lb, la).m_85969_(light).m_5752_();
        buffer.m_5483_(lastDown.x, lastDown.y, lastDown.z).m_7421_(u0, v1).m_85950_(lr, lg, lb, la).m_85969_(light).m_5752_();
        buffer.m_5483_(down.x, down.y, down.z).m_7421_(u1, v1).m_85950_(tr, tg, tb, ta).m_85969_(light).m_5752_();
    }

    public Vector4f getUVs(int tailIndex, float partialTicks) {
        float u0;
        float u1;
        float v0;
        float v1;
        if (getUvMode() == UVMode.Stretch) {
            if (this.dynamicTailUVs != null) {
                Vector4f uvs = (Vector4f) this.dynamicTailUVs.apply(this, Integer.valueOf(tailIndex), Float.valueOf(partialTicks));
                u0 = uvs.m_123601_();
                v0 = uvs.m_123615_();
                u1 = uvs.m_123616_();
                v1 = uvs.m_123617_();
            } else {
                u0 = getU0(tailIndex, partialTicks);
                u1 = getU1(tailIndex, partialTicks);
                v0 = getV0(tailIndex, partialTicks);
                v1 = getV1(tailIndex, partialTicks);
            }
        } else if (this.dynamicUVs != null) {
            Vector4f uvs2 = this.dynamicUVs.apply(this, Float.valueOf(partialTicks));
            u0 = uvs2.m_123601_();
            v0 = uvs2.m_123615_();
            u1 = uvs2.m_123616_();
            v1 = uvs2.m_123617_();
        } else {
            u0 = getU0(partialTicks);
            u1 = getU1(partialTicks);
            v0 = getV0(partialTicks);
            v1 = getV1(partialTicks);
        }
        return new Vector4f(u0, v0, u1, v1);
    }

    public float getWidth(int tail, float pPartialTicks) {
        if (this.dynamicTailWidth != null) {
            return ((Float) this.dynamicTailWidth.apply(this, Integer.valueOf(tail), Float.valueOf(pPartialTicks))).floatValue();
        }
        return this.width;
    }

    protected float getU0(int tail, float pPartialTicks) {
        return tail / (this.tails.size() - 1.0f);
    }

    protected float getV0(int tail, float pPartialTicks) {
        return 0.0f;
    }

    protected float getU1(int tail, float pPartialTicks) {
        return (tail + 1) / (this.tails.size() - 1.0f);
    }

    protected float getV1(int tail, float pPartialTicks) {
        return 1.0f;
    }

    @Override // com.lowdragmc.photon.client.particle.LParticle
    public void resetParticle() {
        super.resetParticle();
        this.tails.clear();
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/particle/TrailParticle$Basic.class */
    public static class Basic extends TrailParticle {
        final PhotonParticleRenderType renderType;

        @Override // com.lowdragmc.photon.client.particle.LParticle
        /* renamed from: getRenderType */
        public PhotonParticleRenderType m_7556_() {
            return this.renderType;
        }

        public Basic(ClientLevel level, double x, double y, double z, PhotonParticleRenderType renderType) {
            super(level, x, y, z);
            this.renderType = renderType;
        }

        public Basic(ClientLevel level, double x, double y, double z, double sX, double sY, double sZ, PhotonParticleRenderType renderType) {
            super(level, x, y, z, sX, sY, sZ);
            this.renderType = renderType;
        }
    }
}
