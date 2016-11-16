/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicum;

import java.util.EmptyStackException;
import java.util.Stack;
import practicum.exceptions.*;

/**
 *
 * @author Admin
 */
public class ErrorChecking {

    public static void errorCheck(String s) {
        Stack<Character> st = new Stack<Character>();
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '[') {
                    st.add(s.charAt(i));
                }
                if (s.charAt(i) == ']') {
                    if (st.pop() != '[') {
                      throw new BracketsException();
                    }
                }
            }
            if(st.isEmpty() == false){
                throw new BracketsException();
            }
    }
    public static void symbolsCheck(String s){
        String s1 = s;
        s1 = s1.replace("[", "").replace("]","").replace("(", "").replace(")","");
        for(int i = 0;i<s1.length();i++){
            if(!Character.isLetter(s1.charAt(i)) && !Character.isDigit(s1.charAt(i))){
                throw new UndefinedSymbol();
            }
        }
    }
    public static void symbolsCheck1(String s){
        String s1 = s;
        s1 = s1.replace("_","").replace("+","").replace("-","").replace("*","").replace("[","").replace("]","").replace(":","").replace("=","").replace("(", "").replace(")","");
        for(int i = 0;i<s1.length();i++){
            if(!Character.isLetter(s1.charAt(i)) && !Character.isDigit(s1.charAt(i))){
                throw new UndefinedSymbol();
            }
        }
    }
}
