/*
Task In our project
• Allows users to input logical expressions
• Automatically generates complete truth tables
• Converts between different logical notation forms (infix, prefix, postfix)
• Simplifies logical expressions using Boolean algebra rules
• Visualizes logical operations and equivalences
*/
/*
Create By:
    Muslim Mahmood
    Muhammad Jalal
    Darw Kamal
    Ahmad Jabar

    Date: 17/12/2024
 */
import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Input logical expression
        System.out.println("Enter a logical expression in infix notation (EX: A & B | C: ");
        String infixExpression = scanner.nextLine();

        // Step 2: Convert to postfix and prefix notations
        String postfixExpression = infixToPostfix(infixExpression);
        String prefixExpression = infixToPrefix(infixExpression);

        System.out.println("\nConverted Notations:");
        System.out.println("-Postfix: " + postfixExpression);
        System.out.println("-Prefix: " + prefixExpression);

        // Step 3: Generate and display truth table
        System.out.println("\nTruth Table:");
        generateTruthTable(infixExpression);

        // Step 5: Visualize logical operations and equivalences
        visualizeOperations();

        scanner.close();
    }

    // Convert infix to postfix notation
    public static String infixToPostfix(String expression) {
        Stack<Character> stack = new Stack<>();
        String postfix = "";

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isLetter(ch)) {
                postfix += ch;
            } else if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix += stack.pop();
                }
                stack.pop();
            } else if (isOperator(ch)) {
                while (!stack.isEmpty() && precedence(ch) <= precedence(stack.peek())) {
                    postfix += stack.pop();
                }
                stack.push(ch);
            }
        }
        while (!stack.isEmpty()) {
            postfix += stack.pop();
        }
        return postfix;
    }

    // Convert infix to prefix notation
    public static String infixToPrefix(String expression) {
        char[] chars = expression.toCharArray();
        reverse(chars);
        replaceBrackets(chars);
        String postfix = infixToPostfix(new String(chars));
        chars = postfix.toCharArray();
        reverse(chars);
        return new String(chars);
    }

    // Helper method to reverse a character array
    private static void reverse(char[] array) {
        int start = 0;
        int end = array.length-1;
        while (start < end) {
            char temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
        }
    }

    // Helper method to replace brackets
    private static void replaceBrackets(char[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '(') {
                array[i] = ')';
            } else if (array[i] == ')') {
                array[i] = '(';
            }
        }
    }

    // Helper method to find index of a variable
    public static int getIndex(char[] array, char key) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == key) {
                return i;
            }
        }
        return -1;
    }

    // Helper: Check if a character is an operator
    public static boolean isOperator(char ch) {
        return ch == '&' || ch == '|' || ch == '!' || ch == '^' || ch == '~' || ch == '%';
    }





    // Evaluate logical expression for given truth values
    public static boolean evaluateExpression(String expression, char[] variables, boolean[] values) {
        Stack<Boolean> stack = new Stack<>();

        String postfix = infixToPostfix(expression);
        for (int i = 0; i < postfix.length(); i++) {
            char ch = postfix.charAt(i);

            if (Character.isLetter(ch)) {
                int index = getIndex(variables, ch);
                stack.push(values[index]);
            } else {
                boolean b = stack.pop();
                boolean a = stack.isEmpty() ? false : stack.pop();

                switch (ch) {
                    case '&':
                        stack.push(a && b);
                        break;
                    case '|':
                        stack.push(a || b);
                        break;
                    case '!':
                        stack.push(!b);
                        break;
                    case '^':
                        stack.push(a ^ b);
                        break;
                    case '~':
                        stack.push(!(a || b));
                        break;
                    case '%':
                        stack.push(!(a && b));
                        break;
                }
            }
        }
        return stack.pop();
    }

    // Extract variables from the expression
    public static char[] getVariables(String expression) {
        String vars = "";
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (Character.isLetter(ch) && vars.indexOf(ch) == -1) {
                vars += ch;
            }
        }
        char[] varArray = new char[vars.length()];
        for (int i = 0; i < vars.length(); i++) {
            varArray[i] = vars.charAt(i);
        }
        return varArray;
    }

    // Generate truth table for the logical expression
    public static void generateTruthTable(String expression) {
        char[] variables = getVariables(expression);
        int rows = (int) Math.pow(2, variables.length);

        // Print header
        for (char var : variables) {
            System.out.print(var + "\t");
        }
        System.out.println("Result");

        // Generate truth table rows
        for (int i = 0; i < rows; i++) {
            boolean[] values = new boolean[variables.length];
            for (int j = 0; j < variables.length; j++) {
                values[j] = (i & (1 << (variables.length - j - 1))) != 0;
                System.out.print(values[j] ?'T' : 'F');
                System.out.print("\t");
            }

            boolean result = evaluateExpression(expression, variables, values);
            System.out.println(result ? 'T' : 'F');
        }
    }

    // Helper: Define operator precedence
    public static int precedence(char operator) {
        switch (operator) {
            case '!':
                return 4; // NOT
            case '&':
                return 3; // AND
            case '|':
            case '^':
                return 2; // OR, XOR
            case '~':
            case '%':
                return 1; // NOR, NAND
            default:
                return 0;
        }
    }
    // Visualize logical operations and equivalences
    public static void visualizeOperations() {
        System.out.println("\nLogical Operations and Equivalences:");
        System.out.println("A & 1 = A");
        System.out.println("A & 0 = 0");
        System.out.println("A | 1 = 1");
        System.out.println("A | 0 = A");
        System.out.println("A ^ 1 = !A");
        System.out.println("A ^ 0 = A");
        System.out.println("!A & !B = !(A | B)");
        System.out.println("!A | !B = !(A & B)");
    }
}
