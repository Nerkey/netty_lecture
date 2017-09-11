package com.shengsiyuan.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.channels.FileChannel;

/**
 * io切换nio
 */
public class NioTest2 {

    public static void main(String[] args) throws FileNotFoundException {

        FileInputStream fileInputStream = new FileInputStream("NioTest2.txt");
        FileChannel fileChannel = fileInputStream.getChannel();


    }

}
