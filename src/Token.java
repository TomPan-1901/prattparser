public class Token {

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    public int[] getBindingPower() {
        switch (type) {
            case PLUS, MINUS:
                return new int[] {10, 9};
            case MULTIPLY, MOD, DIVIDE:
                return new int[] {20, 19};
            case EXPONENT:
                return new int[] {30, 29};
            default:
                throw new RuntimeException("Unknown token type" + type);
        }
    }

    TokenType type;

    int left;
    int right;

    public Token(TokenType type, int left, int right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }
}
