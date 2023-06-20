package edu.jcourse.service;

import edu.jcourse.dao.UserDao;
import edu.jcourse.dto.CreateUserDto;
import edu.jcourse.dto.LoginUserDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.entity.User;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.impl.CreateUserMapper;
import edu.jcourse.mapper.impl.UserMapper;
import edu.jcourse.service.impl.ImageServiceImpl;
import edu.jcourse.service.impl.UserServiceImpl;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.impl.CreateUserValidator;
import edu.jcourse.validator.impl.LoginValidator;
import jakarta.servlet.http.Part;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDao userDao;
    @Mock
    private CreateUserValidator createUserValidator;
    @Mock
    private CreateUserMapper createUserMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private LoginValidator loginValidator;
    @Mock
    private ImageServiceImpl imageService;
    @Mock
    private Part part;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    private ArgumentCaptor<CreateUserDto> createUserDtoCaptor;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    @Captor
    private ArgumentCaptor<LoginUserDto> loginUserDtoCaptor;
    @Captor
    private ArgumentCaptor<ReceiveUserDto> receiveUserDtoCaptor;

    @SneakyThrows
    @Test
    void create() {
        doReturn(new ValidationResult()).when(createUserValidator).validate(any());

        CreateUserDto createUserDto = buildCreateUserDto();
        User user = buildUser();
        doReturn(user).when(createUserMapper).mapFrom(any());
        doReturn(user).when(userDao).save(any());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        doReturn(inputStream).when(part).getInputStream();

        Long actualResult = userService.create(createUserDto);

        assertThat(actualResult).isEqualTo(user.getId());
        verify(createUserValidator).validate(createUserDtoCaptor.capture());
        assertThat(createUserDtoCaptor.getValue()).isEqualTo(createUserDto);
        verify(createUserMapper).mapFrom(createUserDtoCaptor.capture());
        assertThat(createUserDtoCaptor.getValue()).isEqualTo(createUserDto);
        verify(imageService).upload(user.getImage(), inputStream);
        verify(userDao).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
    }

    @SneakyThrows
    @Test
    void createShouldThrowValidationExceptionIfUserDtoIsInvalid() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of(CodeUtil.INVALID_NAME_CODE, MessageUtil.NAME_INVALID_MESSAGE));
        doReturn(validationResult).when(createUserValidator).validate(any());

        assertThrowsExactly(ValidationException.class, () -> userService.create(buildCreateUserDto()));
        verify(createUserValidator).validate(any());

        verifyNoInteractions(createUserMapper, imageService, userDao);
    }

    @SneakyThrows
    @Test
    void createShouldThrowServiceExceptionIfDaoThrowsException() {
        doReturn(new ValidationResult()).when(createUserValidator).validate(any());

        CreateUserDto createUserDto = buildCreateUserDto();
        User user = buildUser();
        doReturn(user).when(createUserMapper).mapFrom(any());
        doThrow(new DAOException()).when(userDao).save(any());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        doReturn(inputStream).when(part).getInputStream();

        assertThrowsExactly(ServiceException.class, () -> userService.create(createUserDto));

        verify(createUserValidator).validate(createUserDtoCaptor.capture());
        assertThat(createUserDtoCaptor.getValue()).isEqualTo(createUserDto);
        verify(createUserMapper).mapFrom(createUserDtoCaptor.capture());
        assertThat(createUserDtoCaptor.getValue()).isEqualTo(createUserDto);
        verify(imageService).upload(user.getImage(), inputStream);
        verify(userDao).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
    }

    @SneakyThrows
    @Test
    void createShouldThrowServiceExceptionIfPartThrowsException() {
        doReturn(new ValidationResult()).when(createUserValidator).validate(any());

        CreateUserDto createUserDto = buildCreateUserDto();
        User user = buildUser();
        doReturn(user).when(createUserMapper).mapFrom(any());
        doThrow(IOException.class).when(part).getInputStream();

        assertThrowsExactly(ServiceException.class, () -> userService.create(createUserDto));

        verify(createUserValidator).validate(createUserDtoCaptor.capture());
        assertThat(createUserDtoCaptor.getValue()).isEqualTo(createUserDto);
        verify(createUserMapper).mapFrom(createUserDtoCaptor.capture());
        assertThat(createUserDtoCaptor.getValue()).isEqualTo(createUserDto);
        verifyNoInteractions(imageService, userDao);
    }

    @SneakyThrows
    @Test
    void loginSuccess() {
        doReturn(new ValidationResult()).when(loginValidator).validate(any());

        LoginUserDto loginUserDto = buildLoginUserDto();
        User user = buildUser();
        doReturn(Optional.of(user)).when(userDao).findByEmailAndPassword(any(), any());

        ReceiveUserDto expectedResult = buildReceiveUserDto();
        doReturn(expectedResult).when(userMapper).mapFrom(any());

        Optional<ReceiveUserDto> actualResult = userService.login(loginUserDto);
        assertThat(actualResult).isPresent().contains(expectedResult);
        verify(loginValidator).validate(loginUserDtoCaptor.capture());
        assertThat(loginUserDtoCaptor.getValue()).isEqualTo(loginUserDto);
        verify(userDao).findByEmailAndPassword(loginUserDto.email(), loginUserDto.password());
        verify(userMapper).mapFrom(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
    }

    @SneakyThrows
    @Test
    void loginFailed() {
        doReturn(new ValidationResult()).when(loginValidator).validate(any());

        LoginUserDto loginUserDto = buildLoginUserDto();
        doReturn(Optional.empty()).when(userDao).findByEmailAndPassword(any(), any());

        Optional<ReceiveUserDto> actualResult = userService.login(loginUserDto);
        assertThat(actualResult).isEmpty();
        verify(loginValidator).validate(loginUserDtoCaptor.capture());
        assertThat(loginUserDtoCaptor.getValue()).isEqualTo(loginUserDto);
        verify(userDao).findByEmailAndPassword(loginUserDto.email(), loginUserDto.password());
        verifyNoInteractions(userMapper);
    }

    @SneakyThrows
    @Test
    void loginShouldThrowValidationExceptionIfLoginUserDtoIsInvalid() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of(CodeUtil.INVALID_EMAIL_CODE, MessageUtil.EMAIL_INVALID_MESSAGE));

        doReturn(validationResult).when(loginValidator).validate(any());

        assertThrowsExactly(ValidationException.class, () -> userService.login(buildLoginUserDto()));
        verify(loginValidator).validate(any());

        verifyNoInteractions(userDao, userMapper);
    }

    @SneakyThrows
    @Test
    void loginShouldThrowServiceExceptionIfDaoThrowsException() {
        doReturn(new ValidationResult()).when(loginValidator).validate(any());

        LoginUserDto loginUserDto = buildLoginUserDto();
        doThrow(new DAOException()).when(userDao).findByEmailAndPassword(any(), any());

        assertThrowsExactly(ServiceException.class, () -> userService.login(loginUserDto));
        verify(loginValidator).validate(loginUserDtoCaptor.capture());
        assertThat(loginUserDtoCaptor.getValue()).isEqualTo(loginUserDto);
        verify(userDao).findByEmailAndPassword(loginUserDto.email(), loginUserDto.password());
        verifyNoInteractions(userMapper);
    }

    @SneakyThrows
    @Test
    void findById() {
        User user = buildUser();
        doReturn(Optional.of(user)).when(userDao).findById(any());
        ReceiveUserDto expectedResult = buildReceiveUserDto();
        doReturn(expectedResult).when(userMapper).mapFrom(any());

        Optional<ReceiveUserDto> actualResult = userService.findById(1L);

        assertThat(actualResult).isPresent().contains(expectedResult);
        verify(userDao).findById(1L);
        verify(userMapper).mapFrom(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
    }

    @SneakyThrows
    @Test
    void findByIdShouldThrowServiceExceptionIfDaoThrowsException() {
        doThrow(new DAOException()).when(userDao).findById(any());

        assertThrowsExactly(ServiceException.class, () -> userService.findById(1L));
        verify(userDao).findById(1L);
        verifyNoInteractions(userMapper);
    }

    private CreateUserDto buildCreateUserDto() {
        return CreateUserDto.builder()
                .name("test")
                .birthDate("2000-01-01")
                .password("test")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .email("test")
                .partImage(part)
                .build();
    }

    private User buildUser() {
        return User.builder()
                .id(1L)
                .name("test")
                .image("test")
                .email("test")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .gender(Gender.MALE)
                .password("test")
                .build();
    }

    private ReceiveUserDto buildReceiveUserDto() {
        return ReceiveUserDto.builder()
                .id(1L)
                .name("test")
                .image("test")
                .email("test")
                .birthday(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }

    private LoginUserDto buildLoginUserDto() {
        return LoginUserDto.builder()
                .email("test")
                .password("test")
                .build();
    }

}