package ru.mitrasoft.userservice.appuser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.mitrasoft.userservice.domain.AppUser;
import ru.mitrasoft.userservice.repository.AppUserRepository;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenAppUserEmailExists() {
        // given
        String email = "alex@gmail.com";
        AppUser appUser = new AppUser(
                "Alex",
                email,
                "password",
                new ArrayList<>()
        );
        underTest.save(appUser);

        // when
        boolean expected = underTest.findByEmail(email).isPresent();

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckWhenAppUserEmailDoesNotExists() {
        // given
        String email = "akex@gmail.com";

        // when
        boolean expected = underTest.findByEmail(email).isPresent();

        // then
        assertThat(expected).isFalse();
    }
}