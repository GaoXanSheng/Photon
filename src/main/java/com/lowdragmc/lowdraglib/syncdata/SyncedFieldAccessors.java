package com.lowdragmc.lowdraglib.syncdata;

import com.lowdragmc.lowdraglib.syncdata.accessor.ArrayAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.BlockStateAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.CollectionAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.ComponentAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.EnumAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.IGuiTextureAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.IManagedAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.ITagSerializableAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.PositionAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.PrimitiveAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.RangeAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.RecipeAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.ResourceLocationAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.SizeAccessor;
import com.lowdragmc.lowdraglib.syncdata.accessor.Vector3Accessor;
import java.util.function.BiFunction;
import net.minecraft.Util;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/syncdata/SyncedFieldAccessors.class */
public class SyncedFieldAccessors {
    public static final IAccessor INT_ACCESSOR = new PrimitiveAccessor.IntAccessor();
    public static final IAccessor LONG_ACCESSOR = new PrimitiveAccessor.LongAccessor();
    public static final IAccessor FLOAT_ACCESSOR = new PrimitiveAccessor.FloatAccessor();
    public static final IAccessor DOUBLE_ACCESSOR = new PrimitiveAccessor.DoubleAccessor();
    public static final IAccessor BOOLEAN_ACCESSOR = new PrimitiveAccessor.BooleanAccessor();
    public static final IAccessor BYTE_ACCESSOR = new PrimitiveAccessor.ByteAccessor();
    public static final IAccessor SHORT_ACCESSOR = new PrimitiveAccessor.ShortAccessor();
    public static final IAccessor CHAR_ACCESSOR = new PrimitiveAccessor.CharAccessor();
    public static final IAccessor ENUM_ACCESSOR = new EnumAccessor();
    public static final IAccessor TAG_SERIALIZABLE_ACCESSOR = new ITagSerializableAccessor();
    public static final IAccessor MANAGED_ACCESSOR = new IManagedAccessor();
    public static final IAccessor BLOCK_STATE_ACCESSOR = new BlockStateAccessor();
    public static final IAccessor RECIPE_ACCESSOR = new RecipeAccessor();
    public static final IAccessor POSITION_ACCESSOR = new PositionAccessor();
    public static final IAccessor VECTOR3_ACCESSOR = new Vector3Accessor();
    public static final IAccessor COMPONENT_ACCESSOR = new ComponentAccessor();
    public static final IAccessor SIZE_ACCESSOR = new SizeAccessor();
    public static final IAccessor GUI_TEXTURE_ACCESSOR = new IGuiTextureAccessor();
    public static final IAccessor RESOURCE_LOCATION_ACCESSOR = new ResourceLocationAccessor();
    public static final IAccessor RANGE_ACCESSOR = new RangeAccessor();
    private static final BiFunction<IAccessor, Class<?>, IAccessor> ARRAY_ACCESSOR_FACTORY = Util.m_143821_(ArrayAccessor::new);
    private static final BiFunction<IAccessor, Class<?>, IAccessor> COLLECTION_ACCESSOR_FACTORY = Util.m_143821_(CollectionAccessor::new);

    public static IAccessor collectionAccessor(IAccessor childAccessor, Class<?> child) {
        return COLLECTION_ACCESSOR_FACTORY.apply(childAccessor, child);
    }

    public static IAccessor arrayAccessor(IAccessor childAccessor, Class<?> child) {
        return ARRAY_ACCESSOR_FACTORY.apply(childAccessor, child);
    }
}
