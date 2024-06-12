package org.figrja.combo_auth_ahent.ely.by;


public class propery {
    private String name;
    private String value;
    private String signature;


    public String name() {
        return this.name;
    }


    public String value() {
        return this.value;
    }


    public String signature() {
        return this.signature;
    }


    public propery(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }
    public propery(){}
}
