package expr;

import java.util.Vector;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:expr/Scanner.class */
class Scanner {
    private String s;
    private String operatorChars;
    Vector tokens = new Vector();
    int index = -1;

    public Scanner(String string, String operatorChars) {
        this.s = string;
        this.operatorChars = operatorChars;
        int i = 0;
        do {
            i = scanToken(i);
        } while (i < this.s.length());
    }

    public String getInput() {
        return this.s;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        int whitespace = 0;
        for (int i = 0; i < this.tokens.size(); i++) {
            Token t = (Token) this.tokens.elementAt(i);
            int spaces = whitespace != 0 ? whitespace : t.leadingWhitespace;
            if (i == 0) {
                spaces = 0;
            } else if (spaces == 0 && !joinable((Token) this.tokens.elementAt(i - 1), t)) {
                spaces = 1;
            }
            for (int j = spaces; 0 < j; j--) {
                sb.append(" ");
            }
            sb.append(t.sval);
            whitespace = t.trailingWhitespace;
        }
        return sb.toString();
    }

    private boolean joinable(Token s, Token t) {
        return (isAlphanumeric(s) && isAlphanumeric(t)) ? false : true;
    }

    private boolean isAlphanumeric(Token t) {
        return t.ttype == -4 || t.ttype == -3;
    }

    public boolean isEmpty() {
        return this.tokens.size() == 0;
    }

    public boolean atStart() {
        return this.index <= 0;
    }

    public boolean atEnd() {
        return this.tokens.size() <= this.index;
    }

    public Token nextToken() {
        this.index++;
        return getCurrentToken();
    }

    public Token getCurrentToken() {
        if (atEnd()) {
            return new Token(-2, 0.0d, this.s, this.s.length(), this.s.length());
        }
        return (Token) this.tokens.elementAt(this.index);
    }

    private int scanToken(int i) {
        while (i < this.s.length() && Character.isWhitespace(this.s.charAt(i))) {
            i++;
        }
        if (i == this.s.length()) {
            return i;
        }
        if (0 <= this.operatorChars.indexOf(this.s.charAt(i))) {
            if (i + 1 < this.s.length()) {
                String pair = this.s.substring(i, i + 2);
                int ttype = 0;
                if (pair.equals("<=")) {
                    ttype = -5;
                } else if (pair.equals(">=")) {
                    ttype = -7;
                } else if (pair.equals("<>")) {
                    ttype = -6;
                }
                if (0 != ttype) {
                    this.tokens.addElement(new Token(ttype, 0.0d, this.s, i, i + 2));
                    return i + 2;
                }
            }
            this.tokens.addElement(new Token(this.s.charAt(i), 0.0d, this.s, i, i + 1));
            return i + 1;
        } else if (Character.isLetter(this.s.charAt(i))) {
            return scanSymbol(i);
        } else {
            if (Character.isDigit(this.s.charAt(i)) || '.' == this.s.charAt(i)) {
                return scanNumber(i);
            }
            this.tokens.addElement(makeErrorToken(i, i + 1));
            return i + 1;
        }
    }

    private int scanSymbol(int i) {
        while (i < this.s.length() && (Character.isLetter(this.s.charAt(i)) || Character.isDigit(this.s.charAt(i)))) {
            i++;
        }
        this.tokens.addElement(new Token(-4, 0.0d, this.s, i, i));
        return i;
    }

    private int scanNumber(int i) {
        while (i < this.s.length() && ('.' == this.s.charAt(i) || Character.isDigit(this.s.charAt(i)) || Character.isLetter(this.s.charAt(i)))) {
            i++;
        }
        String text = this.s.substring(i, i);
        try {
            double nval = Double.valueOf(text).doubleValue();
            this.tokens.addElement(new Token(-3, nval, this.s, i, i));
            return i;
        } catch (NumberFormatException e) {
            this.tokens.addElement(makeErrorToken(i, i));
            return i;
        }
    }

    private Token makeErrorToken(int from, int i) {
        return new Token(-1, 0.0d, this.s, from, i);
    }
}
