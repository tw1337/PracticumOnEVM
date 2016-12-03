/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicum.DataPack;

import java.util.ArrayList;
import practicum.DataPack.Data;
import practicum.DataPack.Data.Node;

/**
 *
 * @author Admin
 */
public class DataOperations {

    public static Data plusData(Data a, Data b) {
        Data temp = a.clone();
        temp.plus(b);
        return temp;
    }

    public static Data minusData(Data a, Data b) {
        Data temp = a.clone();
        temp.minus(b);
        return temp;
    }

    public static Data multData(Data a, Data b) {
        Data temp = a.clone();
        Data temp1 = b.clone();
        Data retData = Data.testit(temp, temp1);
        return retData;
    }
}
