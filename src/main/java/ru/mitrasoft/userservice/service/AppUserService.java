package ru.mitrasoft.userservice.service;

import ru.mitrasoft.userservice.domain.AppUser;
import ru.mitrasoft.userservice.domain.AppUserRole;

import java.util.List;

public interface AppUserService {
    AppUser saveAppUser(AppUser appUser);

    AppUserRole saveAppUserRole(AppUserRole appUserRole);

    void addRoleToAppUser(String email, String appUserRoleName);

    AppUser getAppUser(String email);

    List<AppUser> getAllAppUsers();

    List<AppUserRole> getAllAppUserRoles();

    void deleteAppUser(Long appUserId);

    void updateAppUser(Long appUserId, String name, String email);


}
