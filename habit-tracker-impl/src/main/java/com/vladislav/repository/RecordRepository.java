package com.vladislav.repository;

import com.vladislav.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    boolean existsByHabitIdAndDate(Long habitId, LocalDate date);

    List<Record> findByHabitId(Long habitId);
}
