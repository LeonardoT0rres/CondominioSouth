package com.easyvoteapi.controller;

import com.easyvoteapi.creator.AssemblyCreator;
import com.easyvoteapi.creator.ScheduleCreator;
import com.easyvoteapi.creator.VoteCreator;
import com.easyvoteapi.repository.AssemblyRepository;
import com.easyvoteapi.repository.ScheduleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
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
public class VoteControllerTest {

    private final String URL = "/votes";
    private final String ASSEMBLY_URL = "/assemblies";
    private final String SCHEDULE_URL = "/schedules";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private AssemblyRepository assemblyRepository;

    @Test
    void createShouldReturnVoteDto() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest().withStart(LocalDateTime.now().plusSeconds(2));
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);

        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);

        var scheduleStartTime = ScheduleCreator.createFakeStartTime();
        var scheduleStartTimeJsonBody = objectMapper.writeValueAsString(scheduleStartTime);

        var vote = VoteCreator.createFakeRequest();
        var voteJsonBody = objectMapper.writeValueAsString(vote);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(SCHEDULE_URL.concat("/{assemblyId}"), assemblyId)
                .content(scheduleJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var scheduleId = scheduleRepository.findByName(schedule.getName()).get().getId();

        TimeUnit.SECONDS.sleep(7);

        mockMvc.perform(MockMvcRequestBuilders.patch(SCHEDULE_URL.concat("/start/{id}"), scheduleId)
                .content(scheduleStartTimeJsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{scheduleId}/{userId}"), scheduleId, new Faker().number().numberBetween(1, 3))
                .content(voteJsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.voteMessage").value("Voto computado com sucesso!"));
    }

    @Test
    void createShouldReturnBadRequestWhenVotingAgain() throws Exception {
        var assembly = AssemblyCreator.createFakeRequest().withStart(LocalDateTime.now().plusSeconds(2));
        var assemblyJsonBody = objectMapper.writeValueAsString(assembly);

        var schedule = ScheduleCreator.createFakeRequest();
        var scheduleJsonBody = objectMapper.writeValueAsString(schedule);

        var scheduleStartTime = ScheduleCreator.createFakeStartTime();
        var scheduleStartTimeJsonBody = objectMapper.writeValueAsString(scheduleStartTime);

        var vote = VoteCreator.createFakeRequest();
        var voteJsonBody = objectMapper.writeValueAsString(vote);


        mockMvc.perform(MockMvcRequestBuilders.post(ASSEMBLY_URL)
                .content(assemblyJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var assemblyId = assemblyRepository.findByName(assembly.getName()).get().getId();

        mockMvc.perform(MockMvcRequestBuilders.post(SCHEDULE_URL.concat("/{assemblyId}"), assemblyId)
                .content(scheduleJsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var scheduleId = scheduleRepository.findByName(schedule.getName()).get().getId();

        TimeUnit.SECONDS.sleep(7);

        mockMvc.perform(MockMvcRequestBuilders.patch(SCHEDULE_URL.concat("/start/{id}"), scheduleId)
                .content(scheduleStartTimeJsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        TimeUnit.SECONDS.sleep(5);

        var userId = new Faker().number().numberBetween(1, 3);

        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{scheduleId}/{userId}"), scheduleId, userId)
                .content(voteJsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/{scheduleId}/{userId}"), scheduleId, userId)
                .content(voteJsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Voto já computado"));
    }
}
