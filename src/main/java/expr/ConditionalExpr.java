package expr;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/ConditionalExpr.class */
public class ConditionalExpr extends Expr {
    Expr test;
    Expr consequent;
    Expr alternative;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConditionalExpr(Expr test, Expr consequent, Expr alternative) {
        this.test = test;
        this.consequent = consequent;
        this.alternative = alternative;
    }

    @Override // expr.Expr
    public double value() {
        return this.test.value() != 0.0d ? this.consequent.value() : this.alternative.value();
    }
}
