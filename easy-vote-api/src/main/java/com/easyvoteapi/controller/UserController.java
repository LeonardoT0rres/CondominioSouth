package com.easyvoteapi.controller;

import com.easyvoteapi.dto.UserDto;
import com.easyvoteapi.dto.UserRequestDto;
import com.easyvoteapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService service;

    @PreAuthorize("hasAnyRole('SINDICO')")
    @PostMapping
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserRequestDto userRequestDto) {
        return this.service.create(userRequestDto);
    }

    @PreAuthorize("hasAnyRole('SINDICO')")
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public UserDto findById(@PathVariable Long id) {
        return this.service.findById(id);
    }

    @PreAuthorize("hasAnyRole('SINDICO')")
    @GetMapping
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<UserDto> findAll() {
        return this.service.findAll();
    }

    @PreAuthorize("hasAnyRole('SINDICO', 'CONDOMINO')")
    @PutMapping
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public UserDto update(@RequestBody UserRequestDto userRequestDto) {
        return this.service.update(userRequestDto);
    }

    @PreAuthorize("hasAnyRole('SINDICO')")
    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }
}
