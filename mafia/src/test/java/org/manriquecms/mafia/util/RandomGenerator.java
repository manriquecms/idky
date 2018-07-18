package org.manriquecms.mafia.util;

import com.github.javafaker.Faker;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RandomGenerator {

    private static final Faker faker = new Faker();

    public static String randomName(){
        return faker.name().fullName();
    }

    public static Date randomDate(){
        return faker.date().past(1,TimeUnit.DAYS);
    }
}
