package com.lowdragmc.photon.client.particle;

import com.lowdragmc.lowdraglib.utils.Vector3;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/particle/BeamParticle.class */
public abstract class BeamParticle extends LParticle {
    protected Vector3 end;
    protected float width;
    protected float emit;
    @Nullable
    protected BiFunction<LParticle, Float, Float> dynamicWidth;
    @Nullable
    protected BiFunction<LParticle, Float, Float> dynamicEmit;

    public Vector3 getEnd() {
        return this.end;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getWidth() {
        return this.width;
    }

    public void setEmit(float emit) {
        this.emit = emit;
    }

    public float getEmit() {
        return this.emit;
    }

    public void setDynamicWidth(@Nullable BiFunction<LParticle, Float, Float> dynamicWidth) {
        this.dynamicWidth = dynamicWidth;
    }

    public void setDynamicEmit(@Nullable BiFunction<LParticle, Float, Float> dynamicEmit) {
        this.dynamicEmit = dynamicEmit;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BeamParticle(ClientLevel level, Vector3 from, Vector3 end) {
        super(level, from.x, from.y, from.z);
        this.dynamicWidth = null;
        this.dynamicEmit = null;
        this.moveless = true;
        setBeam(from, end);
        this.width = 0.2f;
    }

    public void setBeam(Vector3 from, Vector3 end) {
        setPos(from, true);
        this.end = end;
    }

    @Override // com.lowdragmc.photon.client.particle.LParticle
    public void renderInternal(@Nonnull VertexConsumer pBuffer, @Nonnull Camera camera, float partialTicks) {
        Vector3 toO;
        Vector3 cameraPos = new Vector3(camera.m_90583_());
        Vector3 from = getPos(partialTicks);
        Vector3 end = new Vector3(from).add(this.end);
        float offset = (-getEmit(partialTicks)) * (getAge() + partialTicks);
        float u0 = getU0(partialTicks) + offset;
        float u1 = getU1(partialTicks) + offset;
        float v0 = getV0(partialTicks);
        float v1 = getV1(partialTicks);
        float beamHeight = getWidth(partialTicks);
        int light = this.dynamicLight == null ? getLight(partialTicks) : this.dynamicLight.apply(this, Float.valueOf(partialTicks)).intValue();
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
        Vector3 direction = end.copy().subtract(from);
        Quaternion quaternion = getQuaternionSupplier().get();
        if (quaternion == null) {
            toO = new Vector3(from).subtract(cameraPos);
        } else {
            Vector3f rotation = quaternion.m_175217_();
            Vector3 zVec = Vector3.Z.copy();
            toO = zVec.rotate(rotation.m_122239_(), Vector3.X).rotate(rotation.m_122260_(), Vector3.Y).rotate(rotation.m_122269_(), Vector3.Z);
        }
        Vector3 n = new Vector3(toO).crossProduct(direction).normalize().multiply(beamHeight);
        Vector3 p0 = from.copy().add(n).subtract(cameraPos);
        Vector3 p1 = from.copy().add(n.multiply(-1.0d)).subtract(cameraPos);
        Vector3 p3 = end.copy().add(n).subtract(cameraPos);
        Vector3 p4 = end.copy().add(n.multiply(-1.0d)).subtract(cameraPos);
        pBuffer.m_5483_(p1.x, p1.y, p1.z).m_7421_(u0, v0).m_85950_(r, g, b, a).m_85969_(light).m_5752_();
        pBuffer.m_5483_(p0.x, p0.y, p0.z).m_7421_(u0, v1).m_85950_(r, g, b, a).m_85969_(light).m_5752_();
        pBuffer.m_5483_(p4.x, p4.y, p4.z).m_7421_(u1, v1).m_85950_(r, g, b, a).m_85969_(light).m_5752_();
        pBuffer.m_5483_(p3.x, p3.y, p3.z).m_7421_(u1, v0).m_85950_(r, g, b, a).m_85969_(light).m_5752_();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getWidth(float pPartialTicks) {
        if (this.dynamicWidth != null) {
            return this.dynamicWidth.apply(this, Float.valueOf(pPartialTicks)).floatValue();
        }
        return this.width;
    }

    protected float getEmit(float pPartialTicks) {
        if (this.dynamicEmit != null) {
            return this.dynamicEmit.apply(this, Float.valueOf(pPartialTicks)).floatValue();
        }
        return this.emit;
    }
}
