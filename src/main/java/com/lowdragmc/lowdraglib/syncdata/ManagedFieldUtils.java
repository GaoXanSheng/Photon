package com.lowdragmc.lowdraglib.syncdata;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.DropSaved;
import com.lowdragmc.lowdraglib.syncdata.annotation.LazyManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RPCMethod;
import com.lowdragmc.lowdraglib.syncdata.annotation.ReadOnlyManaged;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedKey;
import com.lowdragmc.lowdraglib.syncdata.field.RPCMethodMeta;
import com.lowdragmc.lowdraglib.syncdata.managed.IRef;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/ManagedFieldUtils.class */
public class ManagedFieldUtils {

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/ManagedFieldUtils$FieldChangedCallback.class */
    public interface FieldChangedCallback {
        void onFieldChanged(IRef iRef, int i, boolean z);
    }

    public static ManagedKey[] getManagedFields(Class<?> clazz) {
        Field[] declaredFields;
        List<ManagedKey> managedFields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && (field.isAnnotationPresent(Persisted.class) || field.isAnnotationPresent(DescSynced.class))) {
                ManagedKey managedKey = createKey(field);
                managedFields.add(managedKey);
            }
        }
        return (ManagedKey[]) managedFields.toArray(x$0 -> {
            return new ManagedKey[x$0];
        });
    }

    public static Map<String, RPCMethodMeta> getRPCMethods(Class<?> clazz) {
        Method[] declaredMethods;
        Map<String, RPCMethodMeta> result = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (!Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(RPCMethod.class)) {
                RPCMethodMeta rpcMethod = new RPCMethodMeta(method);
                result.put(rpcMethod.getName(), rpcMethod);
            }
        }
        return result;
    }

    public static ManagedKey createKey(Field field) {
        boolean isLazy = field.isAnnotationPresent(LazyManaged.class);
        boolean isDestSync = field.isAnnotationPresent(DescSynced.class);
        boolean isPersist = field.isAnnotationPresent(Persisted.class);
        boolean isDrop = field.isAnnotationPresent(DropSaved.class);
        boolean isReadOnlyManaged = field.isAnnotationPresent(ReadOnlyManaged.class);
        String name = field.getName();
        Type type = field.getGenericType();
        ManagedKey managedKey = new ManagedKey(name, isDestSync, isPersist, isDrop, isLazy, type, field);
        if (isPersist) {
            Persisted persisted = (Persisted) field.getAnnotation(Persisted.class);
            managedKey.setPersistentKey(persisted.key());
        }
        if (isReadOnlyManaged) {
            ReadOnlyManaged readOnlyManaged = (ReadOnlyManaged) field.getAnnotation(ReadOnlyManaged.class);
            Class<?> clazz = field.getDeclaringClass();
            Class<?> rawType = field.getType();
            try {
                Method onDirtyMethod = clazz.getDeclaredMethod(readOnlyManaged.onDirtyMethod(), rawType);
                Method serializeMethod = clazz.getDeclaredMethod(readOnlyManaged.serializeMethod(), rawType);
                Method deserializeMethod = clazz.getDeclaredMethod(readOnlyManaged.deserializeMethod(), CompoundTag.class);
                onDirtyMethod.setAccessible(true);
                serializeMethod.setAccessible(true);
                deserializeMethod.setAccessible(true);
                managedKey.setRedOnlyManaged(onDirtyMethod, serializeMethod, deserializeMethod);
            } catch (NoSuchMethodException e) {
                LDLib.LOGGER.warn("No such methods for @ReadOnlyManaged field {}", field);
            }
        }
        return managedKey;
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/ManagedFieldUtils$FieldRefs.class */
    public static final class FieldRefs extends Record {
        private final IRef[] syncedRefs;
        private final IRef[] persistedRefs;
        private final IRef[] nonLazyFields;
        private final Map<ManagedKey, IRef> fieldRefMap;

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
        /*  JADX ERROR: Failed to decode insn: 0x0001: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.syncdata.ManagedFieldUtils.FieldRefs.toString():java.lang.String
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
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:419)
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
            throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.syncdata.ManagedFieldUtils.FieldRefs.toString():java.lang.String");
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
        /*  JADX ERROR: Failed to decode insn: 0x0001: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.syncdata.ManagedFieldUtils.FieldRefs.hashCode():int
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
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:419)
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
            throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.syncdata.ManagedFieldUtils.FieldRefs.hashCode():int");
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
        /*  JADX ERROR: Failed to decode insn: 0x0002: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.syncdata.ManagedFieldUtils.FieldRefs.equals(java.lang.Object):boolean
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
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:419)
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
            throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.syncdata.ManagedFieldUtils.FieldRefs.equals(java.lang.Object):boolean");
        }

        public FieldRefs(IRef[] syncedRefs, IRef[] persistedRefs, IRef[] nonLazyFields, Map<ManagedKey, IRef> fieldRefMap) {
            this.syncedRefs = syncedRefs;
            this.persistedRefs = persistedRefs;
            this.nonLazyFields = nonLazyFields;
            this.fieldRefMap = fieldRefMap;
        }

        public IRef[] syncedRefs() {
            return this.syncedRefs;
        }

        public IRef[] persistedRefs() {
            return this.persistedRefs;
        }

        public IRef[] nonLazyFields() {
            return this.nonLazyFields;
        }

        public Map<ManagedKey, IRef> fieldRefMap() {
            return this.fieldRefMap;
        }
    }

    public static FieldRefs getFieldRefs(ManagedKey[] keys, Object obj, FieldChangedCallback syncFieldChangedCallback, FieldChangedCallback persistedFieldChangedCallback) {
        List<IRef> syncedFields = new ArrayList<>();
        List<IRef> persistedFields = new ArrayList<>();
        List<IRef> nonLazyFields = new ArrayList<>();
        Map<ManagedKey, IRef> fieldRefMap = new HashMap<>();
        for (ManagedKey key : keys) {
            IRef fieldObj = key.createRef(obj);
            fieldObj.markAsDirty();
            fieldRefMap.put(key, fieldObj);
            if (!fieldObj.isLazy()) {
                nonLazyFields.add(fieldObj);
            }
            int syncIndex = -1;
            int persistIndex = -1;
            if (key.isDestSync()) {
                syncIndex = syncedFields.size();
                syncedFields.add(fieldObj);
            }
            if (key.isPersist()) {
                persistIndex = persistedFields.size();
                persistedFields.add(fieldObj);
            }
            int finalSyncIndex = syncIndex;
            int finalPersistIndex = persistIndex;
            fieldObj.setOnSyncListener(changed -> {
                syncFieldChangedCallback.onFieldChanged(fieldObj, finalSyncIndex, changed);
            });
            fieldObj.setOnPersistedListener(changed2 -> {
                persistedFieldChangedCallback.onFieldChanged(fieldObj, finalPersistIndex, changed2);
            });
        }
        return new FieldRefs((IRef[]) syncedFields.toArray(x$0 -> {
            return new IRef[x$0];
        }), (IRef[]) persistedFields.toArray(x$02 -> {
            return new IRef[x$02];
        }), (IRef[]) nonLazyFields.toArray(x$03 -> {
            return new IRef[x$03];
        }), fieldRefMap);
    }
}
