package com.easyvoteapi.utils.mappers;

import com.easyvoteapi.dto.VoteDto;
import com.easyvoteapi.dto.VoteRequestDto;
import com.easyvoteapi.entities.Vote;
import org.mapstruct.Mapper;

@Mapper
public interface VoteMapper {
    Vote toEntity(VoteDto voteDto);

    VoteDto toDto(Vote vote);

    Vote requestToEntity(VoteRequestDto voteRequestDto);
}
