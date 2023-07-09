package edu.jcourse.service;

import edu.jcourse.dto.CreateUserDto;
import edu.jcourse.dto.LoginUserDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.util.Config;
import jakarta.servlet.http.Part;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UserServiceIT extends IntegrationTestBase {

    private static final String FILE_NAME = "image.png";
    private static final String IMAGE_FOLDER = "userImage/";
    private final UserService userService = ServiceProvider.getInstance().getUserService();
    @Mock
    private Part imagePart;

    @SneakyThrows
    @AfterEach
    void deleteImage() {
        Path imagePath = Path.of(Config.getProperty(Config.IMAGE_BASE_URL), IMAGE_FOLDER, FILE_NAME);
        Files.deleteIfExists(imagePath);
    }

    @SneakyThrows
    @Test
    void create() {
        CreateUserDto createUserDto = buildCreateUserDto("email@example.com");
        Path imagePath = Path.of(Config.getProperty(Config.IMAGE_BASE_URL), IMAGE_FOLDER, FILE_NAME);

        Long actualResult = userService.create(createUserDto);

        assertThat(actualResult).isNotNull();
        assertTrue(Files.exists(imagePath));
    }

    @SneakyThrows
    @Test
    void shouldThrowValidationExceptionIfUserDtoIsInvalid() {
        CreateUserDto createUserDto = buildCreateUserDto("dummy");

        ValidationException actualResult = assertThrowsExactly(ValidationException.class, () -> userService.create(createUserDto));
        assertThat(actualResult.getErrors()).hasSize(1);
    }

    @SneakyThrows
    @Test
    void login() {
        userService.create(buildCreateUserDto("email@example.com"));

        Optional<ReceiveUserDto> actualResult = userService.login(LoginUserDto.builder()
                .email("email@example.com")
                .password("password")
                .build());

        assertThat(actualResult).isPresent();
    }

    @SneakyThrows
    @Test
    void shouldThrowValidationExceptionIfLoginUserDtoIsInvalid() {
        userService.create(buildCreateUserDto("email@example.com"));

        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("dummy")
                .password("password")
                .build();

        ValidationException actualResult = assertThrowsExactly(ValidationException.class, () -> userService.login(loginUserDto));
        assertThat(actualResult.getErrors()).hasSize(1);
    }

    @SneakyThrows
    @Test
    void findById() {
        Long userId = userService.create(buildCreateUserDto("email@example.com"));

        Optional<ReceiveUserDto> actualResult = userService.findById(userId);

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().id()).isEqualTo(userId);
    }

    @SneakyThrows
    @Test
    void shouldNotFindByIdIfUserDoesNotExist() {
        userService.create(buildCreateUserDto("email@example.com"));

        Optional<ReceiveUserDto> actualResult = userService.findById(34L);

        assertThat(actualResult).isEmpty();
    }


    @SneakyThrows
    private CreateUserDto buildCreateUserDto(String email) {
        InputStream inputStream = UserServiceIT.class.getClassLoader().getResourceAsStream(FILE_NAME);
        lenient().doReturn(inputStream).when(imagePart).getInputStream();
        lenient().doReturn(FILE_NAME).when(imagePart).getSubmittedFileName();

        return CreateUserDto.builder()
                .name("test")
                .birthDate("2000-01-01")
                .email(email)
                .password("password")
                .role(Role.USER.name())
                .gender(Gender.FEMALE.name())
                .partImage(imagePart)
                .build();
    }

}