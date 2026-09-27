package com.vladislav.habittrackerimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.LoginRq;
import com.vladislav.dto.request.RegisterRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.dto.response.LoginRs;
import com.vladislav.dto.response.RecordRs;
import com.vladislav.dto.response.RegisterRs;
import com.vladislav.entity.Habit;
import com.vladislav.entity.Record;
import com.vladislav.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDate;

public class TestData {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String CREATE_HABIT_RQ_BODY = "bodyRq/save-habit.json";
    private static final String CREATE_REGISTER_RQ_BODY = "bodyRq/register-user.json";
    private static final String CREATE_LOGIN_RQ_BODY = "bodyRq/login-user.json";
    private static final String CREATE_SECOND_REGISTER_RQ_BODY = "bodyRq/register-user-2.json";
    private static final String CREATE_SECOND_LOGIN_RQ_BODY = "bodyRq/login-user-2.json";

    public static <T> T deserialize(String path, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(TestData.class.getClassLoader().getResourceAsStream(path), clazz);
    }

    public static HabitRq getHabitRqBody() throws IOException {
        return deserialize(CREATE_HABIT_RQ_BODY, HabitRq.class);
    }

    public static RegisterRq getRegisterRqBody() throws IOException {
        return deserialize(CREATE_REGISTER_RQ_BODY, RegisterRq.class);
    }

    public static LoginRq getLoginRqBody() throws IOException {
        return deserialize(CREATE_LOGIN_RQ_BODY, LoginRq.class);
    }

    public static RegisterRq getSecondRegisterRqBody() throws IOException {
        return deserialize(CREATE_SECOND_REGISTER_RQ_BODY, RegisterRq.class);
    }

    public static LoginRq getSecondLoginRqBody() throws IOException {
        return deserialize(CREATE_SECOND_LOGIN_RQ_BODY, LoginRq.class);
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

    public static User getUserForUnit() {
        return User.builder()
                .id(100L)
                .username("vlad213")
                .password("encodedPassword")
                .build();

    }

    public static Habit getHabitWithIdForUnit() {
        var habit = getHabitForUnit();
        ReflectionTestUtils.setField(habit, "id", 1L);
        return habit;
    }

    public static RegisterRq getRegisterRqForUnit() {
        return RegisterRq.builder()
                .username("vlad213")
                .password("test")
                .build();
    }

    public static RegisterRs getRegisterRsForUnit() {
        return RegisterRs.builder()
                .username("vlad213")
                .build();
    }

    public static LoginRq getLoginRqForUnit() {
        return LoginRq.builder()
                .username("vlad213")
                .password("test")
                .build();
    }

    public static LoginRs getLoginRsForUnit() {
        return LoginRs.builder()
                .token("lm12rpkniqi[2'tjr1-mrfml;scdkc")
                .build();
    }

}