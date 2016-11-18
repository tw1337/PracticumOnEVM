/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicum;

import java.util.EmptyStackException;
import practicum.exceptions.*;
import java.util.Scanner;
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
            //тут три вида d:=[1] and d:=a+b and d:=A[1] !!!! ewe nado d:=a
            if (strings[0].contains(":") && !strings[0].contains("[")) {
                int k = 0;
                for (int i = 0; i < strings[1].length(); i++) {
                    if (Character.isLetter(strings[1].charAt(i))) {
                        k++;
                    }
                }
                if (k == 0) {
                    //присвоение чистое
                    if (ListOfVariables.exists(strings[0]) == -1) {
                        //new variable
                        ListOfVariables.variables.add(new Variable(new Data(strings[1]), strings[0].substring(0, strings[0].length() - 1)));
                    } else {
                        // change exists
                        ListOfVariables.variables.get(ListOfVariables.exists(strings[0].substring(0, strings[0].length() - 1))).setData(new Data(strings[1]));
                    }
                } else if (strings[1].contains("+") || strings[1].contains("-") || strings[1].contains("*")) {
                    //выражение
                    Expression exp = new Expression(strings[1]);
                    try {
                        if (ListOfVariables.exists(strings[0]) == -1) {
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
                    if (ListOfVariables.exists(strings[0].substring(0,strings[0].length())) == -1) {
                     //   ListOfVariables.add(new Variable(data, strings[0].substring(0, strings[0].length() - 1)));
                     while(Character.isLetter(strings[1].charAt(q)) && q < strings[1].length()){
                         sb.append(strings[1].charAt(q));
                         q++;
                     }
                     ListOfVariables.variables.add(new Variable(ListOfVariables.getVariable(sb.toString()).getData().getPartOfData(strings[1].substring(q,strings[1].length()))
                             , strings[0].substring(0, strings[0].length() - 1)));
                    }
                    else{
                        ListOfVariables.variables.get(ListOfVariables.exists(strings[0].substring(0, strings[0].length() - 1))).setData(
                          ListOfVariables.getVariable(sb.toString()).getData().getPartOfData(strings[1].substring(q,strings[1].length()))      
                        );
                    }
                } else {
                    //d:=a
                    //strings[0] = name + ":"!!!!!!!!!
                    if(ListOfVariables.exists(strings[0].substring(0,strings[0].length())) == -1){
                        ListOfVariables.variables.add(new Variable(new Data (
                        ListOfVariables.getVariable(strings[1]).getData().getRightPart()
                        ),strings[0].substring(0,strings[0].length())));
                    }
                    else{
                        
                    }
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
                } else {
                    //две индексации
                    if(!strings[1].contains("[")){
                        //
                        //d[1]=double;
                    }
                    else{
                        //a[1] = b[1];
                    }
                }
            } //вывод на экран
            else if (!strings[0].contains(":") && !strings[0].contains("[")) {
                try {
                    System.out.print(ListOfVariables.getVariable(strings[0]).getData().getRightPart());
                } catch (NullVariableException e) {
                    System.out.println("Не найдена переменная");
                }

            } //вывести часть
            else if (!strings[0].contains(":") && strings[0].contains("[")) {

            }
        }
    }

}
