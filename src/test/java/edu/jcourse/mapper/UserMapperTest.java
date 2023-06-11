package edu.jcourse.mapper;

import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.entity.User;
import edu.jcourse.mapper.impl.UserMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private final UserMapper userMapper = MapperProvider.getInstance().getUserMapper();

    @Test
    void map() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .birthDate(LocalDate.of(2000, 1, 1))
                .image("userImage/image.jpg")
                .email("email")
                .gender(Gender.FEMALE)
                .role(Role.USER)
                .build();

        ReceiveUserDto actualResult = userMapper.mapFrom(user);

        ReceiveUserDto expectedResult = ReceiveUserDto.builder()
                .id(1L)
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .image("userImage/image.jpg")
                .email("email")
                .gender(Gender.FEMALE)
                .role(Role.USER)
                .build();

        assertEquals(expectedResult, actualResult);
    }
}