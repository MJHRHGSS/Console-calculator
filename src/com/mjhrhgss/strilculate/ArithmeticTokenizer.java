package com.mjhrhgss.strilculate;

import java.util.*;

class ArithmeticTokenizer {
    private final String equation;
    private int current = 0, start = 0;
    private final List<Object> tokens = new ArrayList<>();
    ArithmeticTokenizer(String eq) {
        this.equation = eq;
    }
    List<Object> scanTokens() {
        while (current < equation.length()) {
            start = current;
            scanToken();
        }
        return tokens;
    }
    private void scanToken() {
        char c = equation.charAt(current++);        
        switch (c) {
            case '(' -> addToken("(");
            case ')' -> addToken(")");
            case '>' -> addToken(">");
            case '+' -> addToken("+");
            case '-' -> addToken("-");
            case '*' -> addToken("*");
            case '/' -> addToken("/");
            case '^' -> addToken("^");
            case '!' -> addToken("!");
            case '#' -> addToken("#");
            case ' ' -> {return;}
            default -> {
                if (c >= '0' && c <= '9') number();
                else if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) identifier();
                else {
                    System.err.printf("Unknown token: \"%c\"", c);
                    System.exit(64);
                }
            }
        }
    }
    private void identifier() {
        while (current < equation.length() && ((equation.charAt(current) >= 'a' && equation.charAt(current) <= 'z') || (equation.charAt(current) >= 'A' && equation.charAt(current) <= 'Z'))) current++;
        if (current < equation.length() - 1 && ((equation.charAt(current + 1) >= 'a' && equation.charAt(current + 1) <= 'z') || (equation.charAt(current + 1) >= 'A' && equation.charAt(current + 1) <= 'Z'))) {
            do current++;
            while (current < equation.length() && ((equation.charAt(current) >= 'a' && equation.charAt(current) <= 'z') || (equation.charAt(current) >= 'A' && equation.charAt(current) <= 'Z')));
        }
        if (current < equation.length() && equation.charAt(current) == '<') current++;
        String identifierToken = equation.substring(start, current);
        addToken(identifierToken);
    }
    private void number() {
        while (current < equation.length() && equation.charAt(current) >= '0' && equation.charAt(current) <= '9') current++;
        if (current < equation.length() - 1 && equation.charAt(current) == '.' && equation.charAt(current + 1) >= '0' && equation.charAt(current + 1) <= '9') {
            do current++;
            while (current < equation.length() && equation.charAt(current) >= '0' && equation.charAt(current) <= '9');
        }
        double numberToken = Double.parseDouble(equation.substring(start, current));
        addToken(numberToken);
    }
    private void addToken(Object token) {
        tokens.add(token);
    }
}
