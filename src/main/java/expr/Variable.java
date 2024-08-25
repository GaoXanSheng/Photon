package expr;

import java.util.Hashtable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/Variable.class */
public class Variable extends Expr {
    private static Hashtable variables = new Hashtable();
    private String name;
    private double val = 0.0d;

    public static synchronized Variable make(String name) {
        Variable result = (Variable) variables.get(name);
        if (result == null) {
            Hashtable hashtable = variables;
            Variable variable = new Variable(name);
            result = variable;
            hashtable.put(name, variable);
        }
        return result;
    }

    public Variable(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override // expr.Expr
    public double value() {
        return this.val;
    }

    public void setValue(double value) {
        this.val = value;
    }
}
