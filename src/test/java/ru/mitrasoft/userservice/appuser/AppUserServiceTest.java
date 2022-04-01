package ru.mitrasoft.userservice.appuser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mitrasoft.userservice.domain.AppUser;
import ru.mitrasoft.userservice.repository.AppUserRepository;
import ru.mitrasoft.userservice.repository.AppUserRoleRepository;
import ru.mitrasoft.userservice.service.AppUserServiceImpl;
import ru.mitrasoft.userservice.utils.EmailValidator;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private AppUserRoleRepository appUserRoleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private EmailValidator emailValidator = new EmailValidator();
    private AppUserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new AppUserServiceImpl(appUserRepository,
                appUserRoleRepository,
                passwordEncoder,
                emailValidator);
    }

    @Test
    void canGetAllAppUsers() {
        // when
        underTest.getAllAppUsers();
        // then
        verify(appUserRepository).findAll();
    }

    @Test
    void canAddAppUser() {
        // given
        AppUser appUser = new AppUser(
                "Alex",
                "alexandr@gmail.com",
                "pass",
                new ArrayList<>()
        );

        // when
        underTest.saveAppUser(appUser);

        // then
        ArgumentCaptor<AppUser> appUserArgumentCaptor =
                ArgumentCaptor.forClass(AppUser.class);

        verify(appUserRepository)
                .save(appUserArgumentCaptor.capture());

        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser).isEqualTo(appUser);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        AppUser appUser = new AppUser(
                "Alex",
                "alex@gmail.com",
                "pass",
                new ArrayList<>()
        );

        given(appUserRepository.selectExistsEmail(anyString()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.saveAppUser(appUser))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email " + appUser.getEmail() + "already taken");

        verify(appUserRepository, never()).save(any());

    }

    @Test
    void canDeleteAppUser() {
        // given
        long id = 10;
        given(appUserRepository.existsById(id))
                .willReturn(true);
        // when
        underTest.deleteAppUser(id);

        // then
        verify(appUserRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeleteAppUserNotFound() {
        // given
        long id = 10;
        given(appUserRepository.existsById(id))
                .willReturn(false);
        // when
        // then
        assertThatThrownBy(() -> underTest.deleteAppUser(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("user with id " + id + " does not exists");

        verify(appUserRepository, never()).deleteById(any());
    }
}