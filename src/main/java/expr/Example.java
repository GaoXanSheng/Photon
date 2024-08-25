package expr;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/Example.class */
public class Example {
    public static void main(String[] args) {
        try {
            Expr expr2 = Parser.parse("3 * x^2");
            Variable x = Variable.make("x");
            double d = 0.0d;
            while (true) {
                double xval = d;
                if (xval <= 3.0d) {
                    x.setValue(xval);
                    System.out.println(expr2.value());
                    d = xval + 1.0d;
                } else {
                    return;
                }
            }
        } catch (SyntaxException e) {
            System.err.println(e.explain());
        }
    }
}
