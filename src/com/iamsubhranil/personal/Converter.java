package com.iamsubhranil.personal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Author : Nil
 * Date : 10/27/2016 at 8:29 AM.
 * Project : HammingStream
 */
public class Converter {

    private static final String FILE_NAME = "pic";
    private static BitStream actualBitstream = new BitStream();
    private static BitStream recoveredBitstream = new BitStream();

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
            System.out.println("Reading priginal file..");
            int bitsize = 0;
//            ArrayList<Integer> backingList = new ArrayList<>();
            while ((i = fileInputStream.read()) != -1) {
                bits.addInt(i);
                //               backingList.add(i);
                bitsize++;
            }
            bits.forEach(bit -> actualBitstream.add(bit));
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
        int count = 0;
        System.out.println("Checking error..");
        int streamSize = bitStream.size();
        bitStream.remove(streamSize - 1);
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
        } else {
            System.out.println("File is ok..");
            position = 1 << (count - 1);
            ArrayList<Integer> removePositions = new ArrayList<>();
            System.out.println("Size before removing hamming bits : " + bitStream.size());
            System.out.println("Removing hamming bits..");
            while (position > 0) {
                System.out.println("Adding position " + (position - 1) + " to the removal list..");
                removePositions.add(position - 1);
                position = position / 2;
            }
            removePositions.forEach(pos -> bitStream.remove(pos.intValue()));
            System.out.println("Size after removing hamming bits : " + bitStream.size());
            int extraBits = 7 - (count % 8);
            System.out.println("Size before removing the extra bits : " + bitStream.size());
            System.out.println("Removing " + extraBits + " extra bits..");
            while (extraBits > 0) {
                bitStream.remove(bitStream.size() - 1);
                extraBits--;
            }
            System.out.println("Size after removing extra bits : " + bitStream.size());
            recoveredBitstream = bitStream;
            System.out.println("Dumping final bitmap..");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME + "_frombin.txt");
                bitStream.toBytes().forEach(in -> {
                    //   if (in != backingList.get(counter[0])) {
                    //        System.err.println("Value mismatch at position " + counter[0] + "..\nActual value : " + backingList.get(counter[0]) + "\nSaved value : " + in);
                    //    }
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
        //      bitStream.printStream();
        //    actualBitstream.printStream();
        //    recoveredBitstream.printStream();
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
        int extraBitsRequired = 8 - (hammingBitsRequired % 8);
        System.out.println("Extra bits required : " + extraBitsRequired);
        while (extraBitsRequired > 0) {
            bitStream.add(new Bit());
            extraBitsRequired--;
        }
        streamSize = bitStream.size();
        //       System.out.println("Actual bitmap : ");
        //       bitStream.printStream();
        //      System.out.println("Hamming bits : ");
        while (position < streamSize) {
            HammingBit hammingBit = new HammingBit(position);
            bitStream.add(position - 1, hammingBit);
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
        //      System.out.println("\nModified bitmap : ");
        //      bitStream.printStream();
        System.out.println("Modified stream size : " + bitStream.size());
        //      System.out.println("Hamming bits added : " + (bitStream.size() - streamSize));
    }

}
