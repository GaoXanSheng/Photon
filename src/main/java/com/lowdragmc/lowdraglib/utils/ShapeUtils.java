package com.lowdragmc.lowdraglib.utils;

import expr.Expr;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/ShapeUtils.class */
public class ShapeUtils {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.lowdragmc.lowdraglib.utils.ShapeUtils$1  reason: invalid class name */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/ShapeUtils$1.class */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$minecraft$core$Direction = new int[Direction.values().length];

        static {
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.SOUTH.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.EAST.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.WEST.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.UP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[Direction.DOWN.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public static AABB rotate(AABB AABB, Direction facing) {
        switch (AnonymousClass1.$SwitchMap$net$minecraft$core$Direction[facing.ordinal()]) {
            case 1:
                return rotate(AABB, new Vector3(0.0d, 1.0d, 0.0d), 180.0d);
            case 2:
                return rotate(AABB, new Vector3(0.0d, 1.0d, 0.0d), -90.0d);
            case 3:
                return rotate(AABB, new Vector3(0.0d, 1.0d, 0.0d), 90.0d);
            case 4:
                return rotate(AABB, new Vector3(1.0d, 0.0d, 0.0d), 90.0d);
            case Expr.ATAN2 /* 5 */:
                return rotate(AABB, new Vector3(1.0d, 0.0d, 0.0d), -90.0d);
            default:
                return AABB;
        }
    }

    public static AABB rotate(AABB AABB, Vector3 axis, double degree) {
        Vector3 min = new Vector3(AABB.f_82288_, AABB.f_82289_, AABB.f_82290_).subtract(0.5d);
        Vector3 max = new Vector3(AABB.f_82291_, AABB.f_82292_, AABB.f_82293_).subtract(0.5d);
        double radians = Math.toRadians(degree);
        min.rotate(radians, axis);
        max.rotate(radians, axis);
        min.add(0.5d);
        max.add(0.5d);
        return new AABB(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public static VoxelShape rotate(VoxelShape shape, Direction facing) {
        return (VoxelShape) shape.m_83299_().stream().map(AABB -> {
            return Shapes.m_83064_(rotate(AABB, facing));
        }).reduce(Shapes.m_83040_(), Shapes::m_83110_);
    }

    public static VoxelShape rotate(VoxelShape shape, Vector3 axis, double degree) {
        return (VoxelShape) shape.m_83299_().stream().map(AABB -> {
            return Shapes.m_83064_(rotate(AABB, axis, degree));
        }).reduce(Shapes.m_83040_(), Shapes::m_83110_);
    }
}
