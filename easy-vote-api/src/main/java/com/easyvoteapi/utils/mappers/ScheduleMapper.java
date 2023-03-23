package com.easyvoteapi.utils.mappers;

import com.easyvoteapi.dto.ScheduleDto;
import com.easyvoteapi.dto.ScheduleRequestDto;
import com.easyvoteapi.entities.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ScheduleMapper {
    @Mapping(source = "requestDto.assemblyId", target = "assembly.id")
    Schedule requestToEntity(ScheduleRequestDto requestDto);

    Schedule toEntity(ScheduleDto scheduleDto);

    ScheduleDto toDto(Schedule Schedule);
}
