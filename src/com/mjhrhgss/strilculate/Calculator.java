package com.mjhrhgss.strilculate;

import java.util.concurrent.*;
import java.util.*;
import static com.mjhrhgss.strilculate.Main.*;

public class Calculator { 
    public static class Arithmetic extends Calculator {
        private static double res = 0d;
        public static double calculate(List<Object> tokens) {
            if (logging) System.out.println("[calculate] Starting with tokens: " + tokens);
            String[][] precedenseLevels = {{"("}, {"sin<", "cos<", "tan<"}, {"!"}, {"^", "#"}, {"*", "/"}, {"+", "-"}};
            if (logging) System.out.println("[calculate] Parsing...");
            for (;tokens.size() > 1;) parse(tokens, precedenseLevels);
            if (logging) System.out.printf("[calculate] Final tokens: %s\n", tokens);
            if (isNumber(tokens.get(0))) res = (double) tokens.get(0);
            if (logging) System.out.printf("[calculate] Returning result: %.4f\n", res);
            return res;
        }
        private static boolean isNumber(Object num) {
            System.out.printf("[isNumber] Checking if '%s' is a valid number...\n", num);
            try {
                double check = Double.parseDouble(String.valueOf(num));
                if (logging) System.out.printf("[isNumber] '%s' is a valid number.\n", num);
                return true;
            } catch (NumberFormatException e) {
                if (logging) System.out.printf("[isNumber] '%s' is not a valid number.\n", num);
                return false;
            }
        }
        private static double exponent(double a, double b) {
            if (logging) System.out.printf("[exponent] Calculating %.4f raised to the power of %.1f\n", a, b);
            double base = a;
            for (int i = Math.abs((int) b - 1); i > 0; i--) {
                a *= base;
                if (logging) System.out.println("[exponent] Intermediate result after multiplying by base: " + a);
            }
            if (b < 0) a = 1 / a;
            if (logging) System.out.println("[exponent] Final result: " + a);
            return a;
        }
        private static double factorial(double a) {
            if (logging) System.out.println("[factorial] Calculating factorial of " + a);
            for (int i = (int) a - 1; i > 1; i--) {
                a *= i;
                if (logging) System.out.println("[factorial] Intermediate factorial value: " + a);
            }
            if (logging) System.out.println("[factorial] Final factorial result: " + a);
            return a;
        }
        public static double toRadians(double a) {return a * Consts.PI / 180.0d;}
        private static double toDegrees(double a) {return a * 180.0d / Consts.PI;}
        private static double sin(double a) {
            a = toRadians(a);
            double res = 0d;
            a %= Consts.PI * 2;
            if (a > Consts.PI) a -= Consts.PI * 2;
            else if (a < -Consts.PI) a += Consts.PI * 2;
            if (logging) System.out.printf("[sin] Normalized input: %.6f\n", a);
            double term = a;
            int n = 1;
            for (int i = 0; i < 20; i++) {
                res += term;
                if (logging) System.out.printf("[sin] Term %d: %.10f (n=%d), result so far: %.10f\n", i, term, n, res);
                n += 2;
                term *= -a * a / (n * (n - 1));
            }
            if (logging) System.out.println("[sin] Final result: " + res);
            return res;
        }
        private static double cos(double a) {
            a = toRadians(a);
            double res = 0d;
            a %= Consts.PI * 2;
            if (a > Consts.PI) a -= Consts.PI * 2;
            else if (a < -Consts.PI) a += Consts.PI * 2;
            if (logging) System.out.printf("[cos] Normalized input: %.6f\n", a);
            double term = 1.0d;
            int n = 0;
            for (int i = 0; i < 20; i++) {
                res += term;
                if (logging) System.out.printf("[cos] Term %d: %.10f (n=%d), result so far: %.10f\n", i, term, n, res);
                n += 2;
                term *= -a * a / (n * (n -1));
            }
            if (logging) System.out.println("[cos] Final result: " + res);
            return res;
        }
        private static double tan(double a) {
            a = toRadians(a);
            double res = 0d;
            double sin = sin(a), cos = cos(a);
            if (logging) System.out.printf("[tan] Dividing sin(x): %.10f by cos(x): %.10f", sin, cos);
            res = sin / cos;
            if (logging) System.out.println("[tan] Final result: " + res);
            return res;
        }
        private static void parse(List<Object> tokens, String[][] precedenseLevels) {
            for (String[] level : precedenseLevels) {
                if (logging) System.out.printf("[parse] Checking for tokens in level: %s\n", Arrays.toString(level));
                Optional<String> operator = tokens.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .filter(s -> Arrays.asList(level).contains(s))
                    .findFirst();
                if (operator.isPresent()) {
                    String op = operator.get();
                    if (logging) System.out.printf("[parse] Found operator: %s\n", op);
                    if (!op.equals("sin<") && !op.equals("cos<") && !op.equals("tan<")) {
                        char c = op.charAt(0);
                        double a = 0d;
                        int indexOfA = 0;
                        if (!op.equals("!") && !op.equals("#")) {
                            for (Object o : tokens) {
                               if (o instanceof Double) {
                                    a = (double) o;
                                    indexOfA = tokens.indexOf(o);
                                    if (logging) System.out.printf("[parse] Assigning operand 'a' the value of: %.4f at index of %d of tokens\n", a, tokens.indexOf(o));
                                    if ((!op.equals("#") && tokens.indexOf(o) == tokens.indexOf((Object) op) - 1) || (op.equals("#") && tokens.indexOf(o) ==tokens.indexOf((Object) op) + 1)) break;
                                }
                            }
                        } else {
                            if (op.equals("!")) {
                                a = (double) tokens.get(tokens.indexOf((Object) op) - 1);
                                if (logging) System.out.printf("[parse] Assigning operand 'a' the value of: %.4f at index of %d of tokens\n", a, tokens.indexOf((Object) op) + 1);
                            }
                            if (op.equals("#")) {
                                a = (double) tokens.get(tokens.indexOf((Object) op) + 1);
                                if (logging) System.out.printf("[parse] Assigning operand 'a' the value of: %.4f at index of %d of tokens\n", a, tokens.indexOf(a));
                            }
                        }
                        double b = 0d;
                        if (!op.equals("!") && !op.equals("#")) {
                            int i = -1;
                            if (logging) System.out.println("[parse] Searching for operand b...");
                            for (Object o : tokens) {
                                i++;
                                if (i == 0 || !(o instanceof Double) || i == indexOfA) continue;
                                b = (double) o;
                                if (logging) System.out.printf("[parse] Assigning operand 'b' the value of %.4f from the index of %d of tokens\n", b, i);
                                break;
                            }
                        }
                        switch (c) {
                            case '+' -> res = a + b;
                            case '-' -> res = a - b;
                            case '*' -> res = a * b;
                            case '/' -> {
                                if (b == 0) throw new ArithmeticException("Cannot divide by 0");
                                res = a / b;
                            }
                            case '^' -> res = exponent(a, b);
                            case '!' -> res = factorial(a);
                            case '#' -> res = Math.sqrt(a);
                            case '(' -> {
                                int firstParen = tokens.lastIndexOf("(");
                                int secondParen = tokens.indexOf(")");
                                List<Object> newTokens = tokens.subList(firstParen + 1, secondParen);
                                res = calculate(newTokens);
                                firstParen = tokens.lastIndexOf("(");
                                secondParen = tokens.indexOf(")");
                                tokens.subList(firstParen + 1, secondParen + 1).clear();
                                tokens.set(firstParen, res);
                                if (logging) System.out.printf("[parse] Updated tokens: %s\n", tokens);
                            }
                            default -> throw new ArithmeticException("Unknown operator: " + op);
                        }
                        if (logging) System.out.printf("[parse] Calculated result of operation '%s': %f\n", op, res);
                        if (!op.equals("(")) {
                            tokens.set(tokens.indexOf(a), res);
                            tokens.remove(op);
                            if (!op.equals("#") && !op.equals("!")) tokens.remove(b);
                            if (logging) System.out.printf("[parse] Updated tokens: %s\n", tokens);
                        }
                    } else {
                        int firstParen = tokens.lastIndexOf(op);
                        int secondParen = tokens.indexOf(">");
                        List<Object> newTokens = new ArrayList<>(tokens.subList(firstParen + 1, secondParen));
                        res = calculate(newTokens);
                        firstParen = tokens.lastIndexOf(op);
                        secondParen = tokens.indexOf(">");
                        tokens.subList(firstParen + 1, secondParen + 1).clear();
                        tokens.set(firstParen, op.equals("sin<") ? sin(res) : op.equals("cos<") ? cos(res) : tan(res));
                        if (logging) System.out.printf("[parse] Updated tokens: %s\n", tokens);    
                    }
                }
            }
        }
    }
    public static class Algebraic extends Calculator {
        private static double res = 0d;
        public static double calculate(List<Object> tokens) {
            if (logging) System.out.println("[calculate] Starting with tokens: " + tokens);
            int tempInt = 0;
            while (tempInt < tokens.size() - 1) {
                boolean foundVar = false;
                Object current = tokens.get(tempInt), next = tokens.get(tempInt + 1);
                Object previous = null;
                if (tempInt > 0) previous = tokens.get(tempInt - 1);
                System.out.printf("[calculate] Checking tokens at indexes %d and %d: %s, %s\n", tempInt, tempInt + 1, current, next);
                if (isNumber(current) && isVar(next)) {
                    System.out.printf("[calculate] Detected number followed by variable at %d and %d, inserting '*'\n", tempInt, tempInt + 1);
                    tokens.add(tempInt + 1, "*");
                    tempInt++;
                    foundVar = true;
                    System.out.println("[calculate] Updated tokens: " + tokens);
                } else if (isVar(current) && isNumber(previous)) {
                    System.out.printf("[calculate] Detected number followed by variable at %d and %d, inserting '*'\n", tempInt - 1, tempInt);
                    tokens.add(tempInt, "*");
                    foundVar = true;
                    System.out.println("[calculate] Updated tokens: " + tokens);
                }
                tempInt += 2;
                if (!foundVar) System.out.println("[calculate] Couldn't find a variable with given indexes");
            }
            parse(tokens);
            return res;
        }
        private static boolean isNumber(Object num) {
            try {
                double check = Double.parseDouble(String.valueOf(num));
                if (logging) System.out.printf("[isNumber] %.1f is a number\n", check);
                return true;
            } catch (NumberFormatException e) {
                if (logging) System.out.printf("[isNumber] %s is not a number\n", num);
                return false;
            }
        }
        private static boolean isVar(Object var) {
            if (isNumber(var)) return false;
            char c = String.valueOf(var).charAt(0);
            boolean isVar = switch(c) {
                case '+', '-', '*', '/', '=' -> false;
                default -> true;
            };
            return isVar;
        }
        private static List<Object> whichHasVar(List<Object> lhs, List<Object> rhs) {
            for (Object o : lhs) if (isVar(o)) return lhs;
            for (Object o : rhs) if (isVar(o)) return rhs;
            return Arrays.asList();
        }
        private static double parseSide(List<Object> tokens) {return 0d;}
        private static void parse(List<Object> tokens) {
            int equals = tokens.indexOf("=");
            List<Object> lhs = tokens.subList(0, equals);
            List<Object> rhs = tokens.subList(equals + 1, tokens.size());
            if (whichHasVar(lhs, rhs).isEmpty()) {
                System.err.println("[parse] Syntax error: None of the sides contains a variable, aborting...");
                System.exit(64);
            }
            if (logging) System.out.printf("[parse] Created sublists: lhs = %s, rhs = %s\n", lhs, rhs);
            CompletableFuture<Double> lhsFuture = CompletableFuture.supplyAsync(() -> parseSide(lhs));
            CompletableFuture<Double> rhsFuture = CompletableFuture.supplyAsync(() -> parseSide(rhs));
            CompletableFuture<Void> both = CompletableFuture.allOf(lhsFuture, rhsFuture);
            both.join();
            double lhsRes = lhsFuture.join();
            double rhsRes = rhsFuture.join();
            if (logging) System.out.printf("[parse] Parsed LHS and  RHS: lhsRes = %.4f, rhsRes = %.4f\n", lhsRes, rhsRes);
            tokens.subList(0, equals).clear();
            tokens.add(0, lhsRes);
            equals = tokens.indexOf("=");            
            tokens.subList(equals + 1, tokens.size()).clear();
            tokens.add(rhsRes);
            if (logging) System.out.println("[parse] Updated tokens: " + tokens);
        }
    }
}
