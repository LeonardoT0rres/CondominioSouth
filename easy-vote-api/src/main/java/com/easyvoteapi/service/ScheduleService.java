package com.easyvoteapi.service;

import com.easyvoteapi.dto.ScheduleDto;
import com.easyvoteapi.dto.ScheduleRequestDto;
import com.easyvoteapi.dto.ScheduleStartRequestDto;
import com.easyvoteapi.dto.ScheduleUpdateRequestDto;
import com.easyvoteapi.entities.Vote;
import com.easyvoteapi.repository.AssemblyRepository;
import com.easyvoteapi.repository.ScheduleRepository;
import com.easyvoteapi.repository.VoteRepository;
import com.easyvoteapi.service.exceptions.*;
import com.easyvoteapi.utils.constants.MapperConstants;
import com.easyvoteapi.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository repository;

    private final AssemblyRepository assemblyRepository;

    private final VoteRepository voteRepository;

    public ScheduleDto create(Long assemblyId, ScheduleRequestDto scheduleRequestDto) {
        var assembly = assemblyRepository.findById(assemblyId)
                .orElseThrow(() -> new AssemblyNotFoundException("Assembleia de id " + assemblyId + " não encontrada!"));
        if (assembly.getStatus() != Status.PENDENTE)
            throw new InvalidAssemblyStatusException("Status '" + assembly.getStatus().name() + "' da assembleia inválido para a criação de pauta, o status deve ser 'PENDENTE'!");

        var optSchedule = repository.findByAssemblyIdAndNameOrScheduleOrderAndStatus
                (assembly.getId(), scheduleRequestDto.getName(), scheduleRequestDto.getScheduleOrder(), Status.PENDENTE.name());

        if (optSchedule.isPresent())
            throw new ScheduleAlreadyExistsException("Pauta já cadastrada, os campos 'Título' e/ou 'Número da Pauta' já estão sendo utilizados por outra pauta nessa assembleia!");

        var schedule = MapperConstants.scheduleMapper
                .requestToEntity(scheduleRequestDto);

        schedule.setAssembly(assembly);
        schedule.setDuration(Duration.ofSeconds(60));
        schedule.setStatus(Status.PENDENTE);

        var result = MapperConstants.scheduleMapper.toDto(repository.save(schedule)).withAssemblyId(assemblyId);

        log.info("Pauta '" + scheduleRequestDto.getName() + "' criada!");

        return result;
    }

    public List<ScheduleDto> findAll(Long assemblyId) {
        var assembly = assemblyRepository.findById(assemblyId)
                .orElseThrow(() -> new AssemblyNotFoundException("Assembleia de id " + assemblyId + " não encontrada!"));

        var result = repository.findAllByAssemblyId(assemblyId)
                .stream()
                .map(MapperConstants.scheduleMapper::toDto)
                .collect(Collectors.toList());

        log.info("Lista de pautas da assembleia '" + assembly.getName() + "' retornada!");

        return result;
    }

    public ScheduleDto findById(Long assemblyId, Long scheduleId) {
        assemblyRepository.findById(assemblyId)
                .orElseThrow(() -> new AssemblyNotFoundException("Assembleia de id " + assemblyId + " não encontrada!"));

        var schedule = repository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("Pauta de id " + scheduleId + " não encontrada!"));

        if (!Objects.equals(schedule.getAssembly().getId(), assemblyId))
            throw new ScheduleNotFoundException("Pauta de id " + scheduleId + " não encontrada na assembleia de id " + assemblyId + "!");

        var result = MapperConstants.scheduleMapper
                .toDto(schedule);

        log.info("Pauta '" + schedule.getName() + "' retornada!");

        return result;
    }

    public ScheduleDto update(Long id, ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        var schedule = repository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Pauta de id " + id + " não encontrada!"));

        var assembly = schedule.getAssembly();

        var optScheduleCheck = repository.findByAssemblyIdAndNameOrScheduleOrderAndStatusAndIdIsNot
                (assembly.getId(), scheduleUpdateRequestDto.getName(), scheduleUpdateRequestDto.getScheduleOrder(), Status.PENDENTE.name(), id);
        if (optScheduleCheck.isPresent())
            throw new ScheduleAlreadyExistsException("Pauta já cadastrada, os campos 'Título' e/ou 'Número da Pauta' já estão sendo utilizados por outra pauta nessa assembleia!");


        if (assembly.getStatus() != Status.PENDENTE)
            throw new InvalidAssemblyStatusException("Status '" + assembly.getStatus().name() + "' da assembleia inválido para a edição de pauta, o status deve ser 'PENDENTE'!");
        else if (schedule.getStatus() != Status.PENDENTE)
            throw new InvalidScheduleStatusException("Status '" + schedule.getStatus().name() + "' da pauta de id " + id + " inválido para a deleção, o status deve ser 'PENDENTE'!");

        schedule.setScheduleOrder(scheduleUpdateRequestDto.getScheduleOrder() == null ? schedule.getScheduleOrder() : scheduleUpdateRequestDto.getScheduleOrder());
        schedule.setName(scheduleUpdateRequestDto.getName() == null ? schedule.getName() : scheduleUpdateRequestDto.getName());
        schedule.setDescription(scheduleUpdateRequestDto.getDescription() == null ? schedule.getDescription() : scheduleUpdateRequestDto.getDescription());

        var result = MapperConstants.scheduleMapper
                .toDto(repository.save(schedule));

        log.info("Pauta de id " + id + " editada!");

        return result;
    }

    public Status patchStart(Long id, ScheduleStartRequestDto ScheduleStartRequestDto) {
        var schedule = repository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Pauta de id " + id + " não encontrada!"));

        var assembly = assemblyRepository.findById(schedule.getAssembly()
                        .getId())
                .get();

        if (assembly.getStatus() != Status.ANDAMENTO)
            throw new InvalidAssemblyStatusException("Status '" + assembly.getStatus().name() + "' da assembleia inválido para o início de pauta, o status deve ser 'ANDAMENTO'!");
        else if (schedule.getStatus() != Status.PENDENTE)
            throw new InvalidScheduleStatusException("Status '" + schedule.getStatus().name() + "' da pauta de id " + id + " inválido para o início, o status deve ser 'PENDENTE'!");

        schedule.setStatus(Status.ANDAMENTO);
        schedule.setStartTime(ScheduleStartRequestDto.getStartTime());
        schedule.setEndTime(schedule.getStartTime()
                .plusMinutes(schedule.getDuration().getSeconds() / 60));

        repository.save(schedule);

        log.info("Pauta '" + schedule.getName() + "' iniciada!");

        return schedule.getStatus();
    }


    public void delete(Long id) {
        var schedule = repository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Pauta de id " + id + " não encontrada!"));

        var assembly = assemblyRepository.findById(schedule.getAssembly().getId()).get();

        if (assembly.getStatus() != Status.PENDENTE)
            throw new InvalidAssemblyStatusException("Status '" + assembly.getStatus().name() + "' da assembleia inválido para a deleção de pauta, o status deve ser 'PENDENTE'!");
        else if (schedule.getStatus() != Status.PENDENTE)
            throw new InvalidScheduleStatusException("Status '" + schedule.getStatus().name() + "' da pauta de id " + id + " inválido para a deleção, o status deve ser 'PENDENTE'!");

        schedule.setStatus(Status.INATIVO);

        repository.save(schedule);

        log.info("Pauta '" + schedule.getName() + "' deletada!");
    }

    @Scheduled(fixedDelay = 5000)
    public void finish() {
        try {
            var schedule = repository.findByStatus(Status.ANDAMENTO)
                    .orElseThrow(() -> new ScheduleNotFoundException("Não há nenhuma pauta em andamento!"));

            if (schedule.getEndTime().isBefore(LocalDateTime.now())) {
                var votes = voteRepository.findAll()
                        .stream()
                        .filter(vote -> Objects.equals(vote.getScheduleId(), schedule.getId()))
                        .collect(Collectors.toList());

                var positiveVote = 0;
                var negativeVote = 0;

                for (Vote vote : votes) {
                    if (vote.getVote()) {
                        ++positiveVote;
                    } else {
                        ++negativeVote;
                    }
                }

                schedule.setPositiveVotes(positiveVote);
                schedule.setNegativeVotes(negativeVote);

                if (positiveVote > negativeVote) {
                    schedule.setStatus(Status.DEFERIDO);
                } else {
                    schedule.setStatus(Status.INDEFERIDO);
                }

                log.info("Pauta '" + schedule.getName() + "' encerrada!");
                repository.save(schedule);
            }
        } catch (ScheduleNotFoundException ignored) {
        }
    }
}
