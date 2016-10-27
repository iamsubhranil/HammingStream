package com.iamsubhranil.personal;

import java.util.ArrayList;

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

    public Bit decideAndSetValueExcludeSelf(ArrayList<Bit> bitmap) {
        Bit resultBit = decideValueFromPosition(position * 2, bitmap);
        setValue(resultBit);
        return resultBit;
    }

    private Bit decideValueFromPosition(int start, ArrayList<Bit> bitmap) {
        int max = bitmap.size();
        Bit previousBit = bitmap.get(start);
        Bit resultBit = new Bit();
        start++;
        while (start < max) {
            if ((start & position) == position) {
                Bit nextBit = bitmap.get(start);
                resultBit = nextBit.XOR(previousBit);
                previousBit = nextBit;
            }
            start++;
        }
        return resultBit;
    }

    public Bit decideValueIncludeSelf(ArrayList<Bit> bitmap) {
        return decideValueFromPosition(position, bitmap);
    }

}