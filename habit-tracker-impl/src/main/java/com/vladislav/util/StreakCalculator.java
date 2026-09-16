package com.vladislav.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class StreakCalculator {

    public int currentStreak(List<LocalDate> dateList) {
        int currentStreak = 0;
        LocalDate currentDate = LocalDate.now();

        for (LocalDate date : dateList) {
            if (date.equals(currentDate)) {
                currentStreak++;
                currentDate = currentDate.minusDays(1);
            } else {
                break;
            }
        }
        return currentStreak;
    }

    public int bestStreak(List<LocalDate> dateList) {
        if (dateList.isEmpty()) {
            return 0;
        }

        int bestStreak = 1;
        int currentStreak = 1;
        for (int i = 1; i < dateList.size(); i++) {
            var previousDate = dateList.get(i - 1);
            var currentDate = dateList.get(i);
            if (previousDate.plusDays(1).equals(currentDate)) {
                currentStreak++;
                bestStreak = Math.max(currentStreak, bestStreak);
            } else {
                currentStreak = 1;
            }
        }
        return bestStreak;
    }
}