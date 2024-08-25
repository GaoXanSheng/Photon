package expr;

import java.io.PrintStream;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/Benchmark.class */
public class Benchmark {
    static final int nruns = 1000000;
    static final int nparses = 1000;

    public static void main(String[] args) {
        double parse_product = 1.0d;
        double run_product = 1.0d;
        for (String arg : args) {
            long parsetime = timeParse(arg);
            long runtime = timeRun(arg);
            PrintStream printStream = System.out;
            printStream.println(msec(parsetime) + " ms(parse) " + printStream + " ms(run): " + msec(runtime));
            parse_product *= parsetime;
            run_product *= runtime;
        }
        if (0 < args.length) {
            double run_geomean = Math.pow(run_product, 1.0d / args.length);
            double parse_geomean = Math.pow(parse_product, 1.0d / args.length);
            PrintStream printStream2 = System.out;
            long msec = msec(parse_geomean);
            msec(run_geomean);
            printStream2.println(msec + " ms(parse) " + printStream2 + " ms(run): (geometric mean)");
        }
    }

    static long msec(double nsec) {
        return (long) Math.rint(nsec * 1.0E-6d);
    }

    static long timeRun(String expression) {
        Variable x = Variable.make("x");
        Expr expr2 = parse(expression);
        double step = (4.0d - 0.0d) / 1000000.0d;
        long start = System.nanoTime();
        double d = 0.0d;
        while (true) {
            double xval = d;
            if (xval <= 4.0d) {
                x.setValue(xval);
                expr2.value();
                d = xval + step;
            } else {
                return System.nanoTime() - start;
            }
        }
    }

    static long timeParse(String expression) {
        long start = System.nanoTime();
        for (int i = 0; i < nparses; i++) {
            parse(expression);
        }
        return System.nanoTime() - start;
    }

    static Expr parse(String expression) {
        try {
            return Parser.parse(expression);
        } catch (SyntaxException e) {
            System.err.println(e.explain());
            throw new Error(e);
        }
    }
}
