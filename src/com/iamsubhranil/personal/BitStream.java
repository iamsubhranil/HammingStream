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
        ArrayList<Bit> backingList = new ArrayList<>(8);
        while (counter < 8) {
            backingList.add(new Bit(i % 2));
            i = (i - (i % 2)) / 2;
            counter++;
        }
        int size = backingList.size() - 1;
        while (size >= 0) {
            add(backingList.get(size));
            size--;
        }
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<>(size() / 8);
        int[] counter = {7};
        int[] presentByte = {0};
        forEach(bit -> {
            presentByte[0] = presentByte[0] + (bit.getValue() * ((int) Math.pow(2, counter[0])));
            counter[0]--;
            if (counter[0] == 0) {
                bytes.add(presentByte[0]);
                counter[0] = 0;
                presentByte[0] = 0;
            }
        });
        return bytes;
    }

}
