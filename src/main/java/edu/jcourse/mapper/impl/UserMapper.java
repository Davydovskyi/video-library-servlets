package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.User;
import edu.jcourse.mapper.Mapper;

public class UserMapper implements Mapper<User, ReceiveUserDto> {
    @Override
    public ReceiveUserDto mapFrom(User user) {
        return ReceiveUserDto.builder()
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