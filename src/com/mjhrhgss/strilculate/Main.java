package com.mjhrhgss.strilculate;

import java.util.*;

import static com.mjhrhgss.strilculate.Calculator.*;
import static com.mjhrhgss.strilculate.KillMyPC.*;

public class Main {
    public static boolean logging = true;
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("DISCLAIMER: Algebra is currently unavailable\nWould you like to enter an Arithemetic equation or an Algebraic equation? (Ar, Al, (h)elp, (e)xit)\n");
        String input = scanner.nextLine().toLowerCase();
        if (input.contains("ar")) arithmetic();
        else if (input.contains("al")) algebraic();
        else if (input.equals("h")) help();
        else if (input.equals("e")) {System.exit(0); scanner.close();}
        else {
            System.err.println("Invalid argument: " + input);
            System.exit(64);
            scanner.close();
        }
    }
    private static void arithmetic() {
        System.out.println("\nPlease enter the equation:\n");
        String input = scanner.nextLine();
        ArithmeticTokenizer AT = new ArithmeticTokenizer(input);
        List<Object> tokens = AT.scanTokens();
        System.out.println("\nWould you like this to be done in (n)ormal mode or (k)ill my PC mode?\n\nFor contrast, 1+1 in normal mode takes ~0.0000003 seconds to calculate, logging is not as intense as the one in KMPC mode and is irrelevant to speed.\nBut, it takes KMPC ~30 seconds to calculate it without logging, and ~6 years* with logging**\n");
        String mode = scanner.nextLine();
        if (mode.contains("*")) {
            System.out.println("\n* This is a theoretical calculation, I cannot text it becuase my PC started getting too hot way before it finished calculating.\n** The tests were done on a 13th Gen Intel(R) Core(TM) i7-13700F\n\nPlease choose a mode to use now ((n)ormal, (k)mpc)\n");
            mode = scanner.nextLine();
        }
        System.out.println("\nWould you like some logging? (y/n)   DEFAULT: n\n");
        String logs = scanner.nextLine();
        if (logs.toLowerCase().equals("y")) logging = true;
        else logging = false;
        System.out.println();
        if (mode.toLowerCase().contains("n")) System.out.printf("\n%s = %.6f\n\n", input, Arithmetic.calculate(tokens));
        else if (mode.toLowerCase().contains("k")) System.out.printf("\n%s = %.6f\n\n", input, ArithmeticKMPC.calculate(tokens));
        else {
            System.err.printf("Invalid argument for mode: \"%s\"\n", mode);
            System.exit(64);
        }
        String[] args = {};
        main(args);
    }
    private static void algebraic() {
        System.out.println("\nPlease enter the equation:");
        String input = scanner.nextLine();
        System.out.println();
        AlgebraicTokenzier AT = new AlgebraicTokenzier(input);
        List<Object> tokens = AT.scanTokens();
        System.out.println("\nWould you like logging? (y/n)  DEFAULT: y");
        String logs = scanner.nextLine();
        if (logs.contains("y")) logging = true;
        else logging = false;
        double x = Algebraic.calculate(tokens);
        System.out.printf("\nIn %s:\n\n", input);
        for (String var : AlgebraicTokenzier.vars) System.out.printf("%s = %f\n", var, x);
        System.out.println();
        String[] args = {};
        main(args);
    }
    private static void help() {
        List<String> ops = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "()", "^", "#", "!", "sin", "cos", "tan"));
        List<String> soon = new ArrayList<>(Arrays.asList("Algebra"));
        System.out.printf("\nValid operators: %s\nSoon to be added: %s\nSpaces are ignored\nFor more info, type 'h' again.\n\n", ops, soon);
        String input = scanner.nextLine();
        String[] args = {};
        if (input.equals("h")) moreHelp();
        else main(args);
    }
    private static void moreHelp() {
        System.out.println("\nOperator descriptions:\n'+' : Plus sign, add two numbers together\n'-' : Minus sign, negate a number or subtract a number from another\n'*' : Multiplication sign, multiply two numbers\n'/' : Division sign, divide a number by another\n'()' : Parentheses, operations in them take priority and are done before other operations, they can be nested\n'#' : Square root sign, get the number that, when squared, equals the inputted number\n'^' : Exponentation sign, multiply a number by itself the number of times specified in the exponent\n'!' : Factorial sign, multiply a number by a decreasing value until you reach 1, e.g. 7! = 7*6*5*4*3*2*1\n'sin' : Sine, gives the ratio of the length of the side opposite an angle to the hypotenuse in a right triangle; used to calculate the vertical position on a unit circle for a given angle in radians\n'cos' : Cosine, gives the ratio of the length of the side adjacent to an angle to the hypotenuse in a right triangle; used to calculate the horizontal position on a unit circle for a given angle in radians\n'tan' : Tangent, gives the ratio of the length of the side opposite an angle to the side adjacent to it in a right triangle; used to calculate the slope of the angle on the unit circle\n\nFor syntax, type 's'\n");
        String input = scanner.nextLine();
        String[] args = {};
        if (input.equals("s")) syntax();
        else main(args);
    }
    private static void syntax() {
        System.out.println("\nOperator syntaxes:\n'+' : a + b\n'-' : a - b\n'*' : a * b\n'/' : a / b\n'()' : It is treated as a value in itself, it can be used like any number but has values in it, e.g. (1+2)/3\n'#' : #x\n'^' : a^b\n'!' : x!\n'sin' : sin<x>\n'cos' : cos<x>\n'tan' : tan<x>\n\nNOTE: in 'sin', 'cos' and 'tan', you can exchange 'x' with any value, including one that has operators, e.g. sin<30 + 60>\n");
        String[] args = {};
        main(args);
    }
}
