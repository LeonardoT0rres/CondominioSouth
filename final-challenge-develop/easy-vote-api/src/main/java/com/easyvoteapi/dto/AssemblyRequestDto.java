package com.easyvoteapi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class AssemblyRequestDto {

    @NotBlank(message = "Campo nome é obrigatório")
    private String name;
    @NotBlank(message = "Campo local é obrigatório")
    private String locale;
    @NotBlank(message = "Campo observação é obrigatório")
    private String obs;
    @NotBlank(message = "Campo cardinalidade é obrigatório")
    private String cardinality;
    @NotNull(message = "Campo data de início é obrigatório")
    private LocalDateTime start;
}
