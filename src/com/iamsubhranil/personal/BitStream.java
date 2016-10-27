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
        addIntInBits(i, 8);
    }

    private void addIntInBits(int i, int bitlimit) {
        int counter = 0;
        ArrayList<Bit> backingList = new ArrayList<>(8);
        while (counter < bitlimit) {
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
        //       int bitlimit = size()%8;
        //       addIntInBits(8-bitlimit,bitlimit);
        ArrayList<Integer> bytes = new ArrayList<>(size() / 8);
        int[] counter = {0};
        int[] pow = {7};
        int[] presentByte = {0};
        forEach(bit -> {
            presentByte[0] = presentByte[0] + (bit.getValue() * (1 << pow[0]));
            counter[0]++;
            pow[0]--;
            if (((counter[0]) % 8) == 0 || counter[0] == size() - 1) {
                bytes.add(presentByte[0]);
                presentByte[0] = 0;
                pow[0] = 7;
            }
        });
        return bytes;
    }

}
