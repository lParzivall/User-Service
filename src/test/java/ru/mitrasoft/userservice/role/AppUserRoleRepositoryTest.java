package ru.mitrasoft.userservice.role;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.mitrasoft.userservice.domain.AppUser;
import ru.mitrasoft.userservice.domain.AppUserRole;
import ru.mitrasoft.userservice.repository.AppUserRepository;
import ru.mitrasoft.userservice.repository.AppUserRoleRepository;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AppUserRoleRepositoryTest {

    @Autowired
    private AppUserRoleRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenAppUserRoleExists() {
        // given
        String name = "ROLE_TEST";
        AppUserRole appUserRole = new AppUserRole(
                    name
        );
        underTest.save(appUserRole);

        // when
        boolean expected = underTest.findByName(name).isPresent();

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckWhenAppUserEmailDoesNotExists() {
        // given
        String name = "ROLE_TEST";

        // when
        boolean expected = underTest.findByName(name).isPresent();

        // then
        assertThat(expected).isFalse();
    }
}