/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicum.InputLine;

import practicum.DataPack.Data;

/**
 *
 * @author Admin
 */
public class Variable {

    private String name;
    private Data data;

    public Variable(Data data, String name) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data d) {
        this.data = d;
    }
}
