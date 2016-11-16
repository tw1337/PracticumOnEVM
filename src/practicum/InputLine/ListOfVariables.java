/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicum.InputLine;

import java.util.ArrayList;
import practicum.exceptions.NullVariableException;

/**
 *
 * @author Admin
 */
public class ListOfVariables {

    public static ArrayList<Variable> variables = new ArrayList<Variable>();

    public static int exists(String name) {
        for (int i = 0; i < variables.size(); i++) {
            if(variables.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
    public static Variable getVariable (String name){
         for (int i = 0; i < variables.size(); i++) {
            if(variables.get(i).getName().equals(name)){
                return variables.get(i);
            }
        }
       throw new NullVariableException();
    }
}
