package com.vladislav.habittrackerimpl.controller;

import com.vladislav.dto.response.RecordRs;
import com.vladislav.entity.Habit;
import com.vladislav.mapper.HabitMapper;
import com.vladislav.repository.HabitRepository;
import com.vladislav.repository.RecordRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.random.RandomGenerator;

import static com.vladislav.constant.MessageConstant.HABIT_NOT_FOUND_EXCEPTION;
import static com.vladislav.constant.MessageConstant.RECORD_ALREADY_EXISTS_EXCEPTION;
import static com.vladislav.habittrackerimpl.TestData.getHabitRqBody;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecordControllerTest extends AbstractIntegrationControllerTest {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private HabitMapper habitMapper;
    @Autowired
    private HabitRepository habitRepository;

    @BeforeEach
    void beforeEach() {
        habitRepository.deleteAll();
        recordRepository.deleteAll();
    }

    @SneakyThrows
    private Habit saveHabit() {
        return habitRepository.save(habitMapper.fromDtoToEntity(getHabitRqBody()));
    }

    @Test
    void shouldMarkHabitAsCompletedSuccessfully() {
        var savedHabit = saveHabit();
        var response = given()
                .when()
                .post("/api/habits/{habitId}/records", savedHabit.getId())
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(RecordRs.class);

        assertEquals(savedHabit.getId(), response.getHabitId());
        assertEquals(LocalDate.now(), response.getDate());
        assertThat(recordRepository.findByHabitId(response.getHabitId())).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenHabitIsNotFound() {
        var randomId = RandomGenerator.getDefault().nextLong();
        var exceptionMessage = given()
                .when()
                .post("/api/habits/{habitId}/records", randomId)
                .then()
                .statusCode(404)
                .extract()
                .asString();

        assertEquals(String.format(HABIT_NOT_FOUND_EXCEPTION, randomId), exceptionMessage);
        assertThat(recordRepository.findByHabitId(randomId).isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenHabitAlreadyMarkedToday() {
        var savedHabit = saveHabit();
        given()
                .when()
                .post("/api/habits/{habitId}/records", savedHabit.getId())
                .then()
                .statusCode(201);

        var exceptionMessage = given()
                .when()
                .post("/api/habits/{habitId}/records", savedHabit.getId())
                .then()
                .statusCode(409)
                .extract()
                .asString();

        assertEquals(String.format(RECORD_ALREADY_EXISTS_EXCEPTION, savedHabit.getId()), exceptionMessage);
        assertThat(recordRepository.findByHabitId(savedHabit.getId())).hasSize(1);
    }

    @Test
    void shouldFindAllRecordByHabitIdSuccessfully() {
        var savedHabit = saveHabit();

        given()
                .when()
                .post("/api/habits/{habitId}/records", savedHabit.getId())
                .then()
                .statusCode(201);

        var response = given()
                .when()
                .get("/api/habits/{habitId}/records", savedHabit.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(RecordRs[].class);

        assertThat(response).hasSize(1);
        assertEquals(savedHabit.getId(), response[0].getHabitId());
        assertEquals(LocalDate.now(), response[0].getDate());


    }
}
