package ru.mitrasoft.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mitrasoft.userservice.domain.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}
