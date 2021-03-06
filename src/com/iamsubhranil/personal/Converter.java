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

    private static final String FILE_NAME = "test";

    public static void main(String[] args) {
        writeTest();
        readTest();
    }

    private static void readTest() {
        try {
            FileInputStream fileInputStream = new FileInputStream(FILE_NAME + "_tobin.txt");
            BitStream bitStream = new BitStream();
            int i = 0;
            System.out.println("Reading from " + FILE_NAME + "_tobin.txt");
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
            FileInputStream fileInputStream = new FileInputStream(FILE_NAME + ".txt");
            int i;
            System.out.println("Reading original file..");
            int bitsize = 0;
            while ((i = fileInputStream.read()) != -1) {
                bits.addInt(i);
                bitsize++;
            }
            System.out.println("Reading completed..");
            fileInputStream.close();
            int[] counter = {0};
            System.out.println("Size of bytes : " + bits.toBytes().size());
            System.out.println("Adding hamming bits..");
            addHammingBits(bits);
            //        bits.set(237, bits.get(237).complement());
            System.out.println("Size of bits\nActual : " + bitsize * 8 + "\tRecorded : " + bits.size());
            System.out.println("Dumping to " + FILE_NAME + "_tobin.txt..");
            FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME + "_tobin.txt");
            bits.toBytes().forEach(in -> {
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
        int count = 0;
        System.out.println("Checking error..");
        int streamSize = bitStream.size();
        BitStream errorAtPosition = new BitStream();
        while (position < streamSize) {
            HammingBit hammingBit = new HammingBit(position);
            hammingBit.setValue(bitStream.get(position - 1));
            Bit errorCode = hammingBit.decideValueIncludeSelf(bitStream);
            errorAtPosition.add(errorCode);
            position = position * 2;
            count++;
        }
        System.out.println("Found " + count + " hamming bits..");
        int maxpower = errorAtPosition.size() - 1;
        int errint = 0;
        System.out.println("Generating error position..");
        while (maxpower >= 0) {
            errint = errint + (errorAtPosition.get(maxpower).getValue() * (1 << maxpower));
            maxpower--;
        }
        if (errint != 0) {
            System.out.println("Error at position : " + errint);
            System.out.println("Correcting error..");
            bitStream.set(errint - 1, bitStream.get(errint - 1).complement());
            System.out.println("Bitmap rectified..");
        } else {
            System.out.println("File is ok..");
        }
        position = 1 << (count - 1);
        System.out.println("Size before removing hamming bits : " + bitStream.size());
        System.out.println("Removing hamming bits..");
        while (position > 0) {
            bitStream.remove(position - 1);
            position = position / 2;
        }
        System.out.println("Size after removing hamming bits : " + bitStream.size());
        if ((count) % 8 != 0) {
            int extraBits = 8 - (count % 8);
            System.out.println("Size before removing the extra bits : " + bitStream.size());
            System.out.println("Removing " + extraBits + " extra bits..");
            while (extraBits > 0) {
                bitStream.remove(bitStream.size() - 1);
                extraBits--;
            }
            System.out.println("Size after removing extra bits : " + bitStream.size());
        }
        System.out.println("Dumping final bitmap..");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME + "_frombin.txt");
            bitStream.toBytes().forEach(in -> {
                try {
                    fileOutputStream.write(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Dumping done..");
    }

    private static void addHammingBits(BitStream bitStream) {
        int position = 1;
        int streamSize = bitStream.size();
        int hammingBitsRequired = 0;
        System.out.println("Present size : " + streamSize);
        while ((1 << hammingBitsRequired) < (hammingBitsRequired + streamSize + 1)) {
            hammingBitsRequired++;
        }
        System.out.println("Hamming bits required : " + hammingBitsRequired);
        if (hammingBitsRequired % 8 > 0) {
            int extraBitsRequired = 8 - (hammingBitsRequired % 8);
            System.out.println("Extra bits required : " + extraBitsRequired);
            while (extraBitsRequired > 0) {
                bitStream.add(new Bit());
                extraBitsRequired--;
            }
        }
        streamSize = bitStream.size();
        while (position < streamSize) {
            bitStream.add(position - 1, null);
            position = position * 2;
        }
        position = 1;
        streamSize = bitStream.size();
        while (position < streamSize) {
            HammingBit hammingBit = new HammingBit(position);
            hammingBit.decideAndSetValueExcludeSelf(bitStream);
            bitStream.set(position - 1, hammingBit);
            position = position * 2;
        }
        System.out.println("Modified stream size : " + bitStream.size());
    }

}
