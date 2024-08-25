package com.lowdragmc.photon.client.emitter.data.shape;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.utils.Vector3;
import com.lowdragmc.photon.client.particle.LParticle;
import expr.Expr;
import expr.Parser;
import expr.SyntaxException;
import expr.Variable;

@LDLRegister(name = "function", group = "shape")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/emitter/data/shape/Function.class */
public class Function implements IShape {
    @Configurable(tips = {"photon.gui.editor.shape.function.tooltips.0", "photon.gui.editor.shape.function.tooltips.1", "photon.gui.editor.shape.function.tooltips.2", "photon.gui.editor.shape.function.tooltips.3"})
    private String x = "0";
    @Configurable(tips = {"photon.gui.editor.shape.function.tooltips.0", "photon.gui.editor.shape.function.tooltips.1", "photon.gui.editor.shape.function.tooltips.2", "photon.gui.editor.shape.function.tooltips.3"})
    private String y = "0";
    @Configurable(tips = {"photon.gui.editor.shape.function.tooltips.0", "photon.gui.editor.shape.function.tooltips.1", "photon.gui.editor.shape.function.tooltips.2", "photon.gui.editor.shape.function.tooltips.3"})
    private String z = "0";
    @Configurable(tips = {"photon.gui.editor.shape.function.tooltips.0", "photon.gui.editor.shape.function.tooltips.1", "photon.gui.editor.shape.function.tooltips.2", "photon.gui.editor.shape.function.tooltips.3"})
    private String speedX = "0";
    @Configurable(tips = {"photon.gui.editor.shape.function.tooltips.0", "photon.gui.editor.shape.function.tooltips.1", "photon.gui.editor.shape.function.tooltips.2", "photon.gui.editor.shape.function.tooltips.3"})
    private String speedY = "0";
    @Configurable(tips = {"photon.gui.editor.shape.function.tooltips.0", "photon.gui.editor.shape.function.tooltips.1", "photon.gui.editor.shape.function.tooltips.2", "photon.gui.editor.shape.function.tooltips.3"})
    private String speedZ = "0";
    private Expr xCache;
    private Expr yCache;
    private Expr zCache;
    private Expr sXCache;
    private Expr sYCache;
    private Expr sZCache;
    private static final Variable T = Variable.make("t");
    private static final Variable PI = Variable.make("PI");
    private static final Variable randomA = Variable.make("randomA");
    private static final Variable randomB = Variable.make("randomB");
    private static final Variable randomC = Variable.make("randomC");
    private static final Variable randomD = Variable.make("randomD");
    private static final Variable randomE = Variable.make("randomE");

    public String getX() {
        return this.x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return this.y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return this.z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getSpeedX() {
        return this.speedX;
    }

    public void setSpeedX(String speedX) {
        this.speedX = speedX;
    }

    public String getSpeedY() {
        return this.speedY;
    }

    public void setSpeedY(String speedY) {
        this.speedY = speedY;
    }

    public String getSpeedZ() {
        return this.speedZ;
    }

    public void setSpeedZ(String speedZ) {
        this.speedZ = speedZ;
    }

    static {
        PI.setValue(3.141592653589793d);
    }

    private void prepareExpr(LParticle emitter) {
        T.setValue(emitter.getT());
        randomA.setValue(emitter.getRandomSource().m_188501_());
        randomB.setValue(emitter.getRandomSource().m_188501_());
        randomC.setValue(emitter.getRandomSource().m_188501_());
        randomD.setValue(emitter.getRandomSource().m_188501_());
        randomE.setValue(emitter.getRandomSource().m_188501_());
        if (this.xCache == null || !this.x.equals(this.xCache.getInput())) {
            try {
                this.xCache = Parser.parse(this.x);
            } catch (SyntaxException e) {
            }
        }
        if (this.yCache == null || !this.y.equals(this.yCache.getInput())) {
            try {
                this.yCache = Parser.parse(this.y);
            } catch (SyntaxException e2) {
            }
        }
        if (this.zCache == null || !this.z.equals(this.zCache.getInput())) {
            try {
                this.zCache = Parser.parse(this.z);
            } catch (SyntaxException e3) {
            }
        }
        if (this.sXCache == null || !this.speedX.equals(this.sXCache.getInput())) {
            try {
                this.sXCache = Parser.parse(this.speedX);
            } catch (SyntaxException e4) {
            }
        }
        if (this.sYCache == null || !this.speedY.equals(this.sYCache.getInput())) {
            try {
                this.sYCache = Parser.parse(this.speedY);
            } catch (SyntaxException e5) {
            }
        }
        if (this.sZCache == null || !this.speedZ.equals(this.sZCache.getInput())) {
            try {
                this.sZCache = Parser.parse(this.speedZ);
            } catch (SyntaxException e6) {
            }
        }
    }

    @Override // com.lowdragmc.photon.client.emitter.data.shape.IShape
    public void nextPosVel(LParticle particle, LParticle emitter, Vector3 position, Vector3 rotation, Vector3 scale) {
        prepareExpr(emitter);
        Vector3 pos = new Vector3(this.xCache != null ? this.xCache.value() : 0.0d, this.yCache != null ? this.yCache.value() : 0.0d, this.zCache != null ? this.zCache.value() : 0.0d);
        Vector3 speed = new Vector3(this.sXCache != null ? this.sXCache.value() : 0.0d, this.sYCache != null ? this.sYCache.value() : 0.0d, this.sZCache != null ? this.sZCache.value() : 0.0d);
        particle.setPos(pos.copy().rotateYXY(rotation).add(position).add(particle.getPos()), true);
        particle.setSpeed(speed.normalize().multiply(0.05d).rotateYXY(rotation));
    }
}
