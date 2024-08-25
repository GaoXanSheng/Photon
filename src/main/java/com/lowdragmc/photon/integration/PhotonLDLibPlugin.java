package com.lowdragmc.photon.integration;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegisterClient;
import com.lowdragmc.lowdraglib.gui.editor.runtime.AnnotationDetector;
import com.lowdragmc.lowdraglib.plugin.ILDLibPlugin;
import com.lowdragmc.lowdraglib.plugin.LDLibPlugin;
import com.lowdragmc.lowdraglib.syncdata.IAccessor;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import com.lowdragmc.photon.Photon;
import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.client.emitter.data.shape.IShape;
import com.lowdragmc.photon.gui.editor.accessor.IShapeAccessor;
import com.lowdragmc.photon.gui.editor.accessor.NumberFunction3Accessor;
import com.lowdragmc.photon.gui.editor.accessor.NumberFunctionAccessor;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLibPlugin
public class PhotonLDLibPlugin implements ILDLibPlugin {
    public static final IAccessor NUMBER_FUNCTION_ACCESSOR = new NumberFunctionAccessor();
    public static final IAccessor NUMBER_FUNCTION3_ACCESSOR = new NumberFunction3Accessor();
    public static final IAccessor SHAPE_ACCESSOR = new IShapeAccessor();
    @OnlyIn(Dist.CLIENT)
    public static Map<String, AnnotationDetector.Wrapper<LDLRegisterClient, ? extends IParticleEmitter>> REGISTER_EMITTERS;
    public static Map<String, AnnotationDetector.Wrapper<LDLRegister, ? extends IShape>> REGISTER_SHAPES;

    @Override // com.lowdragmc.lowdraglib.plugin.ILDLibPlugin
    public void onLoad() {
        File fxLocation = new File(LDLib.getLDLibDir(), "assets/photon/fx");
        if (fxLocation.mkdirs()) {
            Photon.LOGGER.info("creat the photon fx folder");
        }
        TypedPayloadRegistries.register(NbtTagPayload.class, NbtTagPayload::new, NUMBER_FUNCTION_ACCESSOR, 1000);
        TypedPayloadRegistries.register(NbtTagPayload.class, NbtTagPayload::new, NUMBER_FUNCTION3_ACCESSOR, 1000);
        TypedPayloadRegistries.register(NbtTagPayload.class, NbtTagPayload::new, SHAPE_ACCESSOR, 1000);
        if (LDLib.isClient()) {
            REGISTER_EMITTERS = new HashMap();
            AnnotationDetector.scanClasses(LDLRegisterClient.class, IParticleEmitter.class, (v0, v1) -> {
                return AnnotationDetector.checkNoArgsConstructor(v0, v1);
            }, PhotonLDLibPlugin::toUINoArgsBuilder, PhotonLDLibPlugin::UIWrapperSorter, l -> {
                REGISTER_EMITTERS.putAll((Map) l.stream().collect(Collectors.toMap(w -> {
                    return ((LDLRegisterClient) w.annotation()).name();
                }, w2 -> {
                    return w2;
                })));
            });
        }
        REGISTER_SHAPES = new HashMap();
        AnnotationDetector.scanClasses(LDLRegister.class, IShape.class, (v0, v1) -> {
            return AnnotationDetector.checkNoArgsConstructor(v0, v1);
        }, AnnotationDetector::toUINoArgsBuilder, AnnotationDetector::UIWrapperSorter, l2 -> {
            REGISTER_SHAPES.putAll((Map) l2.stream().collect(Collectors.toMap(w -> {
                return ((LDLRegister) w.annotation()).name();
            }, w2 -> {
                return w2;
            })));
        });
    }

    public static <T> AnnotationDetector.Wrapper<LDLRegisterClient, T> toUINoArgsBuilder(Class<? extends T> clazz) {
        return new AnnotationDetector.Wrapper<>((LDLRegisterClient) clazz.getAnnotation(LDLRegisterClient.class), clazz, () -> {
            return AnnotationDetector.createNoArgsInstance(clazz);
        });
    }

    public static int UIWrapperSorter(AnnotationDetector.Wrapper<LDLRegisterClient, ?> a, AnnotationDetector.Wrapper<LDLRegisterClient, ?> b) {
        return b.annotation().priority() - a.annotation().priority();
    }
}
