/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EmptyStackException;
import practicum.exceptions.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import practicum.InputLine.Expression;
import practicum.DataPack.*;
import practicum.InputLine.*;

/**
 *
 * @author Admin
 */
public class Practicum {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s;
        String[] strings = null;
        while (true) {
            s = sc.nextLine();
            if(s.length() == 0){
                continue;
            }
            s = s.replace(" ", "");
            strings = s.split("=");
            if (strings.length == 1 && strings[0].contains(":")) {
                System.out.println("Ошибка");
                continue;
            }
            //на скобочки проверка
            try {
                if (strings.length > 1) {
                    ErrorChecking.errorCheck(strings[1]);
                }
            } catch (BracketsException | EmptyStackException exception) {
                System.out.println("Неправильная расстановка скобок");
                continue;
            }
            //проверка левой части на символы
            try {
                ErrorChecking.symbolsCheck(strings[0].substring(0, strings[0].length() - 1));
            } catch (UndefinedSymbol uf) {
                System.out.println("Некорректный ввод");
                continue;
            }
            //проверка правой части на символы
            try {
                if (strings.length > 1) {
                    ErrorChecking.symbolsCheck1(strings[1]);
                }
            } catch (UndefinedSymbol uf) {
                System.out.println("Некорректный ввод");
                continue;
            }
            //тут три вида d:=[1] and d:=a+b and d:=A[1] !!!! ewe nado d:=a completed
            if (strings[0].contains(":") && !strings[0].contains("[")) {
                int k = 0;
                for (int i = 0; i < strings[1].length(); i++) {
                    if (Character.isLetter(strings[1].charAt(i)) || strings[1].charAt(i) == '+' || strings[1].charAt(i) == '-' || strings[1].charAt(i) == '*') {
                        k++;
                    }
                }
                if (k == 0) {
                    //присвоение чистое
                    try{
                    if (ListOfVariables.exists(strings[0].substring(0,strings[0].length()-1)) == -1) {
                        //new variable
                        ListOfVariables.variables.add(new Variable(new Data(strings[1]), strings[0].substring(0, strings[0].length() - 1)));
                    } else {
                        // change exists
                        ListOfVariables.variables.get(ListOfVariables.exists(strings[0].substring(0, strings[0].length() - 1))).setData(new Data(strings[1]));
                    }
                    }
                    catch(Exception e){
                        System.out.println("Произошла ошибка");
                    }
                } else if (strings[1].contains("+") || strings[1].contains("-") || strings[1].contains("*")) {
                    //выражение
                    
                    String[] rstr = null;
                    rstr = strings[1].split("[\\p{Punct}&&[^\\[\\]\\(\\)]]");
                    for(int i = 0;i<rstr.length;i++){
                      //  if(rstr[i].contains("[")){
                          if(rstr[i].charAt(rstr[i].length()-1) != ')'){  
                          rstr[i] = rstr[i] + '&';  
                          }
                          else{
                              rstr[i] = rstr[i].substring(0, rstr[i].length()-1);
                              rstr[i] = rstr[i] + '&';  
                              rstr[i] = rstr[i] + ')';  
                          }
                        //}
                    }
                    int j = 0;
                    for(int i = 0;i<strings[1].length();i++){
                        
                        if(strings[1].charAt(i) == '+' ||strings[1].charAt(i) == '-' || strings[1].charAt(i) == '*'){
                            rstr[j] = rstr[j] + strings[1].charAt(i);
                            j++;
                        }
                    }
                    StringBuilder stringb = new StringBuilder();
                    for(int i = 0;i<rstr.length;i++){
                        stringb.append(rstr[i]);
                    }
                    String res = stringb.toString();
                    Expression exp = new Expression(res);
                    try {
                        if (ListOfVariables.exists(strings[0].substring(0, strings[0].length() - 1)) == -1) {
                            ListOfVariables.variables.add(new Variable(exp.execute(), strings[0].substring(0, strings[0].length() - 1)));
                        } else {
                            ListOfVariables.variables.get(ListOfVariables.exists(strings[0].substring(0, strings[0].length() - 1))).setData(exp.execute());
                        }
                    } catch (NullVariableException e) {
                        System.out.println("Ошибка в выражении");
                    }
                } else if (strings[1].contains("[")) {
                    //присвоение индексации
                    StringBuilder sb = new StringBuilder();
                    int q = 0;
                    while (Character.isLetter(strings[1].charAt(q)) && q < strings[1].length()) {
                            sb.append(strings[1].charAt(q));
                            q++;
                        }
                    if (ListOfVariables.exists(strings[0].substring(0, strings[0].length()-1)) == -1) {
                        //   ListOfVariables.add(new Variable(data, strings[0].substring(0, strings[0].length() - 1)));
                       // while (Character.isLetter(strings[1].charAt(q)) && q < strings[1].length()) {
                         //   sb.append(strings[1].charAt(q));
                        //    q++;
                      //  }
                        ListOfVariables.variables.add(new Variable(ListOfVariables.getVariable(sb.toString()).getData().getPartOfData(strings[1].substring(q, strings[1].length())), strings[0].substring(0, strings[0].length() - 1)));
                    } else {
                        ListOfVariables.variables.get(ListOfVariables.exists(strings[0].substring(0, strings[0].length() - 1))).setData(
                                ListOfVariables.getVariable(sb.toString()).getData().getPartOfData(strings[1].substring(q, strings[1].length()))
                        );
                    }
                } else //d:=a
                //strings[0] = name + ":"!!!!!!!!!
                 if (ListOfVariables.exists(strings[0].substring(0, strings[0].length())) == -1) {
                        ListOfVariables.variables.add(new Variable(new Data(
                                ListOfVariables.getVariable(strings[1]).getData().getRightPart()
                        ), strings[0].substring(0, strings[0].length())));
                    } else {
                        ListOfVariables.variables.get(ListOfVariables.exists(strings[0].substring(0, strings[0].length() - 1))).setData(
                                new Data(ListOfVariables.getVariable(strings[1]).getData().getRightPart()));
                    }
            } //тут два вида индексации a[1]:=[1] and a[1]:=b[1]; !!!!d[1] = d
            else if (strings[0].contains(":") && strings[0].contains("[")) {
                int k = 0;
                for (int i = 0; i < strings[1].length(); i++) {
                    if (Character.isLetter(strings[1].charAt(i))) {
                        k++;
                    }
                }
                if (k == 0) {
                    //тупо присвоение
                    String var = strings[0];
                    int curr = 0;
                    while (Character.isLetter(var.charAt(curr))) {
                        curr++;
                    }
                    //    ListOfVariables.getVariable(var.substring(0, curr)).getData().setPartOfData(new Data(strings[1]),var.substring(curr,var.length()-1));
                    ListOfVariables.getVariable(var.substring(0, curr)).setData(ListOfVariables.getVariable(var.substring(0, curr)).getData().setPartOfData(new Data(strings[1]), var.substring(curr, var.length() - 1)));
                } else //две индексации
                 if (!strings[1].contains("[")) {
                        //
                        //d[1]=double;
                        //доделать a[1] = a - не робит
                        String var = strings[0];
                        int curr = 0;
                        while (Character.isLetter(var.charAt(curr))) {
                            curr++;
                        }
                        if(var.substring(0, curr).equals(strings[1])){
                           ListOfVariables.getVariable(var.substring(0, curr)).setData(ListOfVariables.getVariable(var.substring(0, curr)).getData().setPartOfData(
                                new Data(ListOfVariables.getVariable(strings[1]).getData().getRightPart()),
                                var.substring(curr, var.length() - 1))); 
                        }
                        else{
                        ListOfVariables.getVariable(var.substring(0, curr)).setData(ListOfVariables.getVariable(var.substring(0, curr)).getData().setPartOfData(
                                ListOfVariables.getVariable(strings[1]).getData(),
                                var.substring(curr, var.length() - 1)));
                        }
                    } else {
                        //a[1]: = b[1];
                        String str = strings[0].substring(0,strings[0].length()-1);
                        int curr = 0;
                        while (Character.isLetter(str.charAt(curr))) {
                            curr++;
                        }
                        String rp = strings[1];
                        int curr2 = 0;
                        while (Character.isLetter(rp.charAt(curr2))) {
                            curr2++;
                        }
                        
                        ListOfVariables.getVariable(str.substring(0, curr)).setData(ListOfVariables.getVariable(str.substring(0, curr)).getData().setPartOfData(
                                ListOfVariables.getVariable(rp.substring(0, curr2)).getData().getPartOfData(rp.substring(curr2, rp.length())), 
                                str.substring(curr, str.length())));
                        
                    }
            } //вывод на экран
            else if (!strings[0].contains(":") && !strings[0].contains("[")) {
                try {
                    System.out.println(ListOfVariables.getVariable(strings[0]).getData().getRightPart());
                } catch (NullVariableException e) {
                    System.out.println("Не найдена переменная");
                }

            } //вывести часть
            else if (!strings[0].contains(":") && strings[0].contains("[")) {
                //System.out.print(ListOfVariables.getVariable(s));
                String var = strings[0];
                int curr = 0;
                while (Character.isLetter(var.charAt(curr))) {
                    curr++;
                }
                try{
                System.out.println(ListOfVariables.getVariable(var.substring(0, curr)).getData().getPartString(var.substring(curr, var.length())));
            }
                catch(Exception e){
                    System.out.println("Не найдена переменная");
                }
            }
        }
    }

}
