package edu.jcourse.mapper.impl;

import edu.jcourse.dto.UserDTO;
import edu.jcourse.entity.User;
import edu.jcourse.mapper.Mapper;

public class UserMapper implements Mapper<User, UserDTO> {
    @Override
    public UserDTO mapFrom(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .birthday(user.getBirthDate())
                .image(user.getImage())
                .email(user.getEmail())
                .gender(user.getGender())
                .role(user.getRole())
                .build();
    }
}
