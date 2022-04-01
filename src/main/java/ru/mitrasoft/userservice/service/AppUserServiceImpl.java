package ru.mitrasoft.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mitrasoft.userservice.domain.AppUser;
import ru.mitrasoft.userservice.domain.AppUserRole;
import ru.mitrasoft.userservice.repository.AppUserRepository;
import ru.mitrasoft.userservice.repository.AppUserRoleRepository;
import ru.mitrasoft.userservice.utils.EmailValidator;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final AppUserRoleRepository appUserRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;

    @Override
    public AppUser saveAppUser(AppUser appUser) {
        Boolean existsEmail = appUserRepository
                .selectExistsEmail(appUser.getEmail());
        if (existsEmail) {
            log.info("email {} already taken", appUser.getEmail());
            throw new IllegalStateException("email " + appUser.getEmail() + "already taken");
        }
        boolean isValidEmail = emailValidator.test(appUser.getEmail());
        if (!isValidEmail) {
            log.info("Can't saving new user to the database. Not valid email {}", appUser.getEmail());
            throw new IllegalStateException("email not valid");
        }
        log.info("Saving new user {} to the database", appUser.getEmail());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUserRole saveAppUserRole(AppUserRole appUserRole) {
        log.info("Saving new role {} to the database", appUserRole.getName());
        if (appUserRoleRepository.findByName(appUserRole.getName()).isPresent())
            throw new IllegalStateException("Role already exists in the database");
        return appUserRoleRepository.save(appUserRole);
    }

    @Override
    public void addRoleToAppUser(String email, String appUserRoleName) {
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));
        AppUserRole appUserRole = appUserRoleRepository.findByName(appUserRoleName)
                .orElseThrow(() -> new IllegalStateException("Role not found in the database"));
        log.info("Adding role {} to user {}", appUserRoleName, email);
        appUser.getRoles().add(appUserRole);
    }

    @Override
    public AppUser getAppUser(String email) {
        log.info("Fetching user {}", email);
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));
    }

    @Override
    public List<AppUser> getAllAppUsers() {
        log.info("Fetching all users");
        return appUserRepository.findAll();
    }

    @Override
    public List<AppUserRole> getAllAppUserRoles() {
        log.info("Fetching all roles");
        return appUserRoleRepository.findAll();
    }

    @Override
    public void deleteAppUser(Long appUserId) {
        boolean exists = appUserRepository.existsById(appUserId);
        if (!exists) {
            throw new IllegalStateException(
                    "user with id " + appUserId + " does not exists");
        }
        appUserRepository.deleteById(appUserId);
    }

    @Override
    public void updateAppUser(Long appUserId, String name, String email) {
        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " + appUserId + " does not exists"
                ));
        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(appUser.getName(), name)) {
            appUser.setName(name);
        }
        if (email != null &&
                email.length() > 0 &&
                !Objects.equals(appUser.getEmail(), email) &&
                emailValidator.test(email)) {
            Optional<AppUser> appUserOptional = appUserRepository
                    .findByEmail(email);
            if (appUserOptional.isPresent()) {
                throw new IllegalStateException("email already taken");
            }
            appUser.setEmail(email);
        }
        log.info("User {} updated in the database", appUser);
    }

    @Override
    public void updateAppUserRole(Long appUserRoleId, String name) {
        AppUserRole appUserRole = appUserRoleRepository.findById(appUserRoleId)
                .orElseThrow(() -> new IllegalStateException(
                        "user role with id " + appUserRoleId + " does not exists"
                ));
        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(appUserRole.getName(), name)) {
            appUserRole.setName(name);
        }
        log.info("User role {} updated in the database", appUserRole);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        if (appUser.isEmpty()) {
            log.info("User {} not found in the database", email);
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", email);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            appUser.get().getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
            return new User(appUser.get().getEmail(), appUser.get().getPassword(), authorities);
        }
    }
}

