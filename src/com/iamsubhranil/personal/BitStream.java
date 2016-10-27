package com.iamsubhranil.personal;

import java.util.ArrayList;

/**
 * Author : Nil
 * Date : 10/27/2016 at 2:39 PM.
 * Project : HammingStream
 */
public class BitStream extends ArrayList<Bit> {

    public void addByte(Byte b) {
        while (b != 0) {
            add(new Bit(b & 1));
            b = Byte.valueOf((b >> 1) + "");
        }
    }

    public void addInt(int i) {
        int counter = 0;
        while (i > 0 || counter < 8) {
            add(new Bit(i & 1));
            i = i >> 1;
            counter++;
        }
    }

}
