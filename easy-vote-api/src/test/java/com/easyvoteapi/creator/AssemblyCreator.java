package com.easyvoteapi.creator;

import com.easyvoteapi.dto.AssemblyFinishRequestDto;
import com.easyvoteapi.entities.Assembly;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;

public class AssemblyCreator {

    public static Assembly createFakeRequest() {
        var faker = new Faker();

        return Assembly.builder()
                .name(faker.name().title())
                .obs(faker.company().catchPhrase())
                .locale(faker.space().planet())
                .start(LocalDateTime.now().plusYears(faker.number().numberBetween(1, 24)))
                .cardinality(String.valueOf(faker.number().numberBetween(1, 100)))
                .build();
    }

    public static AssemblyFinishRequestDto createFakeFinishTime() {
        return AssemblyFinishRequestDto.builder()
                .endTime(LocalDateTime.now().plusSeconds(10))
                .build();
    }
}
