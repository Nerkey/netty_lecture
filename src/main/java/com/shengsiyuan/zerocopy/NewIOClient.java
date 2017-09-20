package com.shengsiyuan.zerocopy;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8899));
        socketChannel.configureBlocking(true);

        String fileName = "f:/Simple-Chinese_Gradle.in.Action2014.2.pdf";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();
        long position = 0;
        long transferCount = 0;
        long size = new File(fileName).length();
        long count = 0;

        while (position < fileChannel.size()) {
            transferCount = fileChannel.transferTo(0, size, socketChannel);

            if (transferCount > 0) {
                position += transferCount;
                size -= transferCount;
            }
        }



        System.out.println("发送总字节数: " + position + ", 耗时: " + (System.currentTimeMillis() - startTime));


    }


}
