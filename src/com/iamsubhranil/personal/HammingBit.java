package com.iamsubhranil.personal;

/**
 * Author : Nil
 * Date : 10/27/2016 at 2:20 PM.
 * Project : HammingStream
 */
public class HammingBit extends Bit {

    private int position = 1;

    public HammingBit(int position) {
        this.position = position;
    }

    public Bit decideValueExcludeSelf(BitStream bitmap) {
        return decideValueFromPosition(position + 1, bitmap);
    }

    public Bit decideAndSetValueExcludeSelf(BitStream bitmap) {
        Bit resultBit = decideValueExcludeSelf(bitmap);
        setValue(resultBit);
        return resultBit;
    }

    private Bit decideValueFromPosition(int start, BitStream bitmap) {
        int max = bitmap.size();
        Bit previousBit = bitmap.get(start - 1);
        Bit resultBit = new Bit();
        start++;
        while (start < max) {
            if ((start & position) == position) {
                Bit nextBit = bitmap.get((start - 1));
                resultBit = nextBit.XOR(resultBit);
            }
            start++;
        }
        resultBit = resultBit.XOR(previousBit);
        return resultBit;
    }

    public Bit decideValueIncludeSelf(BitStream bitmap) {
        return decideValueFromPosition(position, bitmap);
    }

}
