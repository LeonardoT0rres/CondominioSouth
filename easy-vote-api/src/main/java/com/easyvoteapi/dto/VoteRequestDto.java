package com.easyvoteapi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequestDto {
    private Long id;
    @NotNull(message = "O voto é obrigatório")
    private Boolean vote;
}
