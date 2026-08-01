import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean keepRunning = true;

            System.out.println("=================================");
            System.out.println("     Java Console Calculator     ");
            System.out.println("=================================");

            while (keepRunning) {
                BigDecimal num1 = getValidNumber(scanner, "Enter the first number: ");
                char operator = getValidOperator(scanner);
                BigDecimal num2 = getValidNumber(scanner, "Enter the second number: ");

                if (operator == '/' && num2.compareTo(BigDecimal.ZERO) == 0) {
                    System.out.println("\n[Error] Division by zero is undefined.");
                } else {
                    BigDecimal result = calculate(num1, num2, operator);
                    System.out.println("\nResult: " + num1 + " " + operator + " " + num2 + " = " + result.stripTrailingZeros().toPlainString());
                }

                keepRunning = askToContinue(scanner);
            }

            System.out.println("\nThank you for using the calculator!");
        }
    }

    /**
     * Reads a full line and parses BigDecimal to prevent buffer skips and precision loss.
     */
    private static BigDecimal getValidNumber(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("[Error] '" + input + "' is not a valid number. Please try again.");
            }
        }
    }

    /**
     * Reads operator using full-line input to avoid left-over newline bugs.
     */
    private static char getValidOperator(Scanner scanner) {
        while (true) {
            System.out.print("Enter an operator (+, -, *, /): ");
            String input = scanner.nextLine().trim();

            if (input.length() == 1) {
                char op = input.charAt(0);
                if (op == '+' || op == '-' || op == '*' || op == '/') {
                    return op;
                }
            }
            System.out.println("[Error] Invalid operator. Please enter +, -, *, or /.");
        }
    }

    /**
     * Performs exact arithmetic with scale handling for non-terminating decimals (like 1/3).
     */
    private static BigDecimal calculate(BigDecimal a, BigDecimal b, char operator) {
        switch (operator) {
            case '+': return a.add(b);
            case '-': return a.subtract(b);
            case '*': return a.multiply(b);
            case '/': 
                // Scale set to 10 decimal places with HALF_UP rounding to handle infinite repeating decimals
                return a.divide(b, 10, RoundingMode.HALF_UP);
            default:
                throw new IllegalArgumentException("Unexpected operator: " + operator);
        }
    }

    /**
     * Safely checks continuation without leaving orphaned tokens in the buffer.
     */
    private static boolean askToContinue(Scanner scanner) {
        while (true) {
            System.out.print("\nDo you want to perform another calculation? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y") || response.equals("yes")) {
                System.out.println("---------------------------------");
                return true;
            } else if (response.equals("n") || response.equals("no")) {
                return false;
            }
            System.out.println("[Error] Please enter 'y' for yes or 'n' for no.");
        }
    }
}
