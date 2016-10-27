package com.iamsubhranil.personal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author : Nil
 * Date : 10/27/2016 at 8:29 AM.
 * Project : HammingStream
 */
public class Converter {

    public static void main(String[] args) {
        writeTest();
        readTest();
    }

    private static void readTest() {
        try {
            FileInputStream fileInputStream = new FileInputStream("test_tobin.txt");
            BitStream bitStream = new BitStream();
            int i = 0;
            System.out.println("Reading from test_tobin.txt");
            while ((i = fileInputStream.read()) != -1) {
                bitStream.addInt(i);
            }
            fileInputStream.close();
            checkAndCorrectBitmap(bitStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void writeTest() {
        try {
            BitStream bits = new BitStream();
            FileInputStream fileInputStream = new FileInputStream("test.txt");
            int i;
            System.out.println("Reading file..");
            int bitsize = 0;
//            ArrayList<Integer> backingList = new ArrayList<>();
            while ((i = fileInputStream.read()) != -1) {
                bits.addInt(i);
                //               backingList.add(i);
                bitsize++;
            }
            System.out.println("Reading completed..");
            fileInputStream.close();
            int[] counter = {0};
            System.out.println("Size of bytes : " + bits.toBytes().size());
            System.out.println("Adding hamming bits..");
            addHammingBits(bits);
            //    bits.set(7, bits.get(7).complement());
            System.out.println("Size of bits\nActual : " + bitsize * 8 + "\tRecorded : " + bits.size());
            System.out.println("Dumping to test_tobin.txt..");
            FileOutputStream fileOutputStream = new FileOutputStream("test_tobin.txt");
            bits.toBytes().forEach(in -> {
                //   if (in != backingList.get(counter[0])) {
                //        System.err.println("Value mismatch at position " + counter[0] + "..\nActual value : " + backingList.get(counter[0]) + "\nSaved value : " + in);
                //    }
                try {
                    fileOutputStream.write(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter[0]++;
            });
            fileOutputStream.close();
            System.out.println("Wrote : " + counter[0] + " bytes");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkAndCorrectBitmap(BitStream bitStream) {
        int position = 1;
        System.out.println("Checking error..\n");
        int prevsize = bitStream.size();
        BitStream errorAtPosition = new BitStream();
        while (position < prevsize) {
            HammingBit hammingBit = new HammingBit(position);
            hammingBit.setValue(bitStream.get(position));
            Bit errorCode = hammingBit.decideValueIncludeSelf(bitStream);
            errorAtPosition.add(errorCode);
            position = position * 2;
        }
        errorAtPosition.forEach(bit -> System.out.printf("%d", bit.getValue()));
        int maxpower = errorAtPosition.size() - 1;
        int errint = 0;
        while (maxpower >= 0) {
            errint = errint + (errorAtPosition.get(maxpower).getValue() * (1 << maxpower));
            maxpower--;
        }
        System.out.println("\nError at position : " + errint);
    }

    private static void addHammingBits(BitStream bitStream) {
        int position = 1;
        int prevsize = bitStream.size();
        while (position < prevsize) {
            HammingBit hammingBit = new HammingBit(position);
            System.out.printf("%d", hammingBit.decideAndSetValueExcludeSelf(bitStream).getValue());
            bitStream.add(position - 1, hammingBit);
            position = position * 2;
        }
        System.out.println("\nHamming bits added : " + (bitStream.size() - prevsize));
    }

}
