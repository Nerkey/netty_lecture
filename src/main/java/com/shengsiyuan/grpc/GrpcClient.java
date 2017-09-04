package com.shengsiyuan.grpc;

import com.shengsiyuan.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by ququ1 on 2017/9/4.
 */
public class GrpcClient {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 8899)
                .usePlaintext(true)
                .build();
        StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(managedChannel);
        MyResponse myResponse = blockingStub.getRealNameByUsername(MyRequest.newBuilder().setUsername("zhangsan").build());
        System.out.println(myResponse.getRealname());

        System.out.println("-------------------------");

        Iterator<StudentResponse> studentsByAge = blockingStub.getStudentsByAge(StudentRequest.newBuilder().setAge(20).build());

        studentsByAge.forEachRemaining(item -> {
            System.out.println(item.getName() + ", " + item.getAge() + ", " + item.getCity());
        });


        managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

}
