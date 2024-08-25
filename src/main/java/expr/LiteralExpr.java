package expr;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/LiteralExpr.class */
public class LiteralExpr extends Expr {
    double v;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LiteralExpr(double v) {
        this.v = v;
    }

    @Override // expr.Expr
    public double value() {
        return this.v;
    }
}
