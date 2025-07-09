package com.mjhrhgss.strilculate;

import java.util.*;

import static com.mjhrhgss.strilculate.Main.logging;

class AlgebraicTokenzier {
    private final String equation;
    private int start = 0, current = 0;
    private final List<Object> tokens = new ArrayList<>();
    static final List<String> vars = new ArrayList<>();
    AlgebraicTokenzier(String equation) {this.equation = equation;}
    List<Object> scanTokens() {
        if (logging) System.out.printf("[scanTokens] Starting scan of equation: \"%s\"\n", equation);
        while (current < equation.length()) {
            start = current;
            scanToken();
        }
        if (logging) System.out.println("[scanTokens] Finished scanning tokens: " + tokens);
        return tokens;
    }
    private void scanToken() {
        char c = equation.charAt(current++);
        if (logging) System.out.printf("[scanToken] Scanning character: '%c'\n", c);
        switch (c) {
            case '+' -> addToken("+");
            case '-' -> addToken("-");
            case '*' -> addToken("*");
            case '/' -> addToken("/");
            case '=' -> addToken("=");
            case '!' -> addToken("!");
            case ' ' -> {return;}
            default -> {
                if (c >= '0' && c <= '9') number();
                else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) variable();
                else {
                    System.err.printf("Unknown token: \"%c\"\n", c);
                    System.exit(64);
                }
            }
        }
    }
    private void variable() {
        while ((current < equation.length()) && ((equation.charAt(current) >= 'a' && equation.charAt(current) <= 'z') || (equation.charAt(current) >= 'A' && equation.charAt(current) <= 'Z'))) current++;
        String varToken = equation.substring(start, current);
        if (logging) System.out.printf("[variable] Parsed variable: \"%s\"\n", varToken);
        addToken(varToken);
        vars.add(varToken);
    }
    private void number() {
        while (current < equation.length() && equation.charAt(current) >= '0' && equation.charAt(current) <= '9') current++;
        if (current < equation.length() - 1 && equation.charAt(current) == '.' && equation.charAt(current + 1) >= '0' && equation.charAt(current + 1) <= '9') {
            do current++;
            while (current < equation.length() && equation.charAt(current) >= '0' && equation.charAt(current) <= '9');
        }
        double numberToken = Double.parseDouble(equation.substring(start, current));
        if (logging) System.out.printf("[number] Parsed number: \"%f\"\n", numberToken);
        addToken(numberToken);
    }
    private void addToken(Object token) {
        tokens.add(token);
        if (logging) System.out.printf("[Tokenizer] Added token: %s\n", token);
    }
}
