package com.easyvoteapi.controller;

import com.easyvoteapi.creator.UserCreator;
import com.easyvoteapi.repository.UserRepository;
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

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private final String URL = "/users";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository repository;

    @Test
    void createShouldReturnUserDto() throws Exception {
        var user = UserCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(user);


        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.passwordMessage").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(user.getName()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.apartment").value(user.getApartment()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.ATIVO.name()));
    }

    @Test
    void createShouldReturnBadRequestWhenCreatingAlreadyExistingUser() throws Exception {
        var user = UserCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(user);


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Usuário já cadastrado"));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        var user = UserCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(user.withId(1L));


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var userId = repository.findByName(user.getName()).get().getId();

        var result = mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"), userId)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteShouldReturnBadRequestWhenInvalidStatus() throws Exception {
        var user = UserCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(user.withId(1L));


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"), 1)
                .contentType(MediaType.APPLICATION_JSON));

        var result = mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"), 1)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Status de usuário inválido"));
    }

    @Test
    void getAllShouldReturnUserDtoList() throws Exception {
        var user = UserCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(user);


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[3].id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[3].name").value(user.getName()));
    }

    @Test
    void getByIdShouldReturnUserDto() throws Exception {
        var user = UserCreator.createFakeRequest();
        var jsonBody = objectMapper.writeValueAsString(user.withId(1L));


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var savedUserId = repository.findByName(user.getName()).get().getId();

        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/{id}"), savedUserId)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(user.getName()));
    }

    @Test
    void getByIdShouldReturnNotFoundWhenGettingInvalidUser() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/{id}"), 10)
                .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateShouldReturnUserDto() throws Exception {
        var savedUser = UserCreator.createFakeRequest();
        var userToUpdate = UserCreator.createFakeRequest();


        var jsonBody = objectMapper.writeValueAsString(savedUser);
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));
        var savedUserId = repository.findByName(savedUser.getName()).get().getId();

        jsonBody = objectMapper.writeValueAsString(userToUpdate.withPassword("12345"));
        var result = mockMvc.perform(MockMvcRequestBuilders.put(URL.concat("/{id}"), savedUserId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userToUpdate.getName()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userToUpdate.getEmail()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.apartment").value(userToUpdate.getApartment()));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.ATIVO.name()));
    }

    @Test
    void updateShouldReturnBadRequestWhenNonRedefinedPassword() throws Exception {
        var savedUser = UserCreator.createFakeRequest();
        var userToUpdate = UserCreator.createFakeRequest();


        var jsonBody = objectMapper.writeValueAsString(savedUser);
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        var savedUserId = repository.findByName(savedUser.getName()).get().getId();

        jsonBody = objectMapper.writeValueAsString(userToUpdate.withPassword(null));
        var result = mockMvc.perform(MockMvcRequestBuilders.put(URL.concat("/{id}"), savedUserId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));


        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Senha padrão não redefinida"));
    }
}
