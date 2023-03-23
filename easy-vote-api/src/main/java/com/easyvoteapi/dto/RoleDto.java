package com.easyvoteapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {

    private String name;
}
