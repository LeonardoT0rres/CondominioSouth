package com.easyvoteapi.controller;

import com.easyvoteapi.creator.AssemblyCreator;
import com.easyvoteapi.creator.ScheduleCreator;
import com.easyvoteapi.repository.AssemblyRepository;
import com.easyvoteapi.repository.ScheduleRepository;
import com.easyvoteapi.utils.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@AutoConfigureMockMvc
class ScheduleControllerTest {

    private final String URL = "/schedules";
    private final String ASSEMBLY_URL = "/assemblies";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ScheduleRepository repository;
    @Autowired
    private AssemblyRepository assemblyRepository;

    @Test
    void createShouldReturnScheduleDto() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest();
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);
        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);


        var assemblyResult = mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        assemblyResult.andDo(MockMvcResultHandlers.print());

        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(scheduleJsonBody).contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(schedule.getName()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.PENDENTE.name()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.scheduleOrder").value(schedule.getScheduleOrder()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(schedule.getDescription()));
    }

    @Test
    void createShouldReturnNotFoundWhenInsertingInInvalidAssembly() throws Exception {
        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);


        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), 3)
                .content(scheduleJsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().isNotFound());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Assembleia não encontrada"));
    }

    @Test
    void createShouldReturnBadRequestWhenCreatingAlreadyExistingSchedule() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest();
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);

        var schedule = ScheduleCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(schedule);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Pauta já cadastrada"));
    }

    @Test
    void findAllShouldReturnScheduleDtoList() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest();
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);

        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var assemblyOpt = assemblyRepository.findByName(assembly.getName());

        var assemblyId = assemblyOpt.get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(scheduleJsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/assembly/{id}"), assemblyId)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(schedule.getName()));
    }

    @Test
    void findScheduleByIdShouldReturnScheduleDto() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest();
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);

        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(scheduleJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var scheduleId = repository.findByName(schedule.getName()).get().getId();

        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/{assemblyId}/{scheduleId}"), assemblyId, scheduleId)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(schedule.getName()));
    }

    @Test
    void findAllShouldReturnNotFoundWhenInvalidAssembly() throws Exception {
        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);


        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), 1)
                .content(scheduleJsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/assembly/{id}"), 77)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().isNotFound());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Assembleia não encontrada"));
    }

    @Test
    void findByIdShouldReturnNotFoundWhenAssemblyIsInvalid() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest();
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);

        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(scheduleJsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/{assemblyId}/{scheduleId}"), 40, 1)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().isNotFound());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Assembleia não encontrada"));
    }

    @Test
    void findByIdShouldReturnNotFoundWhenScheduleIsInvalid() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest();
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);

        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(scheduleJsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/{assemblyId}/{scheduleId}"), assemblyId, 47)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().isNotFound());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Pauta não encontrada"));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        var schedule = ScheduleCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(schedule);

        var assembly = AssemblyCreator.createFakeRequest();
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var scheduleId = repository.findByName(schedule.getName()).get().getId();

        var result = mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"), scheduleId)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteShouldReturnBadRequestWhenInvalidAssemblyStatus() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest().withStart(LocalDateTime.now().plusSeconds(2));
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);

        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);

        var scheduleStartTime = ScheduleCreator.createFakeStartTime();
        var scheduleStartTimeJsonBody = objectMapper.writeValueAsString(scheduleStartTime);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(scheduleJsonBody).contentType(MediaType.APPLICATION_JSON));
        var scheduleId = repository.findByName(schedule.getName()).get().getId();

        TimeUnit.SECONDS.sleep(7);

        mockMvc.perform(MockMvcRequestBuilders.patch(URL.concat("/start/{id}"), scheduleId)
                .content(scheduleStartTimeJsonBody).contentType(MediaType.APPLICATION_JSON));

        TimeUnit.SECONDS.sleep(5);

        var result = mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"), scheduleId)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Status de assembleia inválido"));
    }

    @Test
    void deleteShouldReturnBadRequestWhenInvalidScheduleStatus() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest().withStart(LocalDateTime.now().plusSeconds(2));
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);

        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(scheduleJsonBody).contentType(MediaType.APPLICATION_JSON));
        var scheduleId = repository.findByName(schedule.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"), scheduleId)
                .contentType(MediaType.APPLICATION_JSON));

        var result = mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"), scheduleId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Status de pauta inválido"));
    }

    @Test
    void deleteShouldReturnNotFoundWhenGettingInvalidSchedule() throws Exception {
        var schedule = ScheduleCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(schedule.withId(1L));

        var assembly = AssemblyCreator.createFakeRequest();
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var result = mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"), 90)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Pauta não encontrada"));
    }

    @Test
    void updateShouldReturnScheduleDto() throws Exception {
        var savedSchedule = ScheduleCreator.createFakeRequest();
        var scheduleToUpdate = ScheduleCreator.createFakeRequest();


        var assembly = AssemblyCreator.createFakeRequest();
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);
        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var savedAssemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        var jsonBody = objectMapper.writeValueAsString(savedSchedule);
        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), savedAssemblyId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var savedScheduleId = repository.findByName(savedSchedule.getName()).get().getId();

        jsonBody = objectMapper.writeValueAsString(scheduleToUpdate);
        var result = mockMvc.perform(MockMvcRequestBuilders.put(URL.concat("/{id}"), savedScheduleId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.scheduleOrder").value(scheduleToUpdate.getScheduleOrder()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(scheduleToUpdate.getDescription()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(scheduleToUpdate.getName()));
    }

    @Test
    void patchStartShouldReturnANDAMENTOStatus() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest().withStart(LocalDateTime.now().plusSeconds(2));
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);

        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);

        var scheduleStartTime = ScheduleCreator.createFakeStartTime();
        var scheduleStartTimeJsonBody = objectMapper.writeValueAsString(scheduleStartTime);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{assemblyId}"), assemblyId)
                .content(scheduleJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var scheduleId = repository.findByName(schedule.getName()).get().getId();

        TimeUnit.SECONDS.sleep(7);

        var result = mockMvc.perform(MockMvcRequestBuilders.patch(URL.concat("/start/{id}"), scheduleId)
                .content(scheduleStartTimeJsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$").value("ANDAMENTO"));
    }
}