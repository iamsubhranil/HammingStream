package com.iamsubhranil.personal;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Author : Nil
 * Date : 10/27/2016 at 2:39 PM.
 * Project : HammingStream
 */
public class BitStream extends ArrayList<Bit> {

    public BitStream() {
        super();
    }

    public BitStream(Collection<? extends Bit> c) {
        super(c);
    }

    public BitStream(int initialCapacity) {
        super(initialCapacity);
    }


    public void addByte(Byte b) {
        while (b != 0) {
            add(new Bit(b & 1));
            b = Byte.valueOf((b >> 1) + "");
        }
    }

    public void addInt(int i) {
        addIntInBits(i, 8);
    }

    private void ensureMinimumExtraCapacity(int extraCapacity) {
        ensureCapacity(size() + extraCapacity);
        while (extraCapacity > 0) {
            add(null);
            extraCapacity--;
        }
    }

    private void addIntInBits(int i, int bitLimit) {
        int counter = bitLimit;
        int prevSize = size() - 1;
        ensureMinimumExtraCapacity(bitLimit);
        while (counter > 0) {
            set(prevSize + counter, new Bit(i % 2));
            i = (i - (i % 2)) / 2;
            counter--;
        }
    }

    public void printStream() {
        forEach(bit -> {
            if (bit == null) {
                System.out.print(" null ");
            } else {
                System.out.printf("%s", bit.getValue());
            }
        });
        System.out.print("\n");
    }

    public ArrayList<Integer> toBytes() {
        ArrayList<Integer> bytes = new ArrayList<>(size() / 8);
        int[] counter = {0};
        int[] pow = {7};
        int[] presentByte = {0};
        forEach(bit -> {
            presentByte[0] = presentByte[0] + (bit.getValue() * (1 << pow[0]));
            counter[0]++;
            pow[0]--;
            if (((counter[0]) % 8) == 0) {
                bytes.add(presentByte[0]);
                presentByte[0] = 0;
                pow[0] = 7;
            }
        });
        return bytes;
    }

}
