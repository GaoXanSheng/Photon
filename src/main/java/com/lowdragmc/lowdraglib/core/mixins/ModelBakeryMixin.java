package com.lowdragmc.lowdraglib.core.mixins;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ModelBakery.class})
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/core/mixins/ModelBakeryMixin.class */
public abstract class ModelBakeryMixin {
    @Shadow
    @Final
    private Map<ResourceLocation, UnbakedModel> f_119212_;

    @Shadow
    protected abstract void m_119352_(ResourceLocation resourceLocation, UnbakedModel unbakedModel);

    @Shadow
    protected abstract BlockModel m_119364_(ResourceLocation resourceLocation) throws IOException;

    @Redirect(method = {"getModel"}, at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V"))
    protected void injectStateToModelLocation(Logger logger, String string, Object[] objects) {
        String location = objects[0].toString();
        if (location.endsWith("#inventory") && (Registry.f_122827_.m_7745_(new ResourceLocation(location.substring(0, location.length() - "#inventory".length()))) instanceof IItemRendererProvider)) {
            return;
        }
        logger.warn(location, objects);
    }

    @Inject(method = {"loadModel"}, at = {@At("HEAD")}, cancellable = true)
    protected void injectLoadModel(ResourceLocation blockstateLocation, CallbackInfo ci) {
        if (blockstateLocation instanceof ModelResourceLocation) {
            ModelResourceLocation modelResourceLocation = (ModelResourceLocation) blockstateLocation;
            if (!Objects.equals(modelResourceLocation.m_119448_(), "inventory")) {
                ResourceLocation resourceLocation = new ResourceLocation(blockstateLocation.m_135827_(), blockstateLocation.m_135815_());
                Block block = (Block) Registry.f_122824_.m_7745_(resourceLocation);
                if (block instanceof IBlockRendererProvider) {
                    UnbakedModel model = this.f_119212_.computeIfAbsent(new ResourceLocation("ldlib:block/renderer_model"), modelLocation -> {
                        try {
                            return ModelFactory.getLDLibModel(m_119364_(modelLocation));
                        } catch (IOException e) {
                            LDLib.LOGGER.error("Couldn't load ldlib:renderer_model", e);
                            return this.f_119212_.get(ModelBakery.f_119230_);
                        }
                    });
                    m_119352_(modelResourceLocation, model);
                    ci.cancel();
                }
            }
        }
    }
}
