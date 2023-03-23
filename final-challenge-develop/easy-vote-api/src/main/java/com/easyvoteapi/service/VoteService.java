package com.easyvoteapi.service;

import com.easyvoteapi.dto.VoteDto;
import com.easyvoteapi.dto.VoteRequestDto;
import com.easyvoteapi.repository.ScheduleRepository;
import com.easyvoteapi.repository.UserRepository;
import com.easyvoteapi.repository.VoteRepository;
import com.easyvoteapi.service.exceptions.AlreadyVotedException;
import com.easyvoteapi.service.exceptions.InvalidScheduleStatusException;
import com.easyvoteapi.service.exceptions.ScheduleNotFoundException;
import com.easyvoteapi.service.exceptions.UserNotFoundException;
import com.easyvoteapi.utils.constants.MapperConstants;
import com.easyvoteapi.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository repository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public VoteDto create(Long scheduleId, Long userId, VoteRequestDto voteRequestDto) {
        var vote = MapperConstants.voteMapper.requestToEntity(voteRequestDto);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário de id " + userId + " não encontrado!"));

        var schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("Pauta de id " + scheduleId + " não encontrada!"));

        if (schedule.getStatus() != Status.ANDAMENTO)
            throw new InvalidScheduleStatusException("Só é possível votar em pautas em andamento!");

        var voteCheck = repository.findByUserIdAndScheduleId(userId, scheduleId);
        if (voteCheck.isPresent())
            throw new AlreadyVotedException("Seu voto nesta pauta já foi computado!");

        vote.setUserId(userId);
        vote.setScheduleId(scheduleId);

        log.info("Voto '" + vote.getVote() + "' do usuário '" + user.getName() + "' computado com sucesso!");

        return MapperConstants.voteMapper.toDto(repository.save(vote))
                .withVoteMessage("Voto computado com sucesso!");
    }
}
