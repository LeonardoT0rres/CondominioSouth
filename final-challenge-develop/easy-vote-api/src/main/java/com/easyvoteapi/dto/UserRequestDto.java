package com.easyvoteapi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Campo nome é obrigatório")
    private String name;
    @NotBlank(message = "Campo email é obrigatório")
    private String email;
    private String password;
    @NotNull(message = "Campo apartamento é obrigatório")
    private Integer apartment;
    private Long roleId;
}
