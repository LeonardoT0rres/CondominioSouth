package com.easyvoteapi.dto;

import lombok.*;

@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    private Long id;
    private String voteMessage;
}
