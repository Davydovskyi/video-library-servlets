package edu.jcourse.validator;

import edu.jcourse.dao.UserDao;
import edu.jcourse.dto.CreateUserDto;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.entity.User;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.validator.impl.CreateUserValidator;
import jakarta.servlet.http.Part;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserValidatorTest {
    @Mock
    private UserDao userDAO;
    @Mock
    private Part imagePart;
    @InjectMocks
    private CreateUserValidator createUserValidator;

    static Stream<Arguments> getImageNameArguments() {
        return Stream.of(
                Arguments.of("image.png"),
                Arguments.of("image.jpg"),
                Arguments.of("image.jpeg"),
                Arguments.of("image.PNG")
        );
    }

    @BeforeEach
    void setUp() {
        lenient().doReturn("image.png").when(imagePart).getSubmittedFileName();
        lenient().doReturn(10L).when(imagePart).getSize();
    }

    @SneakyThrows
    @Test
    void shouldPassValidation() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("name")
                .birthDate("2014-01-01")
                .email("email@example.com")
                .password("password")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .partImage(imagePart)
                .build();

        doReturn(Optional.empty()).when(userDAO).findByEmail(any());

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertTrue(actualResult.isValid());
        verify(userDAO).findByEmail(createUserDTO.email());
    }

    @SneakyThrows
    @Test
    void shouldNotPassValidationIfEmailAlreadyExists() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("name")
                .birthDate("2014-01-01")
                .email("email@example.com")
                .password("password")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .partImage(imagePart)
                .build();

        doReturn(Optional.of(User.builder().build())).when(userDAO).findByEmail(any());

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.EXIST_EMAIL_CODE);
        verify(userDAO).findByEmail(createUserDTO.email());
    }

    @SneakyThrows
    @Test
    void invalidName() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("")
                .birthDate("2014-01-01")
                .email("email@example.com")
                .password("password")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .partImage(imagePart)
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_NAME_CODE);
        verifyNoInteractions(userDAO);
    }

    @SneakyThrows
    @Test
    void invalidBirthday() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("name")
                .birthDate("")
                .email("email@example.com")
                .password("password")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .partImage(imagePart)
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_BIRTHDAY_CODE);
        verifyNoInteractions(userDAO);
    }

    @SneakyThrows
    @Test
    void invalidEmail() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("name")
                .birthDate("2014-01-01")
                .email("")
                .password("password")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .partImage(imagePart)
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_EMAIL_CODE);
        verifyNoInteractions(userDAO);
    }

    @SneakyThrows
    @Test
    void invalidPassword() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("name")
                .birthDate("2014-01-01")
                .email("email@example.com")
                .password("")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .partImage(imagePart)
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_PASSWORD_CODE);
        verifyNoInteractions(userDAO);
    }

    @SneakyThrows
    @Test
    void invalidGender() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("name")
                .birthDate("2014-01-01")
                .email("email@example.com")
                .password("password")
                .gender("")
                .role(Role.USER.name())
                .partImage(imagePart)
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_GENDER_CODE);
        verifyNoInteractions(userDAO);
    }

    @SneakyThrows
    @Test
    void invalidRole() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("name")
                .birthDate("2014-01-01")
                .email("email@example.com")
                .password("password")
                .gender(Gender.MALE.name())
                .role("")
                .partImage(imagePart)
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_ROLE_CODE);
        verifyNoInteractions(userDAO);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("getImageNameArguments")
    void shouldPassValidationIfImageNameIsValid(String imageName) {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("name")
                .birthDate("2014-01-01")
                .email("email@example.com")
                .password("password")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .partImage(imagePart)
                .build();

        doReturn(imageName).when(imagePart).getSubmittedFileName();

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();

        assertThat(errorCodes).isEmpty();
    }

    @SneakyThrows
    @Test
    void invalidPartImage() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("name")
                .birthDate("2014-01-01")
                .email("email@example.com")
                .password("password")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .partImage(null)
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_IMAGE_CODE);
        verifyNoInteractions(userDAO);
    }

    @SneakyThrows
    @Test
    void invalidPartImageSize() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("name")
                .birthDate("2014-01-01")
                .email("email@example.com")
                .password("password")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .partImage(imagePart)
                .build();

        doReturn(1025 * 1025L).when(imagePart).getSize();

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_IMAGE_SIZE_CODE);
        verifyNoInteractions(userDAO);
    }

    @SneakyThrows
    @Test
    void invalidPartImageType() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("name")
                .birthDate("2014-01-01")
                .email("email@example.com")
                .password("password")
                .gender(Gender.MALE.name())
                .role(Role.USER.name())
                .partImage(imagePart)
                .build();

        doReturn("dummy").when(imagePart).getSubmittedFileName();

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_IMAGE_FORMAT_CODE);
        verifyNoInteractions(userDAO);
    }

    @SneakyThrows
    @Test
    void invalidAllFields() {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name("")
                .birthDate("invalid")
                .email("invalid")
                .password("invalid")
                .gender("invalid")
                .role("invalid")
                .partImage(null)
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDTO);

        assertThat(actualResult.getErrors()).hasSize(7);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        assertThat(errorCodes).contains(
                CodeUtil.INVALID_NAME_CODE,
                CodeUtil.INVALID_BIRTHDAY_CODE,
                CodeUtil.INVALID_EMAIL_CODE,
                CodeUtil.INVALID_PASSWORD_CODE,
                CodeUtil.INVALID_GENDER_CODE,
                CodeUtil.INVALID_ROLE_CODE,
                CodeUtil.INVALID_IMAGE_CODE
        );
        verifyNoInteractions(userDAO);
    }
}