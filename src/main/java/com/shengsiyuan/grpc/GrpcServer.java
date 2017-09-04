package com.shengsiyuan.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by ququ1 on 2017/9/4.
 */
public class GrpcServer {

    private Server server;

    private void start() throws IOException {
        this.server = ServerBuilder
                .forPort(8899)
                .addService(new StudentServiceImpl())
                .build()
                .start();

        System.out.println("server started");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("关闭Jvm");
            GrpcServer.this.stop();
        }));

        System.out.println("执行到这里");
    }

    private void stop() {
        if (null != this.server) {
            this.server.shutdown();
        }
    }

    private void awaitTermination() throws InterruptedException {
        if (null != server) {
            this.server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        GrpcServer server = new GrpcServer();

        server.start();
        server.awaitTermination();
    }


}
