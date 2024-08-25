package com.lowdragmc.lowdraglib.utils;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/XmlUtils.class */
public class XmlUtils {
    public static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    @Nullable
    public static Document loadXml(InputStream inputstream) {
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(inputstream);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getAsInt(Element element, String name, int defaultValue) {
        if (element.hasAttribute(name)) {
            try {
                return Integer.parseInt(element.getAttribute(name));
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static long getAsLong(Element element, String name, long defaultValue) {
        if (element.hasAttribute(name)) {
            try {
                return Long.parseLong(element.getAttribute(name));
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static boolean getAsBoolean(Element element, String name, boolean defaultValue) {
        if (element.hasAttribute(name)) {
            try {
                return Boolean.parseBoolean(element.getAttribute(name));
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static String getAsString(Element element, String name, String defaultValue) {
        if (element.hasAttribute(name)) {
            return element.getAttribute(name);
        }
        return defaultValue;
    }

    public static float getAsFloat(Element element, String name, float defaultValue) {
        if (element.hasAttribute(name)) {
            try {
                return Float.parseFloat(element.getAttribute(name));
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static int getAsColor(Element element, String name, int defaultValue) {
        if (element.hasAttribute(name)) {
            try {
                int value = Long.decode(element.getAttribute(name)).intValue();
                if (value != 0 && (value & (-16777216)) == 0) {
                    value |= -16777216;
                }
                return value;
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static BlockPos getAsBlockPos(Element element, String name, BlockPos defaultValue) {
        if (element.hasAttribute(name)) {
            String pos = getAsString(element, name, "0 0 0");
            try {
                String[] s = pos.split(" ");
                return new BlockPos(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static <T extends Enum<T>> T getAsEnum(Element element, String name, Class<T> enumClass, T defaultValue) {
        T[] enumConstants;
        if (element.hasAttribute(name)) {
            try {
                String data = element.getAttribute(name);
                for (T t : enumClass.getEnumConstants()) {
                    if (t.name().equals(data)) {
                        return t;
                    }
                }
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static CompoundTag getCompoundTag(Element element) {
        NodeList nodeList = element.getChildNodes();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String text = node.getTextContent().replaceAll("\\h*\\R+\\h*", " ");
            if (!text.isEmpty() && text.charAt(0) == ' ') {
                text = text.substring(1);
            }
            builder.append(text);
        }
        if (!builder.isEmpty()) {
            try {
                return TagParser.m_129359_(builder.toString());
            } catch (CommandSyntaxException e) {
            }
        }
        return new CompoundTag();
    }

    public static ItemStack getItemStack(Element element) {
        SizedIngredient ingredient = getIngredient(element);
        if (ingredient.ingredient.m_43908_().length > 0) {
            ItemStack stack = ingredient.ingredient.m_43908_()[0];
            stack.m_41764_(ingredient.count);
            return stack;
        }
        return ItemStack.f_41583_;
    }

    public static Vec3 getAsVec3(Element element, String name, Vec3 defaultValue) {
        if (element.hasAttribute(name)) {
            String pos = getAsString(element, name, "0 0 0");
            try {
                String[] s = pos.split(" ");
                return new Vec3(Float.parseFloat(s[0]), Float.parseFloat(s[1]), Float.parseFloat(s[2]));
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static Vec2 getAsVec2(Element element, String name, Vec2 defaultValue) {
        if (element.hasAttribute(name)) {
            String pos = getAsString(element, name, "0 0");
            try {
                String[] s = pos.split(" ");
                return new Vec2(Float.parseFloat(s[0]), Float.parseFloat(s[1]));
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static EntityInfo getEntityInfo(Element element) {
        int id = getAsInt(element, "id", LDLib.random.nextInt());
        EntityType<?> entityType = null;
        if (element.hasAttribute("type")) {
            entityType = (EntityType) Registry.f_122826_.m_7745_(new ResourceLocation(element.getAttribute("type")));
        }
        CompoundTag tag = null;
        NodeList nodeList = element.getChildNodes();
        int i = 0;
        while (true) {
            if (i >= nodeList.getLength()) {
                break;
            }
            Node item = nodeList.item(i);
            if (item instanceof Element) {
                Element subElement = (Element) item;
                if (subElement.getNodeName().equals("nbt")) {
                    tag = getCompoundTag(subElement);
                    break;
                }
            }
            i++;
        }
        return new EntityInfo(id, entityType, tag);
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/XmlUtils$SizedIngredient.class */
    public static final class SizedIngredient extends Record {
        private final Ingredient ingredient;
        private final int count;

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
        /*  JADX ERROR: Failed to decode insn: 0x0001: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.utils.XmlUtils.SizedIngredient.toString():java.lang.String
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
            throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.utils.XmlUtils.SizedIngredient.toString():java.lang.String");
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
        /*  JADX ERROR: Failed to decode insn: 0x0001: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.utils.XmlUtils.SizedIngredient.hashCode():int
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
            throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.utils.XmlUtils.SizedIngredient.hashCode():int");
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
        /*  JADX ERROR: Failed to decode insn: 0x0002: INVOKE_CUSTOM, method: com.lowdragmc.lowdraglib.utils.XmlUtils.SizedIngredient.equals(java.lang.Object):boolean
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
            throw new UnsupportedOperationException("Method not decompiled: com.lowdragmc.lowdraglib.utils.XmlUtils.SizedIngredient.equals(java.lang.Object):boolean");
        }

        public SizedIngredient(Ingredient ingredient, int count) {
            this.ingredient = ingredient;
            this.count = count;
        }

        public Ingredient ingredient() {
            return this.ingredient;
        }

        public int count() {
            return this.count;
        }
    }

    public static SizedIngredient getIngredient(Element element) {
        int count = getAsInt(element, "count", 1);
        SizedIngredient ingredient = new SizedIngredient(Ingredient.f_43901_, 0);
        if (element.hasAttribute("item")) {
            Item item = (Item) Registry.f_122827_.m_7745_(new ResourceLocation(element.getAttribute("item")));
            if (item != Items.f_41852_) {
                ItemStack itemStack = new ItemStack(item, count);
                NodeList nodeList = element.getChildNodes();
                int i = 0;
                while (true) {
                    if (i >= nodeList.getLength()) {
                        break;
                    }
                    Node item2 = nodeList.item(i);
                    if (item2 instanceof Element) {
                        Element subElement = (Element) item2;
                        if (subElement.getNodeName().equals("nbt")) {
                            itemStack.m_41751_(getCompoundTag(subElement));
                            break;
                        }
                    }
                    i++;
                }
                ingredient = new SizedIngredient(Ingredient.m_43927_(new ItemStack[]{itemStack}), count);
            }
        } else if (Platform.isForge() && element.hasAttribute("forge-tag")) {
            ingredient = new SizedIngredient(Ingredient.m_204132_(TagKey.m_203882_(Registry.f_122904_, new ResourceLocation(element.getAttribute("forge-tag")))), count);
        } else if (!Platform.isForge() && element.hasAttribute("fabric-tag")) {
            ingredient = new SizedIngredient(Ingredient.m_204132_(TagKey.m_203882_(Registry.f_122904_, new ResourceLocation(element.getAttribute("fabric-tag")))), count);
        } else if (element.hasAttribute("tag")) {
            ingredient = new SizedIngredient(Ingredient.m_204132_(TagKey.m_203882_(Registry.f_122904_, new ResourceLocation(element.getAttribute("tag")))), count);
        }
        return ingredient;
    }

    public static FluidStack getFluidStack(Element element) {
        Fluid fluid;
        long amount = (getAsLong(element, "amount", 1L) * FluidHelper.getBucket()) / 1000;
        FluidStack fluidStack = FluidStack.empty();
        if (element.hasAttribute("fluid") && (fluid = (Fluid) Registry.f_122822_.m_7745_(new ResourceLocation(element.getAttribute("fluid")))) != Fluids.f_76191_) {
            fluidStack = FluidStack.create(fluid, amount);
            NodeList nodeList = element.getChildNodes();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String text = node.getTextContent().replaceAll("\\h*\\R+\\h*", " ");
                if (!text.isEmpty() && text.charAt(0) == ' ') {
                    text = text.substring(1);
                }
                builder.append(text);
            }
            if (!builder.isEmpty()) {
                try {
                    fluidStack.setTag(TagParser.m_129359_(builder.toString()));
                } catch (CommandSyntaxException e) {
                }
            }
        }
        return fluidStack;
    }

    public static BlockInfo getBlockInfo(Element element) {
        Block block;
        BlockInfo blockInfo = BlockInfo.EMPTY;
        if (element.hasAttribute("block") && (block = (Block) Registry.f_122824_.m_7745_(new ResourceLocation(element.getAttribute("block")))) != Blocks.f_50016_) {
            BlockState blockState = block.m_49966_();
            NodeList nodeList = element.getChildNodes();
            CompoundTag tag = new CompoundTag();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    Element subElement = (Element) node;
                    if (subElement.getNodeName().equals("properties")) {
                        blockState = setBlockState(blockState, subElement);
                    } else if (subElement.getNodeName().equals("nbt")) {
                        tag = getCompoundTag(subElement);
                    }
                }
            }
            blockInfo = BlockInfo.fromBlockState(blockState);
            if (!tag.m_128456_()) {
                blockInfo.setTag(tag);
            }
        }
        return blockInfo;
    }

    public static BlockState setBlockState(BlockState blockState, Element element) {
        Property<? extends Comparable<?>> property;
        StateDefinition<Block, BlockState> stateDefinition = blockState.m_60734_().m_49965_();
        String name = getAsString(element, "name", "");
        String value = getAsString(element, "value", "");
        if (!name.isEmpty() && (property = stateDefinition.m_61081_(name)) != null) {
            blockState = setValueHelper(blockState, property, value);
        }
        return blockState;
    }

    private static BlockState setValueHelper(BlockState stateHolder, Property property, String value) {
        Optional optional = property.m_6215_(value);
        if (optional.isPresent()) {
            return (BlockState) stateHolder.m_61124_(property, (Comparable) optional.get());
        }
        return stateHolder;
    }

    public static String getContent(Element element, boolean pretty) {
        NodeList nodeList = element.getChildNodes();
        StringBuilder builder = new StringBuilder();
        boolean lastNodeIsText = false;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == 3) {
                String text = node.getTextContent();
                if (pretty) {
                    text = text.replaceAll("\\h*\\R+\\h*", " ");
                }
                if (!lastNodeIsText && !text.isEmpty() && text.charAt(0) == ' ') {
                    text = text.substring(1);
                }
                builder.append(text);
                lastNodeIsText = false;
            } else if (node instanceof Element) {
                Element nodeElement = (Element) node;
                String nodeName = nodeElement.getNodeName();
                lastNodeIsText = false;
                boolean z = true;
                switch (nodeName.hashCode()) {
                    case 3152:
                        if (nodeName.equals("br")) {
                            z = true;
                            break;
                        }
                        break;
                    case 3314158:
                        if (nodeName.equals("lang")) {
                            z = false;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                        String key = getAsString(nodeElement, "key", "");
                        builder.append(LocalizationUtils.format(key, new Object[0]));
                        lastNodeIsText = true;
                        continue;
                    case true:
                        builder.append('\n');
                        continue;
                }
            }
        }
        return builder.toString();
    }

    public static List<MutableComponent> getComponents(Element element, Style style) {
        NodeList nodeList = element.getChildNodes();
        List<MutableComponent> results = new ArrayList<>();
        MutableComponent component = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == 3) {
                String text = node.getTextContent().replaceAll("\\h*\\R+\\h*", " ");
                if (component == null) {
                    component = Component.m_237113_(text).m_130948_(style);
                } else {
                    component = component.m_7220_(Component.m_237113_(text).m_130948_(style));
                }
            } else if (node instanceof Element) {
                Element nodeElement = (Element) node;
                String nodeName = nodeElement.getNodeName();
                boolean z = true;
                switch (nodeName.hashCode()) {
                    case 3152:
                        if (nodeName.equals("br")) {
                            z = true;
                            break;
                        }
                        break;
                    case 3314158:
                        if (nodeName.equals("lang")) {
                            z = false;
                            break;
                        }
                        break;
                    case 109780401:
                        if (nodeName.equals("style")) {
                            z = true;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                        String key = getAsString(nodeElement, "key", "");
                        if (component == null) {
                            component = Component.m_237115_(key).m_130948_(style);
                            break;
                        } else {
                            component = component.m_7220_(Component.m_237115_(key).m_130948_(style));
                            continue;
                        }
                    case true:
                        results.add((MutableComponent) Objects.requireNonNullElseGet(component, Component::m_237119_));
                        component = Component.m_237119_();
                        continue;
                    case true:
                        Style newStyle = style.m_131148_(style.m_131135_());
                        if (nodeElement.hasAttribute("color")) {
                            newStyle = newStyle.m_178520_(getAsColor(nodeElement, "color", -1));
                        }
                        if (nodeElement.hasAttribute("bold")) {
                            newStyle = newStyle.m_131136_(Boolean.valueOf(getAsBoolean(nodeElement, "bold", true)));
                        }
                        if (nodeElement.hasAttribute("font")) {
                            newStyle = newStyle.m_131150_(new ResourceLocation(nodeElement.getAttribute("font")));
                        }
                        if (nodeElement.hasAttribute("italic")) {
                            newStyle = newStyle.m_131155_(Boolean.valueOf(getAsBoolean(nodeElement, "italic", true)));
                        }
                        if (nodeElement.hasAttribute("underlined")) {
                            newStyle = newStyle.m_131162_(Boolean.valueOf(getAsBoolean(nodeElement, "underlined", true)));
                        }
                        if (nodeElement.hasAttribute("strikethrough")) {
                            newStyle = newStyle.m_178522_(Boolean.valueOf(getAsBoolean(nodeElement, "strikethrough", true)));
                        }
                        if (nodeElement.hasAttribute("obfuscated")) {
                            newStyle = newStyle.m_178524_(Boolean.valueOf(getAsBoolean(nodeElement, "obfuscated", true)));
                        }
                        if (nodeElement.hasAttribute("hover-info")) {
                            newStyle = newStyle.m_131144_(new HoverEvent(HoverEvent.Action.f_130831_, Component.m_237115_(nodeElement.getAttribute("hover-info"))));
                        }
                        if (nodeElement.hasAttribute("link")) {
                            newStyle = newStyle.m_131142_(new ClickEvent(ClickEvent.Action.OPEN_URL, "@!" + nodeElement.getAttribute("link")));
                        }
                        if (nodeElement.hasAttribute("url-link")) {
                            newStyle = newStyle.m_131142_(new ClickEvent(ClickEvent.Action.OPEN_URL, "@#" + nodeElement.getAttribute("url-link")));
                        }
                        List<MutableComponent> components = getComponents(nodeElement, newStyle);
                        for (int j = 0; j < components.size(); j++) {
                            if (j == 0) {
                                if (component != null) {
                                    component.m_7220_(components.get(j));
                                } else {
                                    component = components.get(j);
                                }
                            } else {
                                results.add(component);
                                component = components.get(j);
                            }
                        }
                        continue;
                }
            }
        }
        if (component != null) {
            results.add(component);
        }
        return results;
    }
}
