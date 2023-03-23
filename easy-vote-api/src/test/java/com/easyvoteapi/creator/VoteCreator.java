package com.easyvoteapi.creator;

import com.easyvoteapi.entities.Vote;
import com.github.javafaker.Faker;

public class VoteCreator {

    public static Vote createFakeRequest() {
        var faker = new Faker();

        return Vote.builder()
                .vote(faker.bool().bool())
                .build();
    }
}
