package ru.mitrasoft.userservice.service;

import io.grpc.stub.StreamObserver;
import ru.mitrasoft.grpc.UserServiceGrpc;
import ru.mitrasoft.grpc.UserServiceRequest;
import ru.mitrasoft.grpc.UserServiceResponse;


public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    @Override
    public void getUsers(UserServiceRequest request, StreamObserver<UserServiceResponse> responseObserver) {
        UserServiceResponse response = UserServiceResponse.newBuilder()
                .setUsers(0,"Hello from Server")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
