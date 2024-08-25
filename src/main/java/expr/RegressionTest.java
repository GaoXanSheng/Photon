package expr;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/RegressionTest.class */
public class RegressionTest {
    public static void main(String[] args) {
        Variable.make("pi").setValue(3.141592653589793d);
        expect(9.0d, "3^2");
        expect(256.0d, "2^2^3");
        expect(6.0d, "3*2");
        expect(1.5d, "3/2");
        expect(5.0d, "3+2");
        expect(1.0d, "3-2");
        expect(-3.0d, "-3");
        expect(1.0d, "2<3");
        expect(0.0d, "2<2");
        expect(0.0d, "3<2");
        expect(1.0d, "2<=3");
        expect(1.0d, "2<=2");
        expect(0.0d, "3<=2");
        expect(0.0d, "2=3");
        expect(1.0d, "2=2");
        expect(1.0d, "2<>3");
        expect(0.0d, "2<>2");
        expect(0.0d, "2>=3");
        expect(1.0d, "2>=2");
        expect(1.0d, "3>=2");
        expect(0.0d, "2>3");
        expect(0.0d, "2>2");
        expect(1.0d, "3>2");
        expect(1.0d, "(1 and 1)");
        expect(0.0d, "(1 and 0)");
        expect(0.0d, "(0 and 1)");
        expect(0.0d, "(0 and 0)");
        expect(1.0d, "(1 or 1)");
        expect(1.0d, "(1 or 0)");
        expect(1.0d, "(0 or 1)");
        expect(0.0d, "(0 or 0)");
        expect(2.0d, "abs(-2)");
        expect(2.0d, "abs(2)");
        expect(0.0d, "acos(1)");
        expect(1.5707963267948966d, "asin(1)");
        expect(0.7853981633974483d, "atan(1)");
        expect(-2.356194490192345d, "atan2(-1, -1)");
        expect(4.0d, "ceil(3.5)");
        expect(-3.0d, "ceil(-3.5)");
        expect(1.0d, "cos(0)");
        expect(Math.exp(1.0d), "exp(1)");
        expect(3.0d, "floor(3.5)");
        expect(-4.0d, "floor(-3.5)");
        expect(1.0d, "log(2.7182818284590451)");
        expect(4.0d, "round(3.5)");
        expect(-4.0d, "round(-3.5)");
        expect(1.0d, "sin(pi/2)");
        expect(3.0d, "sqrt(9)");
        expect(0.9999999999999999d, "tan(pi/4)");
        expect(3.0d, "max(2, 3)");
        expect(2.0d, "min(2, 3)");
        expect(137.0d, "if(0, 42, 137)");
        expect(42.0d, "if(1, 42, 137)");
        expect((-3.0d) * Math.pow(1.01d, 100.1d), "  -3 * 1.01^100.1  ");
        Variable x = Variable.make("x");
        x.setValue(-40.0d);
        expect(-171.375208d, "-0.00504238 * x^2 + 2.34528 * x - 69.4962");
        boolean caught = false;
        Parser p = new Parser();
        p.allow(x);
        try {
            p.parseString("whoo");
        } catch (SyntaxException e) {
            caught = true;
        }
        if (!caught) {
            throw new Error("Test failed: unknown variable allowed");
        }
        x.setValue(1.1d);
        expect(137.0d, "137");
        expect(3.141592653589793d, "pi");
        expect(1.1d, "x");
        expect(3.8013239000000003d, "3.14159 * x^2");
        expect(-1.457526100326025d, "sin(10*x) + sin(9*x)");
        expect(0.8907649332805846d, "sin(x) + sin(100*x)/100");
        expect(-0.16000473871962462d, "sin(0.1*x) * (sin(9*x) + sin(10*x))");
        expect(0.29819727942988733d, "exp(-x^2)");
        expect(0.43226861565393254d, "2^(-x^2)");
        expect(0.7075295010833899d, "(x^3)^(-x^2)");
        expect(0.8678400091286832d, "x*sin(1/x)");
        expect(-5.89d, "x^2-x-6");
        expect(3.1953090617340916d, "sqrt(3^2 + x^2)");
        expect(1.3542460218188073d, "atan(5/x)");
        expect(1.5761904761904764d, "(x^2 + x + 1)/(x + 1)");
        expect(2.6451713395638627d, "(x^3 - (4*x^2) + 12)/(x^2 + 2)");
        expect(-2.2199999999999998d, "-2*(x-3)^2+5");
        expect(1.2000000000000002d, "2*abs(x+1)-3");
        expect(2.7910571473905725d, "sqrt(9-x^2)");
        System.out.println("All tests passed.");
    }

    private static void expect(double expected, String input) {
        try {
            Expr expr2 = Parser.parse(input);
            double result = expr2.value();
            if (result != expected) {
                Error error = new Error("Bad result: " + result + " instead of the expected " + error + " in \"" + expected + "\"");
                throw error;
            }
        } catch (SyntaxException e) {
            throw new Error(e.explain());
        }
    }
}
