package com.lowdragmc.lowdraglib.client.model.forge;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lowdragmc.lowdraglib.client.model.custommodel.CustomBakedModel;
import com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraftforge.client.model.IModelBuilder;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.geometry.ISimpleModelGeometry;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Function;
/**
 * @author KilaBash
 * @date 2022/05/28
 * @implNote LDLModel, use vanilla way to improve model rendering
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LDLRendererModel implements ISimpleModelGeometry<LDLRendererModel> {
    public static final LDLRendererModel INSTANCE = new LDLRendererModel();

    private LDLRendererModel() {}

    @Override
    public BakedModel bake(IModelConfiguration iGeometryBakingContext, ModelBakery arg, Function<Material, TextureAtlasSprite> function, ModelState arg2, ItemOverrides arg3, ResourceLocation arg4) {
        return new RendererBakedModel();
    }

    @Override
    public void addQuads(IModelConfiguration owner, IModelBuilder<?> modelBuilder, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation) {

    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return null;
    }

    @Override
    public Collection<Material> getMaterials(IModelConfiguration iGeometryBakingContext, Function<ResourceLocation, UnbakedModel> function, Set<Pair<String, String>> set) {
        return Collections.emptyList();
    }

    public static final class RendererBakedModel implements BakedModel {

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, Random random) {
            return Collections.emptyList();
        }

        @Override
        public boolean useAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean usesBlockLight() {
            return false;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(MissingTextureAtlasSprite.getLocation());
        }

        @Override
        public ItemOverrides getOverrides() {
            return ItemOverrides.EMPTY;
        }

        // forge
        public static final ModelProperty<IRenderer> IRENDERER = new ModelProperty<>();
        public static final ModelProperty<BlockAndTintGetter> WORLD = new ModelProperty<>();
        public static final ModelProperty<BlockPos> POS = new ModelProperty<>();
        public static final ModelProperty<ModelDataMap> MODEL_DATA = new ModelProperty<>();

        public static final ThreadLocal<ModelDataMap> CURRENT_MODEL_DATA = new ThreadLocal<>();
        public static final ThreadLocal<RenderType> CURRENT_RENDER_TYPE = new ThreadLocal<>();

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelDataMap data, @Nullable RenderType renderType) {
            IRenderer renderer = data.getData(IRENDERER);
            BlockAndTintGetter world = data.getData(WORLD);
            BlockPos pos = data.getData(POS);
            if (renderer != null) {
                CURRENT_MODEL_DATA.set(data);
                CURRENT_RENDER_TYPE.set(renderType);
                var quads = renderer.renderModel(world, pos, state, side, rand);
                if (renderer.reBakeCustomQuads() && state != null && world != null && pos != null) {
                    return CustomBakedModel.reBakeCustomQuads(quads, world, pos, state, side, renderer.reBakeCustomQuadsOffset());
                }
                CURRENT_MODEL_DATA.remove();
                CURRENT_RENDER_TYPE.remove();
                return quads;
            }
            return Collections.emptyList();
        }

        @Override
        public boolean useAmbientOcclusion(BlockState state) {
            if (state.getBlock() instanceof IBlockRendererProvider rendererProvider) {
                IRenderer renderer = rendererProvider.getRenderer(state);
                if (renderer != null) {
                    return renderer.useAO(state);
                }
            }
            return useAmbientOcclusion();
        }


        @Override
        public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
            if (state.getBlock() instanceof IBlockRendererProvider rendererProvider) {
                IRenderer renderer = rendererProvider.getRenderer(state);
                if (renderer != null) {
                    modelData = ModelData.builder()
                            .with(IRENDERER, renderer)
                            .with(WORLD, level)
                            .with(POS, pos)
                            .with(MODEL_DATA, modelData)
                            .build();
                }
            }
            return modelData;
        }

        @Override
        public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
            IRenderer renderer = data.get(IRENDERER);
            if (renderer != null) {
                CURRENT_MODEL_DATA.set(data);
                var texture = renderer.getParticleTexture();
                CURRENT_MODEL_DATA.remove();
                return texture;
            }
            return BakedModel.super.getParticleIcon(data);
        }

    }

    public static final class Loader implements IGeometryLoader<LDLRendererModel> {

        public static final Loader INSTANCE = new Loader();
        private Loader() {}

        @Override
        public LDLRendererModel read(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LDLRendererModel.INSTANCE;
        }
    }
}
