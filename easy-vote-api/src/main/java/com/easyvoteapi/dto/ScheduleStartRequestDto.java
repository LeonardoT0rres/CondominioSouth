package com.easyvoteapi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleStartRequestDto {

    @NotNull(message = "Campo data de início é obrigatório")
    private LocalDateTime startTime;
}
