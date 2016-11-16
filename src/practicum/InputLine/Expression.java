/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicum.InputLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.*;
import practicum.DataPack.*;
//import practicum.InputLine.*;

/**
 *
 * @author Admin
 */
public class Expression {

    private String expr;
    private static final int LEFT_ASSOC = 0;
    private static final int RIGHT_ASSOC = 1;
    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();

    static {
        // Map<"token", []{precendence, associativity}>
        OPERATORS.put("+", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("-", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("*", new int[]{5, LEFT_ASSOC});
        OPERATORS.put("/", new int[]{5, LEFT_ASSOC});
        OPERATORS.put("%", new int[]{5, LEFT_ASSOC});
        OPERATORS.put("^", new int[]{10, RIGHT_ASSOC});
    }

    public Expression(String s) {
        this.expr = s;
    }

    private static boolean isOperator(String token) {
        return OPERATORS.containsKey(token);
    }

    private static boolean isAssociative(String token, int type) {
        if (!isOperator(token)) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
        if (OPERATORS.get(token)[1] == type) {
            return true;
        }
        return false;
    }

    private static final int cmpPrecedence(String token1, String token2) {
        if (!isOperator(token1) || !isOperator(token2)) {
            throw new IllegalArgumentException("Invalied tokens: " + token1
                    + " " + token2);
        }
        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
    }

    private static String[] infixToRPN(String[] inputTokens) {
        ArrayList<String> out = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();
        // For all the input tokens [S1] read the next token [S2]
        for (String token : inputTokens) {
            if (isOperator(token)) {
                // If token is an operator (x) [S3]
                while (!stack.empty() && isOperator(stack.peek())) {
                    // [S4]
                    if ((isAssociative(token, LEFT_ASSOC) && cmpPrecedence(
                            token, stack.peek()) <= 0)
                            || (isAssociative(token, RIGHT_ASSOC) && cmpPrecedence(
                            token, stack.peek()) < 0)) {
                        out.add(stack.pop()); 	// [S5] [S6]
                        continue;
                    }
                    break;
                }
                // Push the new operator on the stack [S7]
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token); 	// [S8]
            } else if (token.equals(")")) {
                // [S9]
                while (!stack.empty() && !stack.peek().equals("(")) {
                    out.add(stack.pop()); // [S10]
                }
                stack.pop(); // [S11]
            } else {
                out.add(token); // [S12]
            }
        }
        while (!stack.empty()) {
            out.add(stack.pop()); // [S13]
        }
        String[] output = new String[out.size()];
        return out.toArray(output);
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String s) {
        this.expr = s;
    }

    private String toPoliz() {
        String[] output = infixToRPN(expr.split(""));
        String result = "";
        for (int i = 0; i < output.length; i++) {
            result = result + output[i];
        }
        return result;
    }

    public Data execute() {
        //s = toPoliz();
        String s = this.toPoliz();
        int i = 0;
        Stack<Data> operands = new Stack<>();
        while (i < s.length()) {
            if (Character.isLetter(s.charAt(i))) {
                operands.add(ListOfVariables.getVariable(Character.toString(s.charAt(i))).getData());
                i++;
                continue;
            }
            if (s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == '*') {
                Data d1 = operands.pop();
                Data d2 = operands.pop();
                switch (String.valueOf(s.charAt(i))) {
                    case "+":
                        operands.push(DataOperations.plusData(d1, d2));
                        break;
                    case "-":
                        operands.push(DataOperations.minusData(d1, d2));
                        break;
                    case "*":
                        operands.push(DataOperations.multData(d1, d2));
                        break;
                }
                i++;
            }
        }
        return operands.pop();
    }

}
