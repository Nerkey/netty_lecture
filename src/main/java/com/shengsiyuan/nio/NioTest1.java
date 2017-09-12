package com.shengsiyuan.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * 生成随机数, 然后打印出来
 */
public class NioTest1 {

    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);

        System.out.println("capacity: " + buffer.capacity());

        for (int i = 0; i < 5; ++i) {
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }

        System.out.println("after flip limit: " + buffer.limit()); // 10

        buffer.flip();

        System.out.println("after flip limit: " + buffer.limit()); // 5

        System.out.println("enter while loop");

        while (buffer.hasRemaining()) {
            System.out.println("position: " + buffer.position()); // 0, 1, 2, 3, 4
            System.out.println("limit: " + buffer.limit()); // 5
            System.out.println("capacity: " + buffer.capacity()); // 10

            System.out.println(buffer.get());
        }
    }

}
