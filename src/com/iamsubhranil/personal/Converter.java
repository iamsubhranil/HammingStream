package com.iamsubhranil.personal;

import java.io.FileInputStream;
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
            FileInputStream fileInputStream = new FileInputStream("test.txt");
            int i;
            System.out.println("Reading file..");
            ArrayList<Integer> backingList = new ArrayList<>();
            while ((i = fileInputStream.read()) != -1) {
                bits.addInt(i);
                backingList.add(i);
            }
            fileInputStream.close();
            int[] counter = {0};
            bits.toBytes().forEach(in -> {
                if (in != backingList.get(counter[0])) {
                    System.out.println("Value mismatch at position " + counter[0] + "\nAcutal Value : " + backingList.get(counter[0]) + " Recorded Value : " + in);
                }
                counter[0]++;
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
