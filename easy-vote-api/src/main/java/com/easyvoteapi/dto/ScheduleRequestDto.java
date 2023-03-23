package com.easyvoteapi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequestDto {

    @NotBlank(message = "Campo nome é obrigatório")
    private String name;
    @NotBlank(message = "Campo descrição é obrigatório")
    private String description;
    @NotNull(message = "Campo número de pauta é obrigatório")
    private Integer scheduleOrder;
    private Long assemblyId;
}
