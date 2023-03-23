package com.easyvoteapi.utils.mappers;

import com.easyvoteapi.dto.AssemblyDto;
import com.easyvoteapi.dto.AssemblyRequestDto;
import com.easyvoteapi.entities.Assembly;
import org.mapstruct.Mapper;

@Mapper
public interface AssemblyMapper {

    Assembly requestToEntity(AssemblyRequestDto requestDto);

    Assembly toEntity(AssemblyDto request);

    AssemblyDto toDto(Assembly entity);

}
