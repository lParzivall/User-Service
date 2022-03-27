package ru.mitrasoft.userservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mitrasoft.userservice.domain.AppUser;
import ru.mitrasoft.userservice.domain.AppUserRole;
import ru.mitrasoft.userservice.service.AppUserService;

import java.util.ArrayList;

@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	CommandLineRunner run(AppUserService appUserService) {
//		return args -> {
//			appUserService.saveAppUserRole(new AppUserRole(null, "ROLE_USER"));
//			appUserService.saveAppUserRole(new AppUserRole(null, "ROLE_MANAGER"));
//			appUserService.saveAppUserRole(new AppUserRole(null, "ROLE_ADMIN"));
//			appUserService.saveAppUserRole(new AppUserRole(null, "ROLE_SUPER_ADMIN"));
//
//			appUserService.saveAppUser(new AppUser(null, "John Travolta", "john@gfhgh.ry", "1234", new ArrayList<>()));
//			appUserService.saveAppUser(new AppUser(null, "Will Smith", "will@sdf.ty", "1234", new ArrayList<>()));
//			appUserService.saveAppUser(new AppUser(null, "Jim Carry", "jim@rdgdfg.tu", "1234", new ArrayList<>()));
//			appUserService.saveAppUser(new AppUser(null, "Arnold Schwarzenegger", "arnold@afsdf.ry", "1234", new ArrayList<>()));
//
////			appUserService.addRoleToAppUser("john@gfhgh.ry", "ROLE_USER");
////			appUserService.addRoleToAppUser("will@sdf.ty", "ROLE_MANAGER");
////			appUserService.addRoleToAppUser("jim@rdgdfg.tu", "ROLE_ADMIN");
////			appUserService.addRoleToAppUser("arnold@afsdf.ry", "ROLE_SUPER_ADMIN");
//			appUserService.addRoleToAppUser("arnold@afsdf.ry", "ROLE_ADMIN");
////			appUserService.addRoleToAppUser("arnold@afsdf.ry", "ROLE_USER");
//		};
//	}

}
