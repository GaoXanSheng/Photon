package com.lowdragmc.lowdraglib.side.fluid.forge;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/side/fluid/forge/FluidTransferWrapper.class */
public final class FluidTransferWrapper extends Record implements IFluidTransfer {
    private final IFluidHandler handler;

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
    /*  JADX ERROR: Failed to decode insn: 0x0001: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.side.fluid.forge.FluidTransferWrapper.toString():java.lang.String
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
        throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.side.fluid.forge.FluidTransferWrapper.toString():java.lang.String");
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
    /*  JADX ERROR: Failed to decode insn: 0x0001: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.side.fluid.forge.FluidTransferWrapper.hashCode():int
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
    public final int hashCode() {
        /*
            r2 = this;
            r0 = r2
            // decode failed: Can't encode constant CLASS as encoded value
            r1 = -1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.side.fluid.forge.FluidTransferWrapper.hashCode():int");
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
    /*  JADX ERROR: Failed to decode insn: 0x0002: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.side.fluid.forge.FluidTransferWrapper.equals(java.lang.Object):boolean
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
    public final boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r3
            r1 = r4
            // decode failed: Can't encode constant CLASS as encoded value
            r2 = -1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.side.fluid.forge.FluidTransferWrapper.equals(java.lang.Object):boolean");
    }

    public FluidTransferWrapper(IFluidHandler handler) {
        this.handler = handler;
    }

    public IFluidHandler getHandler() {
        return this.handler;
    }

    public IFluidHandler handler() {
        return this.handler;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public int getTanks() {
        return this.handler.getTanks();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public FluidStack getFluidInTank(int tank) {
        return FluidHelperImpl.toFluidStack(this.handler.getFluidInTank(tank));
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {
        throw new NotImplementedException("try to set fluid for a FluidTransfer wrapper");
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long getTankCapacity(int tank) {
        return this.handler.getTankCapacity(tank);
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return this.handler.isFluidValid(tank, FluidHelperImpl.toFluidStack(stack));
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long fill(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        throw new NotImplementedException("try to fill fluid for a FluidTransfer wrapper with a specific tank");
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public long fill(FluidStack resource, boolean simulate, boolean notifyChange) {
        return this.handler.fill(FluidHelperImpl.toFluidStack(resource), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public FluidStack drain(FluidStack resource, boolean simulate, boolean notifyChange) {
        return FluidHelperImpl.toFluidStack(this.handler.drain(FluidHelperImpl.toFluidStack(resource), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public Object createSnapshot() {
        return new Object();
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public void restoreFromSnapshot(Object snapshot) {
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean supportsFill(int tank) {
        return true;
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    @NotNull
    public FluidStack drain(int tank, FluidStack resource, boolean simulate, boolean notifyChanges) {
        throw new NotImplementedException("try to drain fluid for a FluidTransfer wrapper with a specific tank");
    }

    @Override // com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer
    public boolean supportsDrain(int tank) {
        return true;
    }
}
