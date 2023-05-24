package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceiveUserDTO;
import edu.jcourse.entity.User;
import edu.jcourse.mapper.Mapper;

public class UserMapper implements Mapper<User, ReceiveUserDTO> {
    @Override
    public ReceiveUserDTO mapFrom(User user) {
        return ReceiveUserDTO.builder()
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