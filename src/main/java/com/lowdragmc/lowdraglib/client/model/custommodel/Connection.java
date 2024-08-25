package com.lowdragmc.lowdraglib.client.model.custommodel;

import com.lowdragmc.lowdraglib.utils.ShapeUtils;
import expr.Expr;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/model/custommodel/Connection.class */
public enum Connection {
    UP(Direction.UP),
    DOWN(Direction.DOWN),
    LEFT(Direction.EAST),
    RIGHT(Direction.WEST),
    UP_LEFT(Direction.UP, Direction.EAST),
    UP_RIGHT(Direction.UP, Direction.WEST),
    DOWN_LEFT(Direction.DOWN, Direction.EAST),
    DOWN_RIGHT(Direction.DOWN, Direction.WEST);
    
    public final Direction[] dirs;
    public final BlockPos[] offsets = new BlockPos[6];

    Connection(Direction... dirs) {
        Direction[] values;
        this.dirs = dirs;
        for (Direction normal : Direction.values()) {
            BlockPos pos = BlockPos.f_121853_;
            for (Direction dir : dirs) {
                if (normal.m_122434_() == Direction.Axis.Y) {
                    dir = dir.m_122424_();
                }
                pos = pos.m_121945_(dir);
            }
            AABB rotated = ShapeUtils.rotate(new AABB(pos), normal);
            this.offsets[normal.ordinal()] = new BlockPos((rotated.f_82288_ + rotated.f_82291_) / 2.0d, (rotated.f_82289_ + rotated.f_82292_) / 2.0d, (rotated.f_82290_ + rotated.f_82293_) / 2.0d);
        }
    }

    @Nonnull
    public BlockPos getOffset(Direction normal) {
        return this.offsets[normal.ordinal()];
    }

    @Nonnull
    public BlockPos transform(BlockPos pos, Direction normal) {
        return pos.m_121955_(getOffset(normal));
    }

    /* renamed from: com.lowdragmc.lowdraglib.client.model.custommodel.Connection$1  reason: invalid class name */
    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/model/custommodel/Connection$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$lowdragmc$lowdraglib$client$model$custommodel$Connection = new int[Connection.values().length];

        static {
            try {
                $SwitchMap$com$lowdragmc$lowdraglib$client$model$custommodel$Connection[Connection.UP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$lowdragmc$lowdraglib$client$model$custommodel$Connection[Connection.DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$lowdragmc$lowdraglib$client$model$custommodel$Connection[Connection.LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$lowdragmc$lowdraglib$client$model$custommodel$Connection[Connection.RIGHT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$lowdragmc$lowdraglib$client$model$custommodel$Connection[Connection.UP_LEFT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$lowdragmc$lowdraglib$client$model$custommodel$Connection[Connection.UP_RIGHT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$lowdragmc$lowdraglib$client$model$custommodel$Connection[Connection.DOWN_LEFT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$lowdragmc$lowdraglib$client$model$custommodel$Connection[Connection.DOWN_RIGHT.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    public Connection getOppisite() {
        switch (AnonymousClass1.$SwitchMap$com$lowdragmc$lowdraglib$client$model$custommodel$Connection[ordinal()]) {
            case 1:
                return DOWN;
            case 2:
                return UP;
            case 3:
                return RIGHT;
            case 4:
                return LEFT;
            case Expr.ATAN2 /* 5 */:
                return DOWN_RIGHT;
            case Expr.MAX /* 6 */:
                return DOWN_LEFT;
            case Expr.MIN /* 7 */:
                return UP_RIGHT;
            case 8:
                return UP_LEFT;
            default:
                throw new IncompatibleClassChangeError();
        }
    }
}
