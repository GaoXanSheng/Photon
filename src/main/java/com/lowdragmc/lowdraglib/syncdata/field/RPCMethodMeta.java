package com.lowdragmc.lowdraglib.syncdata.field;

import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.IAccessor;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.accessor.ManagedAccessor;
import com.lowdragmc.lowdraglib.syncdata.managed.ManagedHolder;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.rpc.RPCSender;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/field/RPCMethodMeta.class */
public class RPCMethodMeta {
    private final String name;
    private final ManagedAccessor[] argsAccessor;
    private final Class<?>[] argsType;
    private final Method method;
    private final boolean isFirstArgSender;

    public String getName() {
        return this.name;
    }

    public RPCMethodMeta(Method method) {
        this.method = method;
        method.setAccessible(true);
        this.name = method.getName();
        Parameter[] args = method.getParameters();
        if (args.length == 0) {
            this.argsAccessor = new ManagedAccessor[0];
            this.argsType = new Class[0];
            this.isFirstArgSender = false;
            return;
        }
        Parameter firstArg = args[0];
        if (RPCSender.class.isAssignableFrom(firstArg.getType())) {
            this.argsAccessor = new ManagedAccessor[args.length - 1];
            this.argsType = new Class[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                Parameter arg = args[i];
                this.argsAccessor[i - 1] = getAccessor(arg.getType());
                this.argsType[i - 1] = arg.getType();
            }
            this.isFirstArgSender = true;
            return;
        }
        this.argsAccessor = new ManagedAccessor[args.length];
        this.argsType = new Class[args.length];
        for (int i2 = 0; i2 < args.length; i2++) {
            Parameter arg2 = args[i2];
            this.argsAccessor[i2] = getAccessor(arg2.getType());
            this.argsType[i2] = arg2.getType();
        }
        this.isFirstArgSender = false;
    }

    public void invoke(Object instance, RPCSender sender, ITypedPayload<?>[] payloads) {
        Object[] args;
        if (this.argsAccessor.length != payloads.length) {
            throw new IllegalArgumentException("Invalid number of arguments, expected " + this.argsAccessor.length + " but got " + payloads.length);
        }
        if (this.isFirstArgSender) {
            args = new Object[this.argsAccessor.length + 1];
            args[0] = sender;
            for (int i = 0; i < this.argsAccessor.length; i++) {
                args[i + 1] = deserialize(payloads[i], this.argsType[i], this.argsAccessor[i]);
            }
        } else {
            args = new Object[this.argsAccessor.length];
            for (int i2 = 0; i2 < this.argsAccessor.length; i2++) {
                args[i2] = deserialize(payloads[i2], this.argsType[i2], this.argsAccessor[i2]);
            }
        }
        try {
            this.method.invoke(instance, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ITypedPayload<?>[] serializeArgs(Object[] args) {
        if (this.argsAccessor.length != args.length) {
            throw new IllegalArgumentException("Invalid number of arguments, expected " + this.argsAccessor.length + " but got " + args.length);
        }
        ITypedPayload[] payloads = new ITypedPayload[this.argsAccessor.length];
        for (int i = 0; i < this.argsAccessor.length; i++) {
            payloads[i] = serialize(args[i], this.argsAccessor[i]);
        }
        return payloads;
    }

    private static ManagedAccessor getAccessor(Type type) {
        IAccessor accessor = TypedPayloadRegistries.findByType(type);
        if (accessor == null) {
            throw new IllegalArgumentException("Cannot find accessor for type " + type);
        }
        if (accessor instanceof ManagedAccessor) {
            return (ManagedAccessor) accessor;
        }
        throw new IllegalArgumentException("Accessor for type " + type + " is not a ManagedAccessor");
    }

    private static Object deserialize(ITypedPayload<?> payload, Class<?> type, ManagedAccessor accessor) {
        ManagedHolder<?> cache = ManagedHolder.ofType(type);
        accessor.writeManagedField(AccessorOp.PERSISTED, cache, payload);
        return cache.value();
    }

    private static ITypedPayload<?> serialize(Object value, ManagedAccessor accessor) {
        ManagedHolder<Object> cache = ManagedHolder.of(value);
        return accessor.readManagedField(AccessorOp.PERSISTED, cache);
    }
}
