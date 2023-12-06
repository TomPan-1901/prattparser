import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter an expression: ");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            try {
                Parser p = new Parser(input);
                Node node = null;
                node = p.parseInfix(0);
                System.out.println(node.value);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}