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

    public static void main(String[] args) {
        //       Byte b2 = Byte.valueOf("127");
//        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        try {

            BitStream bits = new BitStream();
            FileInputStream fileInputStream = new FileInputStream("summary.txt");
            int i;
            System.out.println("Reading file..");
            int bitsize = 0;
            ArrayList<Integer> backingList = new ArrayList<>();
            while ((i = fileInputStream.read()) != -1) {
                bits.addInt(i);
                backingList.add(i);
                bitsize++;
            }
            System.out.println("Reading completed..");
            fileInputStream.close();
            int[] counter = {0};
            System.out.println("Size of bits\nActual : " + bitsize * 8 + "\tRecorded : " + bits.size());
            System.out.println("Size of bytes : " + bits.toBytes().size());
            System.out.println("Dumping to summary_frombin.txt..");
            FileOutputStream fileOutputStream = new FileOutputStream("summary_frombin.txt");
            bits.toBytes().forEach(in -> {
                if (in != backingList.get(counter[0])) {
                    System.err.println("Value mismatch at position " + counter[0] + "..\nActual value : " + backingList.get(counter[0]) + "\nSaved value : " + in);
                }
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

}
