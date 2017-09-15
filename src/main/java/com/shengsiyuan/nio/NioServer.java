package com.shengsiyuan.nio;

import io.grpc.Server;
import io.netty.buffer.ByteBuf;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NioServer {

    private static Map<String, SocketChannel> clientMap = new HashMap<>(); // 维护着所有客户端的连接信息

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false); // 非阻塞
        ServerSocket serverSocket = serverSocketChannel.socket(); // 获得服务器端ServerSocket对象
        serverSocket.bind(new InetSocketAddress(8899)); // 绑定端口号

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // 注册到Selector对象上

        while (true) {
            try {
                System.out.println("事件数量: " + selector.select()); // 阻塞, 返回所关注的事件的数量

                Set<SelectionKey> selectionKeys = selector.selectedKeys(); // 返回已经发生的key

                System.out.println("selectionKeys: " + selectionKeys);

                selectionKeys.forEach(selectionKey -> {
                    final SocketChannel client; // 和客户端通信的Channel对象

                    try {
                        if (selectionKey.isAcceptable()) { // 表示客户端向服务端发起连接
                            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel(); // 获取到当前事件发生在哪一个Channel上面
                            client = server.accept(); // 建立连接
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ); // 将channel注册到selector上

                            String key = "[" + UUID.randomUUID().toString() + "]";

                            clientMap.put(key, client);
                        } else if (selectionKey.isReadable()) { // 是否有数据可读
                            client = (SocketChannel) selectionKey.channel(); // 获取通道对象
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                            int count = client.read(readBuffer); // 将客户端发送过来的数据读到readBuffer中

                            if (count > 0) {
                                readBuffer.flip();

                                Charset charset = Charset.forName("utf-8"); // 用utf-8编码
                                String receivedMessage = String.valueOf(charset.decode(readBuffer).array()); // 将字节数组解码 -> charBuffer -> char数组 -> 字符串

                                System.out.println(client + ": " + receivedMessage);

                                String senderKey = null;

                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) { // 根据当前的SocketChannel 获取map的value -> 获取value 对应的key
                                    if (client == entry.getValue()) {
                                        senderKey = entry.getKey();
                                        break;
                                    }
                                }

                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) { // 向每一个已连接的客户端发送数据
                                    SocketChannel value = entry.getValue();

                                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

                                    writeBuffer.put((senderKey + ": " + receivedMessage).getBytes()); // 读数据

                                    writeBuffer.flip();

                                    value.write(writeBuffer); // 写数据
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                selectionKeys.clear(); // 处理完后清空selectionsKeys

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
