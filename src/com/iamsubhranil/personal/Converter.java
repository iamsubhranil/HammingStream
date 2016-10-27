package com.iamsubhranil.personal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
            File f = new File("summary.txt");
            System.out.println("Reading file..");
            System.out.println(fileInputStream.read());
            while ((i = fileInputStream.read()) != -1) {
                bits.addInt(i);
            }
            fileInputStream.close();
            //  bits.forEach(System.out::print);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
