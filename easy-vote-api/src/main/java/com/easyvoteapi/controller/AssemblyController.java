package com.easyvoteapi.controller;

import com.easyvoteapi.dto.AssemblyDto;
import com.easyvoteapi.dto.AssemblyFinishRequestDto;
import com.easyvoteapi.dto.AssemblyRequestDto;
import com.easyvoteapi.service.AssemblyService;
import com.easyvoteapi.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/assemblies", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssemblyController {

    private final AssemblyService service;

    @PreAuthorize("hasAnyRole('SINDICO')")
    @PostMapping
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public AssemblyDto create(@Valid @RequestBody AssemblyRequestDto assemblyRequestDto) {
        return this.service.create(assemblyRequestDto);
    }

    @PreAuthorize("hasAnyRole('SINDICO', 'CONDOMINO')")
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public AssemblyDto findById(@PathVariable Long id) {
        return this.service.findById(id);
    }

    @PreAuthorize("hasAnyRole('SINDICO', 'CONDOMINO')")
    @GetMapping
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<AssemblyDto> findAll() {
        return this.service.findAll();
    }

    @PreAuthorize("hasAnyRole('SINDICO')")
    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public AssemblyDto update(@PathVariable Long id, @RequestBody AssemblyRequestDto assemblyRequestDto) {
        return this.service.update(id, assemblyRequestDto);
    }

    @PreAuthorize("hasAnyRole('SINDICO')")
    @PatchMapping("/end/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public Status patchFinish(@PathVariable Long id, @RequestBody AssemblyFinishRequestDto assemblyFinishRequestDto) {
        return this.service.patchFinish(id, assemblyFinishRequestDto);
    }

    @PreAuthorize("hasAnyRole('SINDICO')")
    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }
}
