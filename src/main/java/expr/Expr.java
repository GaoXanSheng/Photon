package expr;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/Expr.class */
public abstract class Expr {
    public String input;
    public static final int ADD = 0;
    public static final int SUB = 1;
    public static final int MUL = 2;
    public static final int DIV = 3;
    public static final int POW = 4;
    public static final int ATAN2 = 5;
    public static final int MAX = 6;
    public static final int MIN = 7;
    public static final int LT = 8;
    public static final int LE = 9;
    public static final int EQ = 10;
    public static final int NE = 11;
    public static final int GE = 12;
    public static final int GT = 13;
    public static final int AND = 14;
    public static final int OR = 15;
    public static final int ABS = 100;
    public static final int ACOS = 101;
    public static final int ASIN = 102;
    public static final int ATAN = 103;
    public static final int CEIL = 104;
    public static final int COS = 105;
    public static final int EXP = 106;
    public static final int FLOOR = 107;
    public static final int LOG = 108;
    public static final int NEG = 109;
    public static final int ROUND = 110;
    public static final int SIN = 111;
    public static final int SQRT = 112;
    public static final int TAN = 113;

    public abstract double value();

    public void setInput(String input) {
        this.input = input;
    }

    public String getInput() {
        return this.input;
    }

    public static Expr makeLiteral(double v) {
        return new LiteralExpr(v);
    }

    public static Expr makeApp1(int rator, Expr rand) {
        Expr app = new UnaryExpr(rator, rand);
        if (rand instanceof LiteralExpr) {
            return new LiteralExpr(app.value());
        }
        return app;
    }

    public static Expr makeApp2(int rator, Expr rand0, Expr rand1) {
        Expr app = new BinaryExpr(rator, rand0, rand1);
        if ((rand0 instanceof LiteralExpr) && (rand1 instanceof LiteralExpr)) {
            return new LiteralExpr(app.value());
        }
        return app;
    }

    public static Expr makeIfThenElse(Expr test, Expr consequent, Expr alternative) {
        Expr cond = new ConditionalExpr(test, consequent, alternative);
        if (test instanceof LiteralExpr) {
            return test.value() != 0.0d ? consequent : alternative;
        }
        return cond;
    }
}
