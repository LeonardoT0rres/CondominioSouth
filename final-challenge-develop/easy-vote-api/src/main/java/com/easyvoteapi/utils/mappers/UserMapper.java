package com.easyvoteapi.utils.mappers;

import com.easyvoteapi.dto.UserDto;
import com.easyvoteapi.dto.UserRequestDto;
import com.easyvoteapi.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(source = "requestDto.roleId", target = "role.id")
    User requestToEntity(UserRequestDto requestDto);

    User toEntity(UserDto userDto);

    UserDto toDto(User entity);
}
