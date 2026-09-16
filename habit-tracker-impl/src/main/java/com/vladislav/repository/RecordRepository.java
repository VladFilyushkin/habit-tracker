package com.vladislav.repository;

import com.vladislav.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    boolean existsByHabitIdAndDate(Long habitId, LocalDate date);
    List<Record> findByHabitId(Long habitId);
    List<Record> findByHabitIdOrderByDateDesc(Long habitId);
    int countByHabitId(Long habitId);
    int countByHabitIdAndDateBetween(Long habitId, LocalDate startDate, LocalDate endDate);
    List<Record> findByDate(LocalDate date);
    List<Record> findByDateBetween(LocalDate startDate, LocalDate lastDate);

}
