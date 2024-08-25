package expr;

import lombok.Getter;

import java.util.Hashtable;
import java.util.Vector;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/Parser.class */
public class Parser {
    private static final Variable pi = Variable.make("pi");
    private Hashtable allowedVariables = null;
    @Getter
    Scanner tokens = null;
    private Token token = null;
    private static final String operatorChars = "*/+-^<>=,()";
    private static final String[] procs1;
    private static final int[] rators1;
    private static final String[] procs2;
    private static final int[] rators2;

    static {
        pi.setValue(3.141592653589793d);
        procs1 = new String[]{"abs", "acos", "asin", "atan", "ceil", "cos", "exp", "floor", "log", "round", "sin", "sqrt", "tan"};
        rators1 = new int[]{100, Expr.ACOS, Expr.ASIN, Expr.ATAN, Expr.CEIL, Expr.COS, Expr.EXP, Expr.FLOOR, Expr.LOG, Expr.ROUND, Expr.SIN, Expr.SQRT, Expr.TAN};
        procs2 = new String[]{"atan2", "max", "min"};
        rators2 = new int[]{5, 6, 7};
    }

    public static Expr parse(String input) throws SyntaxException {
        return new Parser().parseString(input);
    }

    public void allow(Variable optVariable) {
        if (null == this.allowedVariables) {
            this.allowedVariables = new Hashtable();
            this.allowedVariables.put(pi, pi);
        }
        if (null != optVariable) {
            this.allowedVariables.put(optVariable, optVariable);
        }
    }

    public Expr parseString(String input) throws SyntaxException {
        this.tokens = new Scanner(input, operatorChars);
        return reparse();
    }

    private Expr reparse() throws SyntaxException {
        this.tokens.index = -1;
        nextToken();
        Expr expr2 = parseExpr(0);
        if (this.token.ttype != -2) {
            throw error("Incomplete expression", 0, null);
        }
        expr2.setInput(this.tokens.getInput());
        return expr2;
    }

    private void nextToken() {
        this.token = this.tokens.nextToken();
    }

    private Expr parseExpr(int precedence) throws SyntaxException {
        Expr expr2;
        int l = 0;
        int r = 0;
        int rator = 0;
        Expr parseFactor = parseFactor();
        while (true) {
            expr2 = parseFactor;
            switch (this.token.ttype) {
                case Token.TT_GE /* -7 */:
                    l = 20;
                    r = 21;
                    rator = 12;
                    break;
                case Token.TT_NE /* -6 */:
                    l = 20;
                    r = 21;
                    rator = 11;
                    break;
                case Token.TT_LE /* -5 */:
                    l = 20;
                    r = 21;
                    rator = 9;
                    break;
                case 42:
                    l = 40;
                    r = 41;
                    rator = 2;
                    break;
                case 43:
                    l = 30;
                    r = 31;
                    rator = 0;
                    break;
                case 45:
                    l = 30;
                    r = 31;
                    rator = 1;
                    break;
                case 47:
                    l = 40;
                    r = 41;
                    rator = 3;
                    break;
                case 60:
                    l = 20;
                    r = 21;
                    rator = 8;
                    break;
                case 61:
                    l = 20;
                    r = 21;
                    rator = 10;
                    break;
                case 62:
                    l = 20;
                    r = 21;
                    rator = 13;
                    break;
                case 94:
                    l = 50;
                    r = 50;
                    rator = 4;
                    break;
                default:
                    if (this.token.ttype == -4 && this.token.sval.equals("and")) {
                        l = 5;
                        r = 6;
                        rator = 14;
                        break;
                    } else if (this.token.ttype == -4 && this.token.sval.equals("or")) {
                        l = 10;
                        r = 11;
                        rator = 15;
                        break;
                    }
                    break;
            }
            if (l >= precedence) {
                nextToken();
                parseFactor = Expr.makeApp2(rator, expr2, parseExpr(r));
            }
        }
    }

