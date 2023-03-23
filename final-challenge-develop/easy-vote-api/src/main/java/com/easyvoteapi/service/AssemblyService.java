package com.easyvoteapi.service;

import com.easyvoteapi.dto.AssemblyDto;
import com.easyvoteapi.dto.AssemblyFinishRequestDto;
import com.easyvoteapi.dto.AssemblyRequestDto;
import com.easyvoteapi.repository.AssemblyRepository;
import com.easyvoteapi.repository.ScheduleRepository;
import com.easyvoteapi.service.exceptions.*;
import com.easyvoteapi.utils.constants.MapperConstants;
import com.easyvoteapi.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssemblyService {

    private final AssemblyRepository repository;

    private final ScheduleRepository scheduleRepository;

    public AssemblyDto create(AssemblyRequestDto assemblyRequestDto) {
        if (assemblyRequestDto.getName().equalsIgnoreCase("teapot"))
            throw new EasterEggException("Parabéns, me descobriu!!! (obs.: a assembleia não foi criada)");

        var optAssembly = repository.findByLocaleAndStartAndStatusIsNot
                (assemblyRequestDto.getLocale(), assemblyRequestDto.getStart(), Status.INATIVO);

        if (optAssembly.isPresent())
            throw new AssemblyAlreadyExistsException("Assembleia já cadastrada, os campos 'Local' e/ou 'Data e Horário' já estão sendo utilizados!");

        if (assemblyRequestDto.getStart().isBefore(LocalDateTime.now()))
            throw new InvalidAssemblyStartException("A data de início da assembleia deve ser futura!");

        var result = MapperConstants.assemblyMapper
                .toDto(repository.save(MapperConstants.assemblyMapper
                        .requestToEntity(assemblyRequestDto)
                        .withStatus(Status.PENDENTE)));

        log.info("Assembleia '" + assemblyRequestDto.getName() + "' criada!");

        return result;
    }

    public List<AssemblyDto> findAll() {
        var result = repository.findAll()
                .stream()
                .map(assembly -> MapperConstants.assemblyMapper.toDto(assembly).withSchedules(null))
                .collect(Collectors.toList());

        log.info("Lista de assembleias retornada!");

        return result;
    }

    public AssemblyDto findById(Long id) {
        var assembly = repository.findById(id)
                .orElseThrow(() -> new AssemblyNotFoundException("Assembleia de id " + id + " não encontrada!"));

        var schedules = scheduleRepository.findAllByAssemblyId(id)
                .stream()
                .map(MapperConstants.scheduleMapper::toDto)
                .collect(Collectors.toList());

        var result = MapperConstants.assemblyMapper
                .toDto(assembly)
                .withSchedules(schedules);

        log.info("Assembleia '" + assembly.getName() + "' retornada!");

        return result;
    }

    public AssemblyDto update(Long id, AssemblyRequestDto assemblyRequestDto) {
        var assembly = repository.findByIdAndStatus(id, Status.PENDENTE)
                .orElseThrow(() -> new AssemblyNotFoundException("Não há assembleia de id " + id + " pendente!"));

        var assemblyCheck = repository.findByLocaleAndStartAndStatusIsNotAndIdIsNot
                (assemblyRequestDto.getLocale(), assemblyRequestDto.getStart(), Status.INATIVO, id);

        if (assemblyCheck.isPresent())
            throw new AssemblyAlreadyExistsException("Assembleia já cadastrada, os campos 'Local' e/ou 'Data e Horário' já estão sendo utilizados!");
        else if (assemblyRequestDto.getStart().isBefore(LocalDateTime.now()))
            throw new InvalidAssemblyStartException("A data de início da assembleia deve ser futura!");


        assembly.setName(assemblyRequestDto.getName() == null ? assembly.getName() : assemblyRequestDto.getName());
        assembly.setLocale(assemblyRequestDto.getLocale() == null ? assembly.getLocale() : assemblyRequestDto.getLocale());
        assembly.setCardinality(assemblyRequestDto.getCardinality() == null ? assembly.getCardinality() : assemblyRequestDto.getCardinality());
        assembly.setObs(assemblyRequestDto.getObs() == null ? assembly.getObs() : assemblyRequestDto.getObs());
        assembly.setStart(assemblyRequestDto.getStart() == null ? assembly.getStart() : assemblyRequestDto.getStart());

        var result = MapperConstants.assemblyMapper
                .toDto(repository.save(assembly));

        log.info("Assembleia de id " + id + " editada!");

        return result;
    }

    public Status patchFinish(Long id, AssemblyFinishRequestDto assemblyFinishRequestDto) {
        var assembly = repository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Assembleia de id " + id + " não encontrada!"));

        if (assembly.getStatus() != Status.ANDAMENTO)
            throw new InvalidAssemblyStatusException("Status '" + assembly.getStatus().name() + "' da assembleia inválido para a conclusão, o status deve ser 'ANDAMENTO'!");

        assembly.setStatus(Status.CONCLUIDA);
        assembly.setFinish(assemblyFinishRequestDto.getEndTime());

        scheduleRepository.findAllByAssemblyId(assembly.getId())
                .stream()
                .filter(schedule -> schedule.getStatus().equals(Status.ANDAMENTO))
                .findFirst()
                .ifPresent(schedule -> {
                    throw new InvalidScheduleStatusException
                            ("Status '" + assembly.getStatus().name() + "' de uma ou mais pautas inválido para a conclusão, o status deve ser diferente de 'ANDAMENTO'!");
                });

        scheduleRepository.findAllByAssemblyId(assembly.getId())
                .stream()
                .filter(schedule -> schedule.getStatus().equals(Status.PENDENTE))
                .forEach(schedule -> schedule.setStatus(Status.INATIVO));

        repository.save(assembly);

        log.info("Assembleia '" + assembly.getName() + "' encerrada!");

        return assembly.getStatus();
    }

    public void delete(Long id) {
        var assembly = repository.findById(id)
                .orElseThrow(() -> new AssemblyNotFoundException("Assembleia de id " + id + " não encontrada!"));

        if (assembly.getStatus() != Status.PENDENTE && assembly.getStatus() != Status.CONCLUIDA)
            throw new InvalidAssemblyStatusException("Status '" + assembly.getStatus().name() + "' da assembleia de id " + id + " inválido para a deleção!");

        assembly.getSchedules()
                .forEach(schedule -> schedule.setStatus(Status.INATIVO));

        assembly.setStatus(Status.INATIVO);

        repository.save(assembly);

        log.info("Assembleia '" + assembly.getName() + "' deletada!");
    }

    @Scheduled(fixedDelay = 5000)
    public void isAlreadyInProgress() {
        try {
            var assemblies = repository.findByStatus(Status.PENDENTE)
                    .orElseThrow(() -> new AssemblyNotFoundException("Não há assembléias pendentes!"));

            assemblies.stream()
                    .filter(assembly -> assembly.getStart().isBefore(LocalDateTime.now()))
                    .forEach(assembly -> {
                        assembly.setStatus(Status.ANDAMENTO);
                        log.info("Assembleia '" + assembly.getName() + "' iniciada!");
                    });

            repository.saveAll(assemblies);
        } catch (AssemblyNotFoundException ignored) {
        }
    }
}
