package com.easyvoteapi.dto;

import com.easyvoteapi.utils.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    private String name, email, passwordMessage;
    private Integer apartment;
    private Status status;
    private RoleDto role;
}
