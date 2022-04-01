package ru.mitrasoft.userservice;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mitrasoft.grpc.UserServiceGrpc;
import ru.mitrasoft.grpc.UserServiceRequest;
import ru.mitrasoft.grpc.UserServiceResponse;
import ru.mitrasoft.userservice.domain.AppUser;
import ru.mitrasoft.userservice.repository.AppUserRepository;

import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GrpcUserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void getUsers(UserServiceRequest request, StreamObserver<UserServiceResponse> responseObserver) {
        String userName = request.getUsername();
        String password = request.getPassword();

        Optional<AppUser> appUser = appUserRepository.findByEmail(userName);
        if (appUser.isPresent() &&
                passwordEncoder.matches(password, appUser.get().getPassword()) &&
                appUser.get().getRoles().stream().anyMatch(appUserRole -> appUserRole.getName().equals("ROLE_ADMIN"))) {
            appUserRepository.findAll()
                    .stream()
                    .map(user -> UserServiceResponse.newBuilder().setUser(user.toString()).build())
                    .forEach(responseObserver::onNext);
            responseObserver.onCompleted();
        } else {
            log.info("User {} not authenticated or doesn't have admin rights for calling grpc method", userName);
            throw new IllegalStateException("User not authenticated or doesn't have admin rights for calling grpc method");
        }
    }
}
