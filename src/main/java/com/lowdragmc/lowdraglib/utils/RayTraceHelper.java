package com.lowdragmc.lowdraglib.utils;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/RayTraceHelper.class */
public class RayTraceHelper {
    public static BlockHitResult rayTraceRange(Level worldIn, Player playerIn, double range) {
        Vec3 origin = getTraceOrigin(playerIn);
        Vec3 target = getTraceTarget(playerIn, range, origin);
        ClipContext context = new ClipContext(origin, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, playerIn);
        return worldIn.m_45547_(context);
    }

    public static PredicateTraceResult rayTraceUntil(Player playerIn, double range, Predicate<BlockPos> predicate) {
        Vec3 origin = getTraceOrigin(playerIn);
        Vec3 target = getTraceTarget(playerIn, range, origin);
        return rayTraceUntil(origin, target, predicate);
    }

    public static Vec3 getTraceTarget(Player playerIn, double range, Vec3 origin) {
        float f = playerIn.m_146909_();
        float f1 = playerIn.m_146908_();
        float f2 = Mth.m_14089_(((-f1) * 0.017453292f) - 3.1415927f);
        float f3 = Mth.m_14031_(((-f1) * 0.017453292f) - 3.1415927f);
        float f4 = -Mth.m_14089_((-f) * 0.017453292f);
        float f5 = Mth.m_14031_((-f) * 0.017453292f);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        return origin.m_82520_(f6 * range, f5 * range, f7 * range);
    }

    public static Vec3 getTraceOrigin(Player playerIn) {
        double d0 = playerIn.m_20185_();
        double d1 = playerIn.m_20186_() + playerIn.m_20192_();
        double d2 = playerIn.m_20189_();
        return new Vec3(d0, d1, d2);
    }

    public static PredicateTraceResult rayTraceUntil(Vec3 start, Vec3 end, Predicate<BlockPos> predicate) {
        Direction enumfacing;
        if (Double.isNaN(start.f_82479_) || Double.isNaN(start.f_82480_) || Double.isNaN(start.f_82481_) || Double.isNaN(end.f_82479_) || Double.isNaN(end.f_82480_) || Double.isNaN(end.f_82481_)) {
            return null;
        }
        int dx = Mth.m_14107_(end.f_82479_);
        int dy = Mth.m_14107_(end.f_82480_);
        int dz = Mth.m_14107_(end.f_82481_);
        int x = Mth.m_14107_(start.f_82479_);
        int y = Mth.m_14107_(start.f_82480_);
        int z = Mth.m_14107_(start.f_82481_);
        BlockPos.MutableBlockPos currentPos = new BlockPos(x, y, z).m_122032_();
        if (predicate.test(currentPos)) {
            return new PredicateTraceResult(currentPos.m_7949_(), Direction.m_122372_(dx - x, dy - y, dz - z));
        }
        int remainingDistance = 200;
        do {
            int i = remainingDistance;
            remainingDistance--;
            if (i >= 0) {
                if (Double.isNaN(start.f_82479_) || Double.isNaN(start.f_82480_) || Double.isNaN(start.f_82481_)) {
                    return null;
                }
                if (x == dx && y == dy && z == dz) {
                    return new PredicateTraceResult();
                }
                boolean flag2 = true;
                boolean flag = true;
                boolean flag1 = true;
                double d0 = 999.0d;
                double d1 = 999.0d;
                double d2 = 999.0d;
                if (dx > x) {
                    d0 = x + 1.0d;
                } else if (dx < x) {
                    d0 = x + 0.0d;
                } else {
                    flag2 = false;
                }
                if (dy > y) {
                    d1 = y + 1.0d;
                } else if (dy < y) {
                    d1 = y + 0.0d;
                } else {
                    flag = false;
                }
                if (dz > z) {
                    d2 = z + 1.0d;
                } else if (dz < z) {
                    d2 = z + 0.0d;
                } else {
                    flag1 = false;
                }
                double d3 = 999.0d;
                double d4 = 999.0d;
                double d5 = 999.0d;
                double d6 = end.f_82479_ - start.f_82479_;
                double d7 = end.f_82480_ - start.f_82480_;
                double d8 = end.f_82481_ - start.f_82481_;
                if (flag2) {
                    d3 = (d0 - start.f_82479_) / d6;
                }
                if (flag) {
                    d4 = (d1 - start.f_82480_) / d7;
                }
                if (flag1) {
                    d5 = (d2 - start.f_82481_) / d8;
                }
                if (d3 == -0.0d) {
                    d3 = -1.0E-4d;
                }
                if (d4 == -0.0d) {
                    d4 = -1.0E-4d;
                }
                if (d5 == -0.0d) {
                    d5 = -1.0E-4d;
                }
                if (d3 < d4 && d3 < d5) {
                    enumfacing = dx > x ? Direction.WEST : Direction.EAST;
                    start = new Vec3(d0, start.f_82480_ + (d7 * d3), start.f_82481_ + (d8 * d3));
                } else if (d4 < d5) {
                    enumfacing = dy > y ? Direction.DOWN : Direction.UP;
                    start = new Vec3(start.f_82479_ + (d6 * d4), d1, start.f_82481_ + (d8 * d4));
                } else {
                    enumfacing = dz > z ? Direction.NORTH : Direction.SOUTH;
                    start = new Vec3(start.f_82479_ + (d6 * d5), start.f_82480_ + (d7 * d5), d2);
                }
                x = Mth.m_14107_(start.f_82479_) - (enumfacing == Direction.EAST ? 1 : 0);
                y = Mth.m_14107_(start.f_82480_) - (enumfacing == Direction.UP ? 1 : 0);
                z = Mth.m_14107_(start.f_82481_) - (enumfacing == Direction.SOUTH ? 1 : 0);
                currentPos.m_122178_(x, y, z);
            } else {
                return new PredicateTraceResult();
            }
        } while (!predicate.test(currentPos));
        return new PredicateTraceResult(currentPos.m_7949_(), enumfacing);
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/RayTraceHelper$PredicateTraceResult.class */
    public static class PredicateTraceResult {
        private BlockPos pos;
        private Direction facing;

        public PredicateTraceResult(BlockPos pos, Direction facing) {
            this.pos = pos;
            this.facing = facing;
        }

        public PredicateTraceResult() {
        }

        public Direction getFacing() {
            return this.facing;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public boolean missed() {
            return this.pos == null;
        }
    }
}
