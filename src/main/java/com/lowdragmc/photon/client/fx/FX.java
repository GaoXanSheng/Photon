package com.lowdragmc.photon.client.fx;

import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/fx/FX.class */
public final class FX extends Record {
    private final ResourceLocation location;
    private final List<IParticleEmitter> emitters;
    private final CompoundTag rawData;

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
    /*  JADX ERROR: Failed to decode insn: 0x0001: INVOKE_CUSTOM, method: com.lowdragmc.photon.client.fx.FX.toString():java.lang.String
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
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:115)
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
        throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.photon.client.fx.FX.toString():java.lang.String");
    }

    public FX(ResourceLocation location, List<IParticleEmitter> emitters, CompoundTag rawData) {
        this.location = location;
        this.emitters = emitters;
        this.rawData = rawData;
    }

    public ResourceLocation location() {
        return this.location;
    }

    public List<IParticleEmitter> emitters() {
        return this.emitters;
    }

    public CompoundTag rawData() {
        return this.rawData;
    }

    public Collection<? extends IParticleEmitter> generateEmitters() {
        List<IParticleEmitter> list = new ArrayList<>(this.emitters.size());
        for (IParticleEmitter emitter : this.emitters) {
            list.add(emitter.copy());
        }
        return list;
    }

    public int hashCode() {
        return this.location.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof FX) {
            FX fx = (FX) obj;
            return fx.location.equals(this.location);
        }
        return false;
    }
}
