package expr;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/Token.class */
class Token {
    public static final int TT_ERROR = -1;
    public static final int TT_EOF = -2;
    public static final int TT_NUMBER = -3;
    public static final int TT_WORD = -4;
    public static final int TT_LE = -5;
    public static final int TT_NE = -6;
    public static final int TT_GE = -7;
    public final int ttype;
    public final String sval;
    public final double nval;
    public final int location;
    public final int leadingWhitespace;
    public final int trailingWhitespace;

    public Token(int ttype, double nval, String input, int start, int end) {
        this.ttype = ttype;
        this.sval = input.substring(start, end);
        this.nval = nval;
        this.location = start;
        int count = 0;
        for (int i = start - 1; 0 <= i && Character.isWhitespace(input.charAt(i)); i--) {
            count++;
        }
        this.leadingWhitespace = count;
        int count2 = 0;
        for (int i2 = end; i2 < input.length() && Character.isWhitespace(input.charAt(i2)); i2++) {
            count2++;
        }
        this.trailingWhitespace = count2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Token(int ttype, double nval, String sval, Token token) {
        this.ttype = ttype;
        this.sval = sval;
        this.nval = nval;
        this.location = token.location;
        this.leadingWhitespace = token.leadingWhitespace;
        this.trailingWhitespace = token.trailingWhitespace;
    }
}
