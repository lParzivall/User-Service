package ru.mitrasoft.userservice;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mitrasoft.grpc.UserServiceGrpc;
import ru.mitrasoft.grpc.UserServiceRequest;
import ru.mitrasoft.grpc.UserServiceResponse;
import ru.mitrasoft.userservice.repository.AppUserRepository;

@GrpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public void getUsers(UserServiceRequest request, StreamObserver<UserServiceResponse> responseObserver) {
        appUserRepository.findAll()
                .stream()
                .map(appUser -> UserServiceResponse.newBuilder().setUser(appUser.toString()).build())
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}
