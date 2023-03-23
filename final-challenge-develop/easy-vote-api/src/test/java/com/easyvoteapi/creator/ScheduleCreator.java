package com.easyvoteapi.creator;

import com.easyvoteapi.dto.ScheduleStartRequestDto;
import com.easyvoteapi.entities.Schedule;
import com.easyvoteapi.utils.enums.Status;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;

public class ScheduleCreator {

    public static Schedule createFakeRequest() {
        var faker = new Faker();

        return Schedule.builder()
                .name(faker.name().title())
                .description(faker.company().catchPhrase())
                .startTime(LocalDateTime.now().plusYears(faker.number().numberBetween(1, 24)))
                .endTime(LocalDateTime.now().plusYears(faker.number().numberBetween(1, 24)))
                .scheduleOrder(faker.number().randomDigit())
                .status(Status.ATIVO)
                .build();
    }

    public static ScheduleStartRequestDto createFakeStartTime() {
        return ScheduleStartRequestDto.builder()
                .startTime(LocalDateTime.now())
                .build();
    }
}
