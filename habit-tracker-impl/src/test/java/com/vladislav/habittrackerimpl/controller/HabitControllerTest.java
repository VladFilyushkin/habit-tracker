package com.vladislav.habittrackerimpl.controller;

import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.entity.Habit;
import com.vladislav.mapper.HabitMapper;
import com.vladislav.repository.HabitRepository;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.random.RandomGenerator;

import static com.vladislav.constant.MessageConstant.HABIT_NOT_FOUND_EXCEPTION;
import static com.vladislav.habittrackerimpl.service.TestData.getHabitRqBody;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HabitControllerTest extends AbstractIntegrationControllerTest {

    @Autowired
    private HabitMapper habitMapper;

    @Autowired
    private HabitRepository habitRepository;


    @BeforeEach
    void beforeEach() {
        habitRepository.deleteAll();
    }

    @SneakyThrows
    private Habit saveHabit() {
        return habitRepository.save(habitMapper.fromDtoToEntity(getHabitRqBody()));
    }

    @Test
    @SneakyThrows
    void shouldSaveHabitSuccessfully() {
        var request = getHabitRqBody();
        var response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/habits")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(HabitRs.class);

        var saved = habitRepository.findById(response.getId());
        assertThat(saved).isPresent();
        var existing = saved.get();
        assertEquals(request.getDescription(), existing.getDescription());
        assertEquals(request.getName(), existing.getName());
        assertEquals(request.getTarget(), existing.getTarget());

        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getTarget(), response.getTarget());
    }

    @Test
    void shouldFindHabitByIdSuccessfully() {
        var saved = saveHabit();
        var response = given()
                .when()
                .get("/api/habits/{id}", saved.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(HabitRs.class);

        assertEquals(saved.getName(), response.getName());
        assertEquals(saved.getDescription(), response.getDescription());
        assertEquals(saved.getTarget(), response.getTarget());
    }


    @Test
    void shouldThrowExceptionWhenHabitIsNotFoundById() {
        var randomId = RandomGenerator.getDefault().nextLong();
        var exceptionMessage = given()
                .when()
                .get("/api/habits/{id}", randomId)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .asString();

        assertEquals(String.format(HABIT_NOT_FOUND_EXCEPTION, randomId), exceptionMessage);
    }

    @Test
    void shouldFindAllHabitsSuccessfully() {
        var saved = saveHabit();
        var response = given()
                .when()
                .get("/api/habits")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(HabitRs[].class);

        assertThat(response).hasSize(1);
        assertEquals(saved.getTarget(), response[0].getTarget());
        assertEquals(saved.getName(), response[0].getName());
        assertEquals(saved.getDescription(), response[0].getDescription());
    }

    @Test
    void shouldUpdateExistingHabitSuccessfully() {
        var saved = saveHabit();
        var updated = new UpdatedHabitRq("new name", "new description", 2);
        var response = given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when()
                .put("/api/habits/{id}", saved.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(HabitRs.class);

        assertEquals(updated.getDescription(), response.getDescription());
        assertEquals(updated.getName(), response.getName());
        assertEquals(updated.getTarget(), response.getTarget());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingHabitIsNotFoundById() {
        var randomId = RandomGenerator.getDefault().nextLong();
        var updated = new UpdatedHabitRq("new name", "new description", 2);
        var exceptionMessage = given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when()
                .put("/api/habits/{id}", randomId)
                .then()
                .statusCode(404)
                .extract()
                .asString();

        assertEquals(String.format(HABIT_NOT_FOUND_EXCEPTION, randomId), exceptionMessage);
    }

    @Test
    void shouldDeleteHabitSuccessFully() {
        var saved = saveHabit();
        given()
                .when()
                .delete("/api/habits/{id}", saved.getId())
                .then()
                .statusCode(204);

        assertTrue(habitRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDeletingHabitIsNotFoundById() {
        var randomId = RandomGenerator.getDefault().nextLong();
        var exceptionMessage = given()
                .when()
                .delete("/api/habits/{id}", randomId)
                .then()
                .statusCode(404)
                .extract()
                .asString();

        assertEquals(String.format(HABIT_NOT_FOUND_EXCEPTION, randomId), exceptionMessage);

    }


}
