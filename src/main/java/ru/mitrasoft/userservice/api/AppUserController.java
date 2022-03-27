package ru.mitrasoft.userservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mitrasoft.userservice.domain.AppUser;
import ru.mitrasoft.userservice.domain.AppUserRole;
import ru.mitrasoft.userservice.service.AppUserService;
import ru.mitrasoft.userservice.service.TokenService;
import ru.mitrasoft.userservice.utils.RoleToUserForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;
    private final TokenService tokenService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAppUsers() {
        return ResponseEntity.ok().body(appUserService.getAppUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUser appUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users").toUriString());
        return ResponseEntity.created(uri).body(appUserService.saveAppUser(appUser));
    }

    @DeleteMapping( "/users/{userId}")
    public void deleteAppUser(
            @PathVariable("userId") Long appUserId) {
        appUserService.deleteAppUser(appUserId);
    }

    @PutMapping( "/users/{userId}")
    public void updateStudent(
            @PathVariable("userId") Long appUserId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        appUserService.updateAppUser(appUserId, name, email);
    }

    @PostMapping("/roles")
    public ResponseEntity<AppUserRole> saveRole(@RequestBody AppUserRole role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/roles").toUriString());
        return ResponseEntity.created(uri).body(appUserService.saveAppUserRole(role));
    }

    @PostMapping("/roles/addtouser")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserForm form) {
        appUserService.addRoleToAppUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        tokenService.refreshToken(request, response);
    }

}