package com.lowdragmc.lowdraglib.utils;

import com.lowdragmc.lowdraglib.utils.forge.ReflectionUtilsImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/ReflectionUtils.class */
public class ReflectionUtils {
    @ExpectPlatform.Transformed
    @ExpectPlatform
    public static <A extends Annotation> void findAnnotationClasses(Class<A> annotationClass, Consumer<Class<?>> consumer, Runnable onFinished) {
        ReflectionUtilsImpl.findAnnotationClasses(annotationClass, consumer, onFinished);
    }

    public static Class<?> getRawType(Type type, Class<?> fallback) {
        Class<?> rawType = getRawType(type);
        return rawType != null ? rawType : fallback;
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof GenericArrayType) {
            return getRawType(((GenericArrayType) type).getGenericComponentType());
        }
        if (type instanceof ParameterizedType) {
            return getRawType(((ParameterizedType) type).getRawType());
        }
        return null;
    }
}
