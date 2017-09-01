package com.shengsiyuan.grpcsample;

import com.shengsiyuan.proto.MyRequest;
import com.shengsiyuan.proto.MyResponse;
import com.shengsiyuan.proto.StudentServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

/**
 * Created by ququ1 on 2017/9/1.
 */
public class GrpcServer {

    private Server server;

    private void start() throws IOException {
    /* The port on which the server should run */
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new GetRealNameImpl())
                .build()
                .start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            GrpcServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final GrpcServer server = new GrpcServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class GetRealNameImpl extends StudentServiceGrpc.StudentServiceImplBase {
        public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse> responseObserver) {
            System.out.println(request.getUsername());
            MyResponse response = MyResponse.newBuilder().setRealname("Mr. " + request.getUsername()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

}
