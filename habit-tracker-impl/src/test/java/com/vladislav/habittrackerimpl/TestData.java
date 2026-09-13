package com.vladislav.habittrackerimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.dto.response.RecordRs;
import com.vladislav.entity.Habit;
import com.vladislav.entity.Record;

import java.io.IOException;
import java.time.LocalDate;

public class TestData {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String CREATE_HABIT_RQ_BODY = "bodyRq/save-habit.json";

    public static <T> T deserialize(String path, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(TestData.class.getClassLoader().getResourceAsStream(path), clazz);
    }

    public static HabitRq getHabitRqBody() throws IOException {
        return deserialize(CREATE_HABIT_RQ_BODY, HabitRq.class);
    }

    public static HabitRq getHabitRqForUnit() {
        return HabitRq.builder()
                .name("Jogging")
                .target(7)
                .description("4 kilometres every day")
                .build();
    }

    public static Habit getHabitForUnit() {
        return Habit.builder()
                .name("Jogging")
                .target(7)
                .description("4 kilometres every day")
                .build();
    }

    public static HabitRs getHabitRsForUnit() {
        return HabitRs.builder()
                .name("Jogging")
                .target(7)
                .description("4 kilometres every day")
                .build();
    }

    public static UpdatedHabitRq getUpdatedHabitRqForUnit() {
        return UpdatedHabitRq.builder()
                .name("new name")
                .target(8)
                .description("new description")
                .build();
    }

    public static Record getRecordForUnit() {
        return Record.builder()
                .date(LocalDate.now())
                .build();
    }

    public static RecordRs getRecordRsForUnit() {
        return RecordRs.builder()
                .id(1L)
                .habitId(1L)
                .date(LocalDate.now())
                .build();
    }
}
