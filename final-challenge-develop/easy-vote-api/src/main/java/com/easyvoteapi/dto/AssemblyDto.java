package com.easyvoteapi.dto;

import com.easyvoteapi.utils.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.List;

@Data
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssemblyDto {

    private Long id;
    private String name, locale, obs, cardinality;
    private String start, finish;
    private Status status;
    private List<ScheduleDto> schedules;
}
