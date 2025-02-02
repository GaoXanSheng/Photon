package com.lowdragmc.lowdraglib.client.renderer.impl;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSetter;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegisterClient;
import com.lowdragmc.lowdraglib.gui.editor.configurator.ConfiguratorGroup;
import com.lowdragmc.lowdraglib.gui.editor.configurator.WrapperConfigurator;
import com.lowdragmc.lowdraglib.gui.editor.ui.Editor;
import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.gui.widget.SceneWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.mojang.blaze3d.vertex.PoseStack;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegisterClient(name = "json_model", group = "renderer")
public class IModelRenderer implements IRenderer {
    @Getter
    @Configurable(forceUpdate = false)
    protected ResourceLocation modelLocation;
    @OnlyIn(Dist.CLIENT)
    protected BakedModel itemModel;
    @OnlyIn(Dist.CLIENT)
    protected Map<Direction, BakedModel> blockModels;

    protected IModelRenderer() {
        modelLocation = new ResourceLocation("block/furnace");
    }

    public IModelRenderer(ResourceLocation modelLocation) {
        this.modelLocation = modelLocation;
        initRenderer();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public TextureAtlasSprite getParticleTexture() {
        BakedModel model = getItemBakedModel();
        if (model == null) {
            return IRenderer.super.getParticleTexture();
        }
        return model.getParticleIcon();
    }

    @OnlyIn(Dist.CLIENT)
    protected UnbakedModel getModel() {
        return ModelFactory.getUnBakedModel(modelLocation);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderItem(ItemStack stack,
                           ItemTransforms.TransformType transformType,
                           boolean leftHand, PoseStack matrixStack,
                           MultiBufferSource buffer, int combinedLight,
                           int combinedOverlay, BakedModel model) {
        IItemRendererProvider.disabled.set(true);
        model = getItemBakedModel(stack);
        if (model != null) {
            Minecraft.getInstance().getItemRenderer().render(stack, transformType, leftHand, matrixStack, buffer, combinedLight, combinedOverlay, model);
        }
        IItemRendererProvider.disabled.set(false);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean useBlockLight(ItemStack stack) {
        var model = getItemBakedModel(stack);
        if (model != null) {
            return model.usesBlockLight();
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean useAO() {
        var model = getItemBakedModel();
        if (model != null) {
            return model.useAmbientOcclusion();
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<BakedQuad> renderModel(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, @Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        var ibakedmodel = getBlockBakedModel(pos, level);
        if (ibakedmodel == null) return Collections.emptyList();
        return ibakedmodel.getQuads(state, side, (Random) rand);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    protected BakedModel getItemBakedModel() {
        if (itemModel == null) {
            var model = getModel();
            if (model instanceof BlockModel blockModel && blockModel.getRootModel() == ModelBakery.GENERATION_MARKER) {
                // fabric doesn't help us to fix vanilla bakery, so we have to do it ourselves
                model = ModelFactory.ITEM_MODEL_GENERATOR.generateBlockModel(this::materialMapping, blockModel);
            }
            itemModel = model.bake(
                    ModelFactory.getModeBakery(),
                    this::materialMapping,
                    BlockModelRotation.X0_Y0,
                    modelLocation);
        }
        return itemModel;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    protected BakedModel getItemBakedModel(ItemStack itemStack) {
        return getItemBakedModel();
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    protected BakedModel getBlockBakedModel(BlockPos pos, BlockAndTintGetter blockAccess) {
        return getRotatedModel(Direction.NORTH);
    }

    @OnlyIn(Dist.CLIENT)
    public BakedModel getRotatedModel(Direction frontFacing) {
        return blockModels.computeIfAbsent(frontFacing, facing -> getModel().bake(
                ModelFactory.getModeBakery(),
                this::materialMapping,
                ModelFactory.getRotation(facing),
                modelLocation));
    }

    @OnlyIn(Dist.CLIENT)
    protected TextureAtlasSprite materialMapping(Material material) {
        return material.sprite();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onPrepareTextureAtlas(ResourceLocation atlasName, Consumer<ResourceLocation> register) {
        if (atlasName.equals(TextureAtlas.LOCATION_BLOCKS)) {
            itemModel = null;
            blockModels.clear();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onAdditionalModel(Consumer<ResourceLocation> registry) {
        registry.accept(modelLocation);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isGui3d() {
        var model = getItemBakedModel();
        if (model == null) {
            return IRenderer.super.isGui3d();
        }
        return model.isGui3d();
    }

    // ISerializableRenderer
    public void initRenderer() {
        if (this.modelLocation != null) {
            if (LDLib.isClient()) {
                blockModels = new ConcurrentHashMap<>();
                registerEvent();
            }
        }
    }

    @ConfigSetter(field = "modelLocation")
    @SuppressWarnings("unused")
    public void updateModelWithoutReloadingResource(ResourceLocation modelLocation) {
        this.modelLocation = modelLocation;
        if (LDLib.isClient()) {
            itemModel = null;
            blockModels.clear();
        }
    }

    public void createPreview(ConfiguratorGroup father) {
        var preview = new WidgetGroup(0, 0, 100, 120);
        var level = new TrackedDummyWorld();
//        level.addBlock(BlockPos.ZERO, BlockInfo.fromBlock(RendererBlock.BLOCK));
//        Optional.ofNullable(level.getBlockEntity(BlockPos.ZERO)).ifPresent(blockEntity -> {
//            if (blockEntity instanceof BlockEntity holder) {
//                holder.setRenderer(this);
//            }
//        });

        var sceneWidget = new SceneWidget(0, 0, 100, 100, level);
        sceneWidget.setRenderFacing(false);
        sceneWidget.setRenderSelect(false);
        sceneWidget.createScene(level);
        sceneWidget.getRenderer().setOnLookingAt(null); // better performance
        sceneWidget.setRenderedCore(Collections.singleton(BlockPos.ZERO), null);
        sceneWidget.setBackground(new ColorBorderTexture(2, ColorPattern.T_WHITE.color));

        preview.addWidget(new ButtonWidget(5, 110, 90, 10,
                new GuiTextureGroup(ColorPattern.T_GRAY.rectTexture().setRadius(5), new TextTexture("ldlib.gui.editor.tips.select_model")), cd -> {
            if (Editor.INSTANCE == null) return;
            File path = new File(Editor.INSTANCE.getWorkSpace(), "models");
            DialogWidget.showFileDialog(Editor.INSTANCE, "ldlib.gui.editor.tips.select_model", path, true,
                    DialogWidget.suffixFilter(".json"), r -> {
                        if (r != null && r.isFile()) {
                            updateModelWithoutReloadingResource(getModelFromFile(path, r));
                        }
                    });
        }));

        preview.addWidget(sceneWidget);
        father.addConfigurators(new WrapperConfigurator("ldlib.gui.editor.group.preview", preview));
    }

    private static ResourceLocation getModelFromFile(File path, File r){
        var id = path.getPath().replace('\\', '/').split("assets/")[1].split("/")[0];
        return new ResourceLocation(id, r.getPath().replace(path.getPath(), "").replace(".json", "").replace('\\', '/'));
    }
}
