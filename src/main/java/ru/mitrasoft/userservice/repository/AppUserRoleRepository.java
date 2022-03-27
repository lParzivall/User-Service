package ru.mitrasoft.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mitrasoft.userservice.domain.AppUserRole;

import java.util.Optional;

public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Long> {
    Optional<AppUserRole> findByName(String name);
}
