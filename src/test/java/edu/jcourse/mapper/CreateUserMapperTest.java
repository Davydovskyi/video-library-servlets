package edu.jcourse.mapper;

import edu.jcourse.dto.CreateUserDto;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.entity.User;
import edu.jcourse.mapper.impl.CreateUserMapper;
import jakarta.servlet.http.Part;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateUserMapperTest {

    private final CreateUserMapper createUserMapper = MapperProvider.getInstance().getCreateUserMapper();

    @Test
    void map() {
        Part partMock = Mockito.mock(Part.class);
        Mockito.when(partMock.getSubmittedFileName()).thenReturn("name.jpg");

        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("name")
                .birthDate("2020-01-01")
                .partImage(partMock)
                .email("email@email.com")
                .password("password")
                .gender(Gender.FEMALE.name())
                .role(Role.USER.name())
                .build();

        User actualResult = createUserMapper.mapFrom(createUserDto);

        User expectedResult = User.builder()
                .name("name")
                .birthDate(LocalDate.of(2020, 1, 1))
                .image("userImage/name.jpg")
                .email("email@email.com")
                .password("password")
                .gender(Gender.FEMALE)
                .role(Role.USER)
                .build();

        assertEquals(expectedResult, actualResult);
        Mockito.verify(partMock).getSubmittedFileName();
        Mockito.verifyNoMoreInteractions(partMock);
    }
}