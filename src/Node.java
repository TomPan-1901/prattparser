public class Node {
    Token token;
    Node left;
    Node right;
    double value;
    boolean solved;
    Parser parser;

    Node(Parser parser, Token token, Node left, Node right) {
        this.parser = parser;
        this.token = token;
        this.left = left;
        this.right = right;
        this.value = 0.0;
        this.solved = false;
        solve();
    }

    public Node (Parser parser, Token token) {
        this.parser = parser;
        this.token = token;
        this.left = null;
        this.right = null;
    }

    @Override
    public String toString() {
        return "Node{\n" +
                "   token=" + token +
                ",\n    left=" + left +
                ",\n    right=" + right +
                "\n}";
    }

    private double solve() {
        if (this.solved) {
            return this.value;
        }
        if (this.left == null && this.right == null) {
            this.solved = true;
            this.value = Double.parseDouble(parser.input.substring(this.token.left, this.token.right));
            return this.value;
        }
        if (this.left == null || this.right != null) {
            this.solved = true;
            if (this.token.type == TokenType.MINUS) {
                this.value = -this.left.solve();
            } else {
                this.value = this.left.solve();
            }
        }
        switch (token.type) {
            case PLUS:
                this.value = this.left.solve() + this.right.solve();
                break;
            case MINUS:
                this.value = this.left.solve() - this.right.solve();
                break;
            case MULTIPLY:
                this.value = this.left.solve() * this.right.solve();
                break;
            case DIVIDE:
                this.value = this.left.solve() / this.right.solve();
                break;
            case MOD:
                this.value = this.left.solve() % this.right.solve();
                break;
            case EXPONENT:
                this.value = Math.pow(this.left.solve(), this.right.solve());
                break;
            case ABS:
                this.value = Math.abs(this.left.solve());
                break;
            case SQRT:
                this.value = Math.sqrt(this.left.solve());
                break;
            case EXP:
                this.value = Math.exp(this.left.solve());
                break;
            case LN:
                this.value = Math.log(this.left.solve());
                break;
            case LOG10:
                this.value = Math.log10(this.left.solve());
                break;
            case SIN:
                this.value = Math.sin(this.left.solve());
                break;
            case COS:
                this.value = Math.cos(this.left.solve());
                break;
            case TANH:
                this.value = Math.tanh(this.left.solve());
                break;
            default:
                String message = "Unknown token type" + token.type;
                String pos = parser.input + "\n" + " ".repeat(token.left) + "^";
                throw new RuntimeException("Unknown token type" + token.type);
        }
        return this.value;
    }
}
