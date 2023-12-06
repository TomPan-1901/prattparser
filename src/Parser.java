public class Parser {
    String input;
    Token currentToken;
    int ptr;
    public Parser(String input) {
        this.input = input;
        this.currentToken = null;
        this.ptr = 0;
        consume();
    }

    public Node parsePrefix() {
        Token prefixToken = peek();
        Node left = null;
        switch (prefixToken.type) {
            case LN, ABS, SQRT, EXP, LOG10, SIN, COS, TANH:
                consume();
                left = new Node(this, prefixToken, parseInfix(0), null);
                if (peek().type != TokenType.RPAREN) {
                    String message = "Expected RPAREN at " + ptr;
                    String pos = input + "\n" + " ".repeat(ptr) + "^";
                    throw new RuntimeException(message + "\n" + pos);
                }
                break;
            case LPAREN:
                consume();
                left = parseInfix(0);
                if (peek().type != TokenType.RPAREN) {
                    String message = "Expected RPAREN at " + ptr;
                    String pos = input + "\n" + " ".repeat(ptr) + "^";
                    throw new RuntimeException(message + "\n" + pos);
                }
                break;
            case MINUS, PLUS:
                left = new Node(this, prefixToken, parseInfix(Integer.MAX_VALUE), null);
                break;
            case NUMBER:
                left = new Node(this, prefixToken, null, null);
                break;
            default:
                break;
        }
        consume();
        return left;
    }

    public Node parseInfix(int precedence) {
        Node left = parsePrefix();
        while (peek().type != TokenType.EOF && peek().type != TokenType.RPAREN) {
            Token infixToken = peek();
            if (infixToken.type == TokenType.EOF || infixToken.type == TokenType.RPAREN) {
                break;
            }
            int[] bindingPower = infixToken.getBindingPower();
            if (bindingPower[0] < precedence) {
                break;
            }
            consume();
            Node right = parseInfix(bindingPower[1]);
            left = new Node(this, infixToken, left, right);

        }
        return left;
    }

    private Token peek() {
        return currentToken;
    }

    private Token consume() {
        getNextToken();
        return currentToken;
    }

    private void getNextToken() {
        while (ptr < input.length() && Character.isWhitespace(input.charAt(ptr))) {
            ptr++;
        }
        if (ptr >= input.length())
        {
            currentToken = new Token(TokenType.EOF, ptr, ptr);
            return;
        }
        switch (input.charAt(ptr)) {
            case '+':
                currentToken = new Token(TokenType.PLUS, ptr, ptr + 1);
                ptr++;
                break;
            case '-':
                currentToken = new Token(TokenType.MINUS, ptr, ptr + 1);
                ptr++;
                break;
            case '*':
                if (ptr + 1 < input.length() && input.charAt(ptr + 1) == '*')
                {
                    currentToken = new Token(TokenType.EXPONENT, ptr, ptr + 2);
                    ptr += 2;
                }
                else
                {
                    currentToken = new Token(TokenType.MULTIPLY, ptr, ptr + 1);
                    ptr++;
                }
                break;
            case '/':
                currentToken = new Token(TokenType.DIVIDE, ptr, ptr + 1);
                ptr++;
                break;
            case '%':
                currentToken = new Token(TokenType.MOD, ptr, ptr + 1);
                ptr++;
                break;
                case '(':
                currentToken = new Token(TokenType.LPAREN, ptr, ptr + 1);
                ptr++;
                break;
            case ')':
                currentToken = new Token(TokenType.RPAREN, ptr, ptr + 1);
                ptr++;
                break;
            default:
                if (Character.isDigit(input.charAt(ptr))) {
                    currentToken = handleNumber();
                }
                else if (Character.isLetter(input.charAt(ptr))) {
                    currentToken = handleFunction();
                }
                else {
                    String message = "Invalid character at " + ptr;
                    String pos = input + "\n" + " ".repeat(ptr) + "^";
                    throw new RuntimeException(message + "\n" + pos);
                }
                break;
        }
    }

    private Token handleNumber() {
        int start = ptr;
        boolean visitedDot = false;
        while (ptr < input.length()) {
            if (input.charAt(ptr) == '.') {
                if (visitedDot) {
                    String message = "Invalid number at " + ptr;
                    String pos = input + "\n" + " ".repeat(ptr) + "^";
                    throw new RuntimeException(message + "\n" + pos);
                }
                visitedDot = true;
            }
            else if (!Character.isDigit(input.charAt(ptr))) {
                break;
            }
            ptr++;
        }
        return new Token(TokenType.NUMBER, start, ptr);
    }

    private Token handleFunction() {
        int start = ptr;
        while (ptr < input.length() && Character.isLetter(input.charAt(ptr))) {
            ptr++;
        }
        if (ptr >= input.length() || input.charAt(ptr) != '(') {
            String message = "Invalid function at " + ptr;
            String pos = input + "\n" + " ".repeat(ptr) + "^";
            throw new RuntimeException(message + "\n" + pos);
        }
        ptr++;
        String func = input.substring(start, ptr);
        switch (func) {
            case "abs(":
                return new Token(TokenType.ABS, start, ptr);
            case "sqrt(":
                return new Token(TokenType.SQRT, start, ptr);
            case "exp(":
                return new Token(TokenType.EXP, start, ptr);
            case "ln(":
                return new Token(TokenType.LN, start, ptr);
            case "log10(":
                return new Token(TokenType.LOG10, start, ptr);
            case "sin(":
                return new Token(TokenType.SIN, start, ptr);
            case "cos(":
                return new Token(TokenType.COS, start, ptr);
            case "tanh(":
                return new Token(TokenType.TANH, start, ptr);
            default:
                String message = "Invalid function at " + ptr;
                String pos = input + "\n" + " ".repeat(ptr) + "^";
                throw new RuntimeException(message + "\n" + pos);
        }
    }
}

