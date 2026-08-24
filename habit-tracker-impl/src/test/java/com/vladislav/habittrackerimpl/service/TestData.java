package com.vladislav.habittrackerimpl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladislav.dto.request.HabitRq;

import java.io.IOException;

public class TestData {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String CREATE_HABIT_RQ_BODY = "bodyRq/save-habit.json";

    public static <T> T deserialize(String path, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(TestData.class.getClassLoader().getResourceAsStream(path), clazz);
    }

    public static HabitRq getHabitRqBody() throws IOException {
        return deserialize(CREATE_HABIT_RQ_BODY, HabitRq.class);
    }
}
