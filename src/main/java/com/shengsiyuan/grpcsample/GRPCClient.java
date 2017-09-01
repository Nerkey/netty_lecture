package com.shengsiyuan.grpcsample;

import com.shengsiyuan.proto.MyRequest;
import com.shengsiyuan.proto.MyResponse;
import com.shengsiyuan.proto.StudentServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

public class GRPCClient {

    private final ManagedChannel channel;
    private final StudentServiceGrpc.StudentServiceBlockingStub blockingStub;

    public GRPCClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext(true)
                .build());
    }

    GRPCClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = StudentServiceGrpc.newBlockingStub(channel);
    }

    public void getRealName(String name) {
        MyRequest request = MyRequest.newBuilder().setUsername(name).build();
        MyResponse response;
        try {
            response = blockingStub.getRealNameByUsername(request);
            System.out.println(response.getRealname());
        } catch (StatusRuntimeException e) {
            return;
        }
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws Exception {
        GRPCClient client = new GRPCClient("localhost", 50051);

        try {
            client.getRealName("张三");
        } finally {
            client.shutdown();
        }
    }

}
