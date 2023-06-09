package edu.jcourse.validator;

import edu.jcourse.dto.LoginUserDto;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.validator.impl.LoginValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginValidatorTest {

    private final LoginValidator loginValidator = ValidatorProvider.getInstance().getLoginValidator();

    @Test
    void shouldPassValidation() {
        LoginUserDto loginUserDTO = LoginUserDto.builder()
                .email("email@example.com")
                .password("password")
                .build();

        ValidationResult actualResult = loginValidator.validate(loginUserDTO);

        assertTrue(actualResult.isValid());
    }

    @Test
    void invalidEmail() {
        LoginUserDto loginUserDTO = LoginUserDto.builder()
                .email("email")
                .password("password")
                .build();

        ValidationResult actualResult = loginValidator.validate(loginUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_EMAIL_CODE);
    }

    @Test
    void invalidPassword() {
        LoginUserDto loginUserDTO = LoginUserDto.builder()
                .email("email@example.com")
                .password("invalid")
                .build();

        ValidationResult actualResult = loginValidator.validate(loginUserDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_PASSWORD_CODE);
    }

    @Test
    void invalidEmailAndPassword() {
        LoginUserDto loginUserDTO = LoginUserDto.builder()
                .email("email")
                .password("invalid")
                .build();

        ValidationResult actualResult = loginValidator.validate(loginUserDTO);

        assertThat(actualResult.getErrors()).hasSize(2);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();

        assertThat(errorCodes).containsExactlyInAnyOrder(CodeUtil.INVALID_EMAIL_CODE, CodeUtil.INVALID_PASSWORD_CODE);
    }
}