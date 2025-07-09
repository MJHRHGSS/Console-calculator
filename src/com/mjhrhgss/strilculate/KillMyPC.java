package com.mjhrhgss.strilculate;

import java.util.*;
import static com.mjhrhgss.strilculate.Main.*;

public class KillMyPC {
    public static class ArithmeticKMPC extends KillMyPC {
        private static double res = 0d;
        public static double calculate(List<Object> tokens) {
            if (logging) System.out.println("[calculate] Starting with tokens: " + tokens);
            String[][] precedenseLevels = {{"("}, {"!"}, {"^", "#"}, {"*", "/"}, {"+", "-"}};
            for (;tokens.size() > 1;) parse(tokens, precedenseLevels);
            if (logging) System.out.printf("[calculate] Final tokens: %s\n", tokens);
            if (logging) System.out.printf("[calculate] Returning result: %.4f\n", res);
            return res;
        }
        private static double exponent(double a, double b) {
            if (logging) System.out.printf("[exponent] Calculating %.4f raised to the power of %.1f\n", a, b);
            double base = a;
            for (int i = (int) b - 1; i > 0; i--) a *= base;
            double i = 0d;
            for (;Math.abs(i - a) >= 1e-10; i += 1e-10) if (logging) System.out.printf("[exponent] Updated i: %.10f\n", i);
            return i;
        }
        private static double factorial(double a) {
            if (logging) System.out.println("[factorial] Calculating factorial of " + a);
            for (int i = (int) a - 1; i > 1; i--) a *= i;
            double i = 0d;
            for (;Math.abs(i - a) >= 1e-10; i += 1e-10) if (logging) System.out.printf("[factorial] Updated i: %.10f\n", i);
            return i;
        }
        private static double add(double a, double b) {
            double i = 0d;
            if (a + b > 0) for (;Math.abs(i - (a + b)) >= 1e-10; i += 1e-10) if (logging) System.out.printf("[add] Updated i: %.10f\n", i);
            else if (a - b < 0) for (;Math.abs(i - (a + b)) >= 1e-10; i -= 1e-10) if (logging) System.out.printf("[add] Updated i: %.10f\n", i);
            return i;
        }
        private static double subtract(double a, double b) {
            double i = 0d;
            if (a - b > 0) for (;Math.abs(i - (a - b)) >= 1e-10; i += 1e-10) if (logging) System.out.printf("[subtract] Updated i: %.10f\n", i);
            else if (a + b < 0) for (;Math.abs(i - (a - b)) >= 1e-10; i -= 1e-10) if (logging) System.out.printf("[subtract] Updated i: %.10f\n", i);;
            return i;
        }
        private static double multiply(double a, double b) {
            double i = 0d;
            if (a * b > 0) for (;Math.abs(i - (a * b)) >= 1e-10; i += 1e-10) if (logging) System.out.printf("[multiply] Updated i: %.10f\n", i);
            else if (a * b < 0) for (;Math.abs(i - (a * b)) >= 1e-10; i-= 1e-10) if (logging) System.out.printf("[multiply] Updated i: %.10f\n", i);
            return i;
        }
        private static double divide(double a, double b) {
            double i = 0d;
            if (a / b > 0) for (;Math.abs(i - (a / b)) >= 1e-10; i += 1e-10) if (logging) System.out.printf("[divide] Updated i: %.10f\n", i);
            else if (a / b < 0) for (;Math.abs(i - (a / b)) >= 1e-10; i -= 1e-10) if (logging) System.out.printf("[divide] Updated i: %.10f\n", i);
            return i;
        }
        private static double sqrt(double a) {
            double i = 0d;
            for (;Math.abs(i - Math.sqrt(a)) >= 1e-10; i += 1e-10) if (logging) System.out.printf("[sqrt] Updated i: %.10f\n", i);
            return i;
        }
        private static void parse(List<Object> tokens, String[][] precedenseLevels) {
            for (String[] level : precedenseLevels) {
                if (logging) System.out.printf("[calculate] Checking for tokens in level: %s\n", Arrays.toString(level));
                Optional<String> operator = tokens.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .filter(s -> Arrays.asList(level).contains(s))
                    .findFirst();
                if (operator.isPresent()) {
                    String op = operator.get();
                    if (logging) System.out.printf("[calculate] Found operator: %s\n", op);
                    char c = op.charAt(0);
                    double a = 0d;
                    if (!op.equals("!") && !op.equals("#")) {
                        for (Object o : tokens) {
                           if (o instanceof Double) {
                                a = (double) o;
                                if (logging) System.out.printf("[calculate] Assigning operand 'a' the value of: %.4f at index of %d of tokens\n", a, tokens.indexOf(o));
                                break;
                            }
                        }
                    } else {
                        if (op.equals("!")) {
                            a = (double) tokens.get(tokens.indexOf((Object) op) - 1);
                            if (logging) System.out.printf("[calculate] Assigning operand 'a' the value of: %.4f at index of %d of tokens\n", a, tokens.indexOf((Object) op) + 1);
                        }
                        if (op.equals("#")) {
                            a = (double) tokens.get(tokens.indexOf((Object) op) + 1);
                            if (logging) System.out.printf("[calculate] Assigning operand 'a' the value of: %.4f at index of %d of tokens\n", a, tokens.indexOf(a));
                        }
                    }
                    double b = 0d;
                    if (!op.equals("!") && !op.equals("#")) {
                        int i = -1;
                        if (logging) System.out.println("[calculate] Searching for operand b...");
                        for (Object o : tokens) {
                            i++;
                            if (i == 0 || o instanceof String) continue;
                            b = (double) o;
                            if (logging) System.out.printf("[calculate] Assigning operand 'b' the value of %.4f from the index of %d of tokens\n", b, i);
                            break;
                        }
                    }
                    switch (c) {
                        case '+' -> res = add(a, b);
                        case '-' -> res = subtract(a, b);
                        case '*' -> res = multiply(a , b);
                        case '/' -> {
                            if (b == 0) throw new ArithmeticException("Cannot divide by 0");
                            res = divide(a, b);
                        }
                        case '^' -> res = exponent(a, b);
                        case '!' -> res = factorial(a);
                        case '#' -> res = sqrt(a);
                        case '(' -> {
                            int firstParen = tokens.lastIndexOf("(");
                            int secondParen = tokens.indexOf(")");
                            List<Object> newTokens = tokens.subList(firstParen + 1, secondParen);
                            res = calculate(newTokens);
                            firstParen = tokens.lastIndexOf("(");
                            secondParen = tokens.indexOf(")");
                            tokens.subList(firstParen + 1, secondParen + 1).clear();
                            tokens.set(firstParen, res);
                            if (logging) System.out.printf("[calculate] Updated tokens: %s\n", tokens);
                        }
                        default -> throw new ArithmeticException("Unknown operator: " + op);
                    }
                    if (logging) System.out.printf("[calculate] Calculated result of operation '%s': %f\n", op, res);
                    if (!op.equals("(")) {
                        tokens.set(tokens.indexOf(a), res);
                        tokens.remove(op);
                        if (!op.equals("#") && !op.equals("!")) tokens.remove(b);
                        if (logging) System.out.printf("[calculate] Updated tokens: %s\n", tokens);
                    }
                }
            }
        }
    }
    public static class AlgebraicKMPC extends KillMyPC {}
}