    private Expr parseFactor() throws SyntaxException {
        switch (this.token.ttype) {
            case Token.TT_WORD /* -4 */:
                for (int i = 0; i < procs1.length; i++) {
                    if (procs1[i].equals(this.token.sval)) {
                        nextToken();
                        expect(40);
                        Expr rand = parseExpr(0);
                        expect(41);
                        return Expr.makeApp1(rators1[i], rand);
                    }
                }
                for (int i2 = 0; i2 < procs2.length; i2++) {
                    if (procs2[i2].equals(this.token.sval)) {
                        nextToken();
                        expect(40);
                        Expr rand1 = parseExpr(0);
                        expect(44);
                        Expr rand2 = parseExpr(0);
                        expect(41);
                        return Expr.makeApp2(rators2[i2], rand1, rand2);
                    }
                }
                if (this.token.sval.equals("if")) {
                    nextToken();
                    expect(40);
                    Expr test = parseExpr(0);
                    expect(44);
                    Expr consequent = parseExpr(0);
                    expect(44);
                    Expr alternative = parseExpr(0);
                    expect(41);
                    return Expr.makeIfThenElse(test, consequent, alternative);
                }
                Expr var = Variable.make(this.token.sval);
                if (null != this.allowedVariables && null == this.allowedVariables.get(var)) {
                    throw error("Unknown variable", 4, null);
                }
                nextToken();
                return var;
            case Token.TT_NUMBER /* -3 */:
                Expr lit = Expr.makeLiteral(this.token.nval);
                nextToken();
                return lit;
            case Token.TT_EOF /* -2 */:
                throw error("Expected a factor", 2, null);
            case 40:
                nextToken();
                Expr enclosed = parseExpr(0);
                expect(41);
                return enclosed;
            case 45:
                nextToken();
                return Expr.makeApp1(Expr.NEG, parseExpr(35));
            default:
                throw error("Expected a factor", 1, null);
        }
    }

    private SyntaxException error(String complaint, int reason, String expected) {
        return new SyntaxException(complaint, this, reason, expected);
    }

    private void expect(int ttype) throws SyntaxException {
        if (this.token.ttype != ttype) {
            throw error(
                    "Expected " + ttype + ", got " + this.token.ttype, 3, "" + ttype
            );
        }
        nextToken();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean tryCorrections() {
        return tryInsertions() || tryDeletions() || trySubstitutions();
    }

    private boolean tryInsertions() {
        Token t;
        Vector v = this.tokens.tokens;
        for (int i = this.tokens.index; 0 <= i; i--) {
            if (i < v.size()) {
                t = (Token) v.elementAt(i);
            } else {
                String s = this.tokens.getInput();
                t = new Token(-2, 0.0d, s, s.length(), s.length());
            }
            Token[] candidates = possibleInsertions(t);
            for (Token candidate : candidates) {
                v.insertElementAt(candidate, i);
                try {
                    reparse();
                    return true;
                } catch (SyntaxException e) {
                    v.removeElementAt(i);
                }
            }
        }
        return false;
    }

    private boolean tryDeletions() {
        Vector v = this.tokens.tokens;
        for (int i = this.tokens.index; 0 <= i; i--) {
            if (v.size() > i) {
                Object t = v.elementAt(i);
                v.remove(i);
                try {
                    reparse();
                    return true;
                } catch (SyntaxException e) {
                    v.insertElementAt(t, i);
                }
            }
        }
        return false;
    }

    private boolean trySubstitutions() {
        Vector v = this.tokens.tokens;
        for (int i = this.tokens.index; 0 <= i; i--) {
            if (v.size() > i) {
                Token t = (Token) v.elementAt(i);
                Token[] candidates = possibleSubstitutions(t);
                for (Token token : candidates) {
                    v.setElementAt(token, i);
                    try {
                        reparse();
                        return true;
                    } catch (SyntaxException ignored) {
                    }
                }
                v.setElementAt(t, i);
            }
        }
        return false;
    }

    private Token[] possibleInsertions(Token t) {
        Token[] ts = new Token[operatorChars.length() + 6 + procs1.length + procs2.length];
        Token one = new Token(-3, 1.0d, "1", t);
        int i = 1;
        ts[0] = one;
        for (int j = 0; j < operatorChars.length(); j++) {
            char c = operatorChars.charAt(j);
            int i2 = i;
            i++;
            ts[i2] = new Token(c, 0.0d, Character.toString(c), t);
        }
        int i3 = i;
        int i4 = i + 1;
        ts[i3] = new Token(-4, 0.0d, "x", t);
        for (String s : procs1) {
            int i5 = i4;
            i4++;
            ts[i5] = new Token(-4, 0.0d, s, t);
        }
        for (String s : procs2) {
            int i6 = i4;
            i4++;
            ts[i6] = new Token(-4, 0.0d, s, t);
        }
        int i7 = i4;
        int i8 = i4 + 1;
        ts[i7] = new Token(-5, 0.0d, "<=", t);
        int i9 = i8 + 1;
        ts[i8] = new Token(-6, 0.0d, "<>", t);
        int i10 = i9 + 1;
        ts[i9] = new Token(-7, 0.0d, ">=", t);
        ts[i10] = new Token(-4, 0.0d, "if", t);
        return ts;
    }

    private Token[] possibleSubstitutions(Token t) {
        return possibleInsertions(t);
    }
}
