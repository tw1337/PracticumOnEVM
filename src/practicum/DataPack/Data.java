/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicum.DataPack;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class Data {

    public class Node {

        ArrayList<Node> childs;
        Node prev;
        int value;

        @Override
        public Object clone() {
            Node result = null;
            result = new Node();
            result.childs = new ArrayList<>();
            for (int i = 0; i < this.childs.size(); i++) {
                result.childs = (ArrayList<Node>) this.childs.get(i).clone();
            }
            return result;
        }
    }
    Node head;
    StringBuilder sb = new StringBuilder();
    boolean bb = false;

    public Data(String s) {
        head = new Node();
        Node temp = head;
        String help = "";
        boolean flag = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '[') {
                if (temp.childs == null) {
                    temp.childs = new ArrayList<>();
                }
                Node child = new Node();
                temp.childs.add(child);
                temp.childs.get(temp.childs.size() - 1).prev = temp;
                temp = temp.childs.get(temp.childs.size() - 1);
            }
            if (s.charAt(i) == '-') {
                flag = true;
            }
            if (Character.isDigit(s.charAt(i))) {
                while (Character.isDigit(s.charAt(i))) {
                    help = help + Character.toString(s.charAt(i));
                    i++;
                }
                temp.value = Integer.parseInt(help);
                if (flag) {
                    temp.value = -temp.value;
                    flag = false;
                }
                help = "";
            }
            if (s.charAt(i) == ']') {
                temp = temp.prev;
            }
        }
    }

    private void getRP(Node N) {
        if (N.childs == null) {
            sb.append(Integer.toString(N.value));
            sb.append("]");
            return;
        }
        for (int i = 0; i < N.childs.size(); i++) {
            sb.append("[");
            getRP(N.childs.get(i));
        }
        sb.append("]");
    }

    public String getRightPart() {
        Node temp = head;
        getRP(temp);
        sb.deleteCharAt(sb.length() - 1);
        String s = sb.toString();
        sb.delete(0, sb.length());
        return s;
    }

    public boolean dimensions(Data d) {
        Node a = this.head;
        Node b = d.head;
        boolean f = dimens(a, b);
        return f;

    }

    private boolean dimens(Node a, Node b) {
        try {
            if (a.childs == null) {
                bb = true;
                return bb;
            }
            for (int i = 0; i < a.childs.size(); i++) {
                dimens(a.childs.get(i), b.childs.get(i));
            }
        } catch (Exception e) {
            bb = false;
        }
        return bb;
    }

    @Override
    public Data clone() {
        Data temp = new Data(this.getRightPart());
        return temp;
    }

    public void plus(Data b) {
        Node a = this.head;
        Node bNode = b.head;
        plusplus(a, bNode);
    }

    private void plusplus(Node a, Node b) {
        if (a.childs == null) {
            a.value = a.value + b.value;
            return;
        }
        for (int i = 0; i < a.childs.size(); i++) {
            plusplus(a.childs.get(i), b.childs.get(i));
        }
    }

    public void minus(Data b) {
        Node a = this.head;
        Node bNode = b.head;
        minusminus(a, bNode);
    }

    private void minusminus(Node a, Node b) {
        if (a.childs == null) {
            a.value = a.value - b.value;
            return;
        }
        for (int i = 0; i < a.childs.size(); i++) {
            plusplus(a.childs.get(i), b.childs.get(i));
        }
    }

    public static ArrayList<Node> getLeafs(Node a, ArrayList<Node> arr) {
        if (a.childs == null) {
            arr.add(a);
            return arr;
        }
        for (int i = 0; i < a.childs.size(); i++) {
            getLeafs(a.childs.get(i), arr);
        }
        return arr;
    }

    private static void multleafs(int m, Node N) {
        if (N.childs == null) {
            N.value = N.value * m;
            return;
        }
        for (int i = 0; i < N.childs.size(); i++) {
            multleafs(m, N.childs.get(i));
        }
    }

    public static Data testit(Data a, Data b) {
        Data d = b.clone();
        Data d1 = d.clone();
        Node N = a.head;
        Node temp = b.head;
        ArrayList<Node> arr = new ArrayList<>();
        ArrayList<Node> arrhelp = new ArrayList<>();
        arr = getLeafs(N, arr);
        for (int i = 0; i < arr.size(); i++) {
            arrhelp.add(arr.get(i));
        }
        for (int i = 0; i < arr.size(); i++) {
            int k = arrhelp.get(i).value;
            arr.remove(i);
            temp = d.head;
            d = d1.clone();
            arr.add(i, temp);
            multleafs(k, arr.get(i));
        }
        setChilds(N, arr);
        StringBuilder sb = new StringBuilder();
        getRP2(N, sb);
        String s = sb.toString();
        sb.delete(0, sb.length());
        return new Data(s);
    }

    private static void setChilds(Node N, ArrayList<Node> arr) {
        if (N.childs == null) {
            N.childs = arr.get(0).childs;
            arr.remove(0);
            return;
        }
        for (int i = 0; i < N.childs.size(); i++) {
            setChilds(N.childs.get(i), arr);
        }
    }

    private static StringBuilder getRP2(Node N, StringBuilder sb) {
        if (N.childs == null) {
            sb.append(Integer.toString(N.value));
            sb.append("]");
            return sb;
        }
        for (int i = 0; i < N.childs.size(); i++) {
            sb.append("[");
            getRP2(N.childs.get(i), sb);
        }
        sb.append("]");
        return sb;
    }

    public Data getPartOfData(String s) {
       return null;
    }

}
