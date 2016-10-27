package com.iamsubhranil.personal;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

/**
 * Author : Nil
 * Date : 10/27/2016 at 8:29 AM.
 * Project : HammingStream
 */
public class Converter {

    public static void main(String [] args){
        Byte b2 = Byte.valueOf("127");
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        readBits(b2.byteValue());
    }

    private static void readBits(Byte b){
        while(b!=0){
            System.out.println(b&1);
            b = Byte.valueOf((b>>1)+"");
        }
    }

}
