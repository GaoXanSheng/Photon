package expr;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/SyntaxException.class */
public class SyntaxException extends Exception {
    public static final int INCOMPLETE = 0;
    public static final int BAD_FACTOR = 1;
    public static final int PREMATURE_EOF = 2;
    public static final int EXPECTED = 3;
    public static final int UNKNOWN_VARIABLE = 4;
    private Parser parser;
    private Scanner scanner;
    private int reason;
    private String expected;
    private String fixedInput;

    public SyntaxException(String complaint, Parser parser, int reason, String expected) {
        super(complaint);
        this.fixedInput = "";
        this.reason = reason;
        this.parser = parser;
        this.scanner = parser.tokens;
        this.expected = expected;
    }

    public String explain() {
        StringBuffer sb = new StringBuffer();
        sb.append("I don't understand your formula ");
        quotify(sb, this.scanner.getInput());
        sb.append(".\n\n");
        explainWhere(sb);
        explainWhy(sb);
        explainWhat(sb);
        return sb.toString();
    }

    private void explainWhere(StringBuffer sb) {
        if (this.scanner.isEmpty()) {
            sb.append("It's empty!\n");
        } else if (this.scanner.atStart()) {
            sb.append("It starts with ");
            quotify(sb, theToken());
            if (isLegalToken()) {
                sb.append(", which can never be the start of a formula.\n");
            } else {
                sb.append(", which is a meaningless symbol to me.\n");
            }
        } else {
            sb.append("I got as far as ");
            quotify(sb, asFarAs());
            sb.append(" and then ");
            if (this.scanner.atEnd()) {
                sb.append("reached the end unexpectedly.\n");
                return;
            }
            sb.append("saw ");
            quotify(sb, theToken());
            if (isLegalToken()) {
                sb.append(".\n");
            } else {
                sb.append(", which is a meaningless symbol to me.\n");
            }
        }
    }

    private void explainWhy(StringBuffer sb) {
        switch (this.reason) {
            case 0:
                if (isLegalToken()) {
                    sb.append("The first part makes sense, but I don't see how the rest connects to it.\n");
                    return;
                }
                return;
            case 1:
            case 2:
                sb.append("I expected a value");
                if (!this.scanner.atStart()) {
                    sb.append(" to follow");
                }
                sb.append(", instead.\n");
                return;
            case 3:
                sb.append("I expected ");
                quotify(sb, this.expected);
                sb.append(" at that point, instead.\n");
                return;
            case 4:
                sb.append("That variable has no value.\n");
                return;
            default:
                throw new Error("Can't happen");
        }
    }

    private void explainWhat(StringBuffer sb) {
        this.fixedInput = tryToFix();
        if (null != this.fixedInput) {
            sb.append("An example of a formula I can parse is ");
            quotify(sb, this.fixedInput);
            sb.append(".\n");
        }
    }

    private String tryToFix() {
        if (this.parser.tryCorrections()) {
            return this.scanner.toString();
        }
        return null;
    }

    private void quotify(StringBuffer sb, String s) {
        sb.append('\"');
        sb.append(s);
        sb.append('\"');
    }

    private String asFarAs() {
        Token t = this.scanner.getCurrentToken();
        int point = t.location - t.leadingWhitespace;
        return this.scanner.getInput().substring(0, point);
    }

    private String theToken() {
        return this.scanner.getCurrentToken().sval;
    }

    private boolean isLegalToken() {
        Token t = this.scanner.getCurrentToken();
        return (t.ttype == -2 || t.ttype == -1) ? false : true;
    }
}
