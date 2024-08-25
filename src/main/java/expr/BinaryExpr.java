package expr;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/BinaryExpr.class */
public class BinaryExpr extends Expr {
    int rator;
    Expr rand0;
    Expr rand1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BinaryExpr(int rator, Expr rand0, Expr rand1) {
        this.rator = rator;
        this.rand0 = rand0;
        this.rand1 = rand1;
    }

    @Override // expr.Expr
    public double value() {
        double arg0 = this.rand0.value();
        double arg1 = this.rand1.value();
        switch (this.rator) {
            case 0:
                return arg0 + arg1;
            case 1:
                return arg0 - arg1;
            case 2:
                return arg0 * arg1;
            case 3:
                return arg0 / arg1;
            case 4:
                return Math.pow(arg0, arg1);
            case Expr.ATAN2 /* 5 */:
                return Math.atan2(arg0, arg1);
            case Expr.MAX /* 6 */:
                return Math.max(arg0, arg1);
            case Expr.MIN /* 7 */:
                return Math.min(arg0, arg1);
            case 8:
                return arg0 < arg1 ? 1.0d : 0.0d;
            case Expr.LE /* 9 */:
                return arg0 <= arg1 ? 1.0d : 0.0d;
            case Expr.EQ /* 10 */:
                return arg0 == arg1 ? 1.0d : 0.0d;
            case Expr.NE /* 11 */:
                return arg0 != arg1 ? 1.0d : 0.0d;
            case Expr.GE /* 12 */:
                return arg0 >= arg1 ? 1.0d : 0.0d;
            case Expr.GT /* 13 */:
                return arg0 > arg1 ? 1.0d : 0.0d;
            case Expr.AND /* 14 */:
                return (arg0 == 0.0d || arg1 == 0.0d) ? 0.0d : 1.0d;
            case Expr.OR /* 15 */:
                return (arg0 == 0.0d && arg1 == 0.0d) ? 0.0d : 1.0d;
            default:
                throw new RuntimeException("BUG: bad rator");
        }
    }
}
