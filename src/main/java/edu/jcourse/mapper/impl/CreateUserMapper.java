package edu.jcourse.mapper.impl;

import edu.jcourse.dto.CreateUserDTO;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.entity.User;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.util.LocalDateFormatter;

public class CreateUserMapper implements Mapper<CreateUserDTO, User> {

    private static final String IMAGE_FOLDER = "userImage/";

    @Override
    public User mapFrom(CreateUserDTO createUserDTO) {
        return User.builder()
                .name(createUserDTO.name())
                .birthDate(LocalDateFormatter.parse(createUserDTO.birthDate()))
                .image(IMAGE_FOLDER + createUserDTO.partImage().getSubmittedFileName())
                .email(createUserDTO.email())
                .password(createUserDTO.password())
                .gender(Gender.valueOf(createUserDTO.gender()))
                .role(Role.valueOf(createUserDTO.role()))
                .build();
    }
}
