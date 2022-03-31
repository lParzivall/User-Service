package ru.mitrasoft.userservice.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StringUtils;
import ru.mitrasoft.userservice.domain.AppUser;
import ru.mitrasoft.userservice.domain.AppUserRole;
import ru.mitrasoft.userservice.repository.AppUserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class AppUserIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    private final Faker faker = new Faker();


    @Test
    void canRegisterNewAppUser() throws Exception {
        // given
        String name = String.format(
                "%s %s",
                faker.name().firstName(),
                faker.name().lastName()
        );
        String password = faker.internet().password();


        AppUser appUser = new AppUser(
                name,
                String.format("%s@email.ru",
                        StringUtils.trimAllWhitespace(name.trim().toLowerCase())),
                password,
                List.of(new AppUserRole(1L, "ROLE_ADMIN"))
        );

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/users")
                        .with(user("admin@mail.ru").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUser)));
        // then
        resultActions.andExpect(status().isCreated());

        assertThat(appUserRepository.selectExistsEmail(appUser.getEmail())).isTrue();
    }

    @Test
    void canDeleteAppUser() throws Exception {
        // given
        String name = String.format(
                "%s %s",
                faker.name().firstName(),
                faker.name().lastName()
        );

        String password = faker.internet().password();

        String email = String.format("%s@email.ru",
                StringUtils.trimAllWhitespace(name.trim().toLowerCase()));

        AppUser appUser = new AppUser(
                name,
                email,
                password,
                new ArrayList<>()
        );

        mockMvc.perform(post("/api/v1/users")
                        .with(user("admin@mail.ru").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUser)))
                .andExpect(status().isCreated());

        MvcResult getAppUsersResult = mockMvc.perform(get("/api/v1/users")
                        .with(user("admin@mail.ru").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getAppUsersResult
                .getResponse()
                .getContentAsString();

        List<AppUser> appUsers = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        long id = appUsers.stream()
                .filter(s -> s.getEmail().equals(appUser.getEmail()))
                .map(AppUser::getId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "user with email: " + email + " not found"));

        // when
        ResultActions resultActions = mockMvc
                .perform(delete("/api/v1/users/" + id).
                        with(user("admin@mail.ru").roles("USER", "ADMIN")));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = appUserRepository.existsById(id);
        assertThat(exists).isFalse();
    }
}
