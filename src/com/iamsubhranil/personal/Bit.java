package com.iamsubhranil.personal;

/**
 * Author : Nil
 * Date : 10/27/2016 at 8:48 AM.
 * Project : HammingStream
 */
public class Bit {

    private boolean value = false;

    public Bit() {
        value = false;
    }

    public Bit(int val){
        value = (val==1);
    }

    public Bit(boolean val){
        value = val;
    }

    public Bit complement(){
        return new Bit(!value);
    }

    public int getValue(){
        return value?1:0;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setValue(Bit copyBit) {
        setValue(copyBit.getBoolean());
    }

    public void setValue(int value) {
        this.value = (value == 1);
    }

    public boolean getBoolean(){
        return value;
    }

    public Bit XOR(Bit anotherBit){
        return new Bit(value^anotherBit.getBoolean());
    }

    public Bit OR(Bit anotherBit){
        return new Bit(value||anotherBit.getBoolean());
    }

    public Bit AND(Bit anotherBit){
        return new Bit(value&&anotherBit.getBoolean());
    }

    public Bit NOT(){
        return complement();
    }

    public Bit XNOR(Bit anotherBit){
        return XOR(anotherBit).complement();
    }

    public String toString() {
        return getValue() + "";
    }

}
