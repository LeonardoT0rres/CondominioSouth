package com.easyvoteapi.controller;

import com.easyvoteapi.dto.VoteDto;
import com.easyvoteapi.dto.VoteRequestDto;
import com.easyvoteapi.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService service;

    @PostMapping("/{scheduleId}/{userId}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public VoteDto create(@PathVariable Long scheduleId, @PathVariable Long userId, @Valid @RequestBody VoteRequestDto voteRequestDto) {
        return this.service.create(scheduleId, userId, voteRequestDto);
    }
}
