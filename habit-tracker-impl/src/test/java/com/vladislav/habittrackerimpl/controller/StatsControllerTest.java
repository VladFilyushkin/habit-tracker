package com.vladislav.habittrackerimpl.controller;

import com.vladislav.entity.Habit;
import com.vladislav.mapper.HabitMapper;
import com.vladislav.repository.HabitRepository;
import com.vladislav.repository.RecordRepository;
import com.vladislav.entity.Record;
import com.vladislav.dto.response.HabitStatsRs;
import com.vladislav.dto.response.DailyStatsRs;
import com.vladislav.dto.response.WeeklyStatsRs;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.random.RandomGenerator;

import static com.vladislav.constant.MessageConstant.HABIT_NOT_FOUND_EXCEPTION;
import static com.vladislav.habittrackerimpl.TestData.getHabitRqBody;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatsControllerTest extends AbstractIntegrationControllerTest {

    @Autowired
    private HabitMapper habitMapper;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private RecordRepository recordRepository;

    @BeforeEach
    void beforeEach() {
        recordRepository.deleteAll();
        habitRepository.deleteAll();
    }

    @SneakyThrows
    private Habit saveHabit() {
        return habitRepository.save(habitMapper.fromDtoToEntity(getHabitRqBody()));
    }

    private void saveRecordForDate(Habit habit, LocalDate date) {
        var record = new Record();
        record.setHabit(habit);
        record.setDate(date);
        recordRepository.save(record);
    }

    @Test
    void shouldReturnHabitStatsWithCorrectStreak() {
        var habit = saveHabit();
        LocalDate today = LocalDate.now();
        saveRecordForDate(habit, today);
        saveRecordForDate(habit, today.minusDays(1));
        saveRecordForDate(habit, today.minusDays(2));

        var response = given()
                .when()
                .get("/api/habits/{id}/stats", habit.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(HabitStatsRs.class);

        assertEquals(3, response.getCurrentStreak());
        assertEquals(3, response.getBestStreak());
        assertEquals(3, response.getTotalCompletions());
    }

    @Test
    void shouldThrowExceptionWhenHabitIdNotFoundForStats() {
        var randomId = RandomGenerator.getDefault().nextLong();

        var exceptionMessage = given()
                .when()
                .get("/api/habits/{id}/stats", randomId)
                .then()
                .statusCode(404)
                .extract()
                .asString();

        assertEquals(String.format(HABIT_NOT_FOUND_EXCEPTION, randomId), exceptionMessage);
    }

    @Test
    void shouldReturnDailyStatsWithCorrectCompletionStatus() {
        var completedHabit = saveHabit();
        saveRecordForDate(completedHabit, LocalDate.now());

        var response = given()
                .when()
                .get("/api/stats/daily")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(DailyStatsRs.class);

        assertThat(response.getHabits()).hasSize(1);
        assertEquals(true, response.getHabits().getFirst().getCompleted());
    }

    @Test
    void shouldReturnWeeklyStatsWithSevenDaysAndCorrectTotalCount() {
        saveHabit();
        saveHabit();

        var response = given()
                .when()
                .get("/api/stats/week")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(WeeklyStatsRs.class);

        assertThat(response.getWeek()).hasSize(7);
        response.getWeek().forEach(day -> assertEquals(2L, day.getTotalCount()));
    }
}