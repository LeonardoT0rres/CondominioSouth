package com.easyvoteapi.creator;

import com.easyvoteapi.entities.User;
import com.github.javafaker.Faker;

public class UserCreator {

    public static User createFakeRequest() {
        var faker = new Faker();

        return User.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .apartment(faker.number().randomDigitNotZero())
                .build();
    }
}
