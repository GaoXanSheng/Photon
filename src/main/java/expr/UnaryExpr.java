package expr;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/UnaryExpr.class */
public class UnaryExpr extends Expr {
    int rator;
    Expr rand;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnaryExpr(int rator, Expr rand) {
        this.rator = rator;
        this.rand = rand;
    }

    @Override // expr.Expr
    public double value() {
        double arg = this.rand.value();
        switch (this.rator) {
            case 100:
                return Math.abs(arg);
            case Expr.ACOS /* 101 */:
                return Math.acos(arg);
            case Expr.ASIN /* 102 */:
                return Math.asin(arg);
            case Expr.ATAN /* 103 */:
                return Math.atan(arg);
            case Expr.CEIL /* 104 */:
                return Math.ceil(arg);
            case Expr.COS /* 105 */:
                return Math.cos(arg);
            case Expr.EXP /* 106 */:
                return Math.exp(arg);
            case Expr.FLOOR /* 107 */:
                return Math.floor(arg);
            case Expr.LOG /* 108 */:
                return Math.log(arg);
            case Expr.NEG /* 109 */:
                return -arg;
            case Expr.ROUND /* 110 */:
                return Math.rint(arg);
            case Expr.SIN /* 111 */:
                return Math.sin(arg);
            case Expr.SQRT /* 112 */:
                return Math.sqrt(arg);
            case Expr.TAN /* 113 */:
                return Math.tan(arg);
            default:
                throw new RuntimeException("BUG: bad rator");
        }
    }
}
