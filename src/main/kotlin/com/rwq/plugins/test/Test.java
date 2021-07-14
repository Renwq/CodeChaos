package com.rwq.plugins.test;


/**
 * author： rwq
 * date: 2021/7/14 9:30
 * desc:
 **/
public class Test implements TestListener {
    private String R99luGlU35V;
    private String I1lalEsSOr0g6IwcW2i;


    public Test() {
    }

    public Test(String a, String b) {
        this.R99luGlU35V = a;
        this.I1lalEsSOr0g6IwcW2i = b;
    }

    public String getA() {
        return R99luGlU35V;
    }

    public void setA(String a) {
        this.R99luGlU35V = a;
    }

    public String getEmFGYndzt() {
        return I1lalEsSOr0g6IwcW2i;
    }

    public void setEmFGYndzt(String emFGYndzt) {
        this.I1lalEsSOr0g6IwcW2i = emFGYndzt;
    }

    public void printA(String a){
        boolean bo = false;
        String localVar = new String("ing");
        String var = "";
        System.out.println(localVar);
        System.out.println(var);
        System.out.println(a);
    }

    public void printB(String b){
        System.out.println(b);
    }


    public void print() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(R99luGlU35V);
        buffer.append(I1lalEsSOr0g6IwcW2i);
        System.out.println(buffer.toString());

    }


    public static void staticMeth(String b){
        StringBuilder buffer = new StringBuilder();
        System.out.println(buffer);
    }

}
