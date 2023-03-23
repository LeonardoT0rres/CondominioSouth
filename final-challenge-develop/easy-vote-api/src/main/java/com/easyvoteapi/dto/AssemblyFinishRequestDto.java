package com.easyvoteapi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class AssemblyFinishRequestDto {

    @NotNull(message = "Campo data de encerramento é obrigatório")
    private LocalDateTime endTime;
}
