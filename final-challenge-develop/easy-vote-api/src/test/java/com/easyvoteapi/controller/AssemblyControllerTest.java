package com.easyvoteapi.controller;

import com.easyvoteapi.controller.exceptions.StandardError;
import com.easyvoteapi.creator.AssemblyCreator;
import com.easyvoteapi.creator.ScheduleCreator;
import com.easyvoteapi.repository.AssemblyRepository;
import com.easyvoteapi.utils.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
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
class AssemblyControllerTest {

    private final String URL = "/assemblies";
    private final String SCHEDULE_URL = "/schedules";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AssemblyRepository repository;

    @Test
    void createShouldReturnAssemblyDto() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(assembly);


        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.obs").value(assembly.getObs()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.locale").value(assembly.getLocale()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(assembly.getName()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.PENDENTE.name()));
    }

    @Test
    void createShouldReturnBadRequestWhenCreatingAssemblyWithInvalidDate() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest().withStart(LocalDateTime.now().minusDays(1));
        var jsonBody = objectMapper.writeValueAsString(assembly);


        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Disabled
    void createShouldReturnBadRequestWhenCreatingAlreadyExistingAssembly() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(assembly);


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Assembleia já cadastrada"));
    }

    @Test
    void easterEggException() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest().withName("teapot");
        var jsonBody = objectMapper.writeValueAsString(assembly);


        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is4xxClientError());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("O servidor recusa a tentativa de coar café num bule de chá"));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(assembly);


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var savedAssemblyId = repository.findByName(assembly.getName()).get().getId();

        var result = mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"), savedAssemblyId)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteShouldReturnBadRequestWhenInvalidStatus() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest().withStart(LocalDateTime.now().plusSeconds(2));
        var jsonBody = objectMapper.writeValueAsString(assembly);


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var savedAssemblyId = repository.findByName(assembly.getName()).get().getId();

        TimeUnit.SECONDS.sleep(7);

        var result = mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"), savedAssemblyId)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Status de assembleia inválido"));
    }

    @Test
    void getByIdShouldReturnAssemblyDto() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(assembly);


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = repository.findByName(assembly.getName()).get().getId();

        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/{id}"), assemblyId)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(assembly.getName()));
    }

    @Test
    void getByIdShouldReturnNotFoundWhenGettingInvalidAssembly() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/{id}"), 100)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateShouldReturnAssemblyDto() throws Exception {
        var savedAssembly = AssemblyCreator.createFakeRequest();
        var assemblyToUpdate = AssemblyCreator.createFakeRequest();


        var jsonBody = objectMapper.writeValueAsString(savedAssembly);
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var savedAssemblyId = repository.findByName(savedAssembly.getName()).get().getId();

        jsonBody = objectMapper.writeValueAsString(assemblyToUpdate);
        var result = mockMvc.perform(MockMvcRequestBuilders.put(URL.concat("/{id}"), savedAssemblyId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.locale").value(assemblyToUpdate.getLocale()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.cardinality").value(assemblyToUpdate.getCardinality()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.obs").value(assemblyToUpdate.getObs()));
    }

    @Test
    void patchFinishShouldReturnCONCLUIDAStatus() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest();
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly.withStart(LocalDateTime.now().plusSeconds(2)));

        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);

        var assemblyFinishTime = AssemblyCreator.createFakeFinishTime();
        var assemblyFinishTimeJsonBody = objectMapper.writeValueAsString(assemblyFinishTime);


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = repository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(SCHEDULE_URL.concat("/{assemblyId}"), assemblyId)
                .content(scheduleJsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        TimeUnit.SECONDS.sleep(7);

        var result = mockMvc.perform(MockMvcRequestBuilders.patch(URL.concat("/end/{id}"), assemblyId)
                .content(assemblyFinishTimeJsonBody).contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$").value("CONCLUIDA"));
    }

    @Test
    void buildStandartError() {
        var result = StandardError.builder().build();
    }
}