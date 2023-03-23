package com.easyvoteapi.dto;

import com.easyvoteapi.utils.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDateTime;

@Data
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleDto {

    private Long id;
    private String name;
    private String description;
    private Status status;
    private Integer scheduleOrder;
    private Long assemblyId;
    private Integer positiveVotes;
    private Integer negativeVotes;
    private LocalDateTime endTime;
}
