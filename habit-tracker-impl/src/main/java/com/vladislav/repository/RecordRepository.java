package com.vladislav.repository;

import com.vladislav.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findByHabitId(Long habitId);

    List<Record> findByHabitIdOrderByDateDesc(Long habitId);

    List<Record> findByDate(LocalDate date);

    @Query("SELECT r FROM Record r WHERE r.habit.user.id = :userId AND r.date BETWEEN :startDate and :endDate   ")
    List<Record> findByUserIdAndDateBetween(@Param("userId") Long userId,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);

    int countByHabitId(Long habitId);

    int countByHabitIdAndDateBetween(Long habitId, LocalDate startDate, LocalDate endDate);

    boolean existsByHabitIdAndDate(Long habitId, LocalDate date);


}
