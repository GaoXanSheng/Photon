package com.lowdragmc.lowdraglib.utils.forge;

import com.lowdragmc.lowdraglib.LDLib;
import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/forge/ReflectionUtilsImpl.class */
public class ReflectionUtilsImpl {
    public static <A extends Annotation> void findAnnotationClasses(Class<A> annotationClass, Consumer<Class<?>> consumer, Runnable onFinished) {
        Type annotationType = Type.getType(annotationClass);
        for (ModFileScanData data : ModList.get().getAllScanData()) {
            for (ModFileScanData.AnnotationData annotation : data.getAnnotations()) {
                if (annotationType.equals(annotation.annotationType())) {
                    try {
                        consumer.accept(Class.forName(annotation.memberName(), false, ReflectionUtilsImpl.class.getClassLoader()));
                    } catch (Throwable throwable) {
                        LDLib.LOGGER.error("Failed to load class for notation: " + annotation.memberName(), throwable);
                    }
                }
            }
        }
        onFinished.run();
    }
}
