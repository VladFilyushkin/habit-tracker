package com.vladislav.habittrackerimpl.service;

import com.vladislav.exception.HabitNotFoundException;
import com.vladislav.exception.RecordAlreadyExistsException;
import com.vladislav.mapper.RecordMapper;
import com.vladislav.repository.HabitRepository;
import com.vladislav.repository.RecordRepository;
import com.vladislav.service.impl.RecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.random.RandomGenerator;

import static com.vladislav.habittrackerimpl.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private RecordMapper recordMapper;

    @Mock
    private HabitRepository habitRepository;

    @InjectMocks
    private RecordServiceImpl recordService;

    @Test
    void shouldMarkAsCompleted() {
        var user = getUserForUnit();
        var habit = getHabitWithIdForUnit();
        var record = getRecordForUnit();
        var recordRs = getRecordRsForUnit();

        when(habitRepository.findByIdAndUserId(habit.getId(), user.getId())).thenReturn(Optional.of(habit));
        when(recordRepository.existsByHabitIdAndDate(habit.getId(), LocalDate.now())).thenReturn(false);
        when(recordMapper.createEntity(habit)).thenReturn(record);
        when(recordRepository.save(record)).thenReturn(record);
        when(recordMapper.fromEntityToDto(record)).thenReturn(recordRs);

        var result = recordService.markComplete(habit.getId(), user);

        assertEquals(recordRs.getDate(), result.getDate());
        assertEquals(recordRs.getHabitId(), result.getHabitId());
        verify(recordRepository).save(record);
    }

    @Test
    void shouldThrowExceptionWhenHabitNotFoundById() {
        var user = getUserForUnit();
        var id = RandomGenerator.getDefault().nextLong();

        when(habitRepository.findByIdAndUserId(id, user.getId())).thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> recordService.markComplete(id, user));
    }

    @Test
    void shouldThrowExceptionWhenAlreadyMarkedToday() {
        var habit = getHabitWithIdForUnit();
        var user = getUserForUnit();

        when(habitRepository.findByIdAndUserId(habit.getId(), user.getId())).thenReturn(Optional.of(habit));
        when(recordRepository.existsByHabitIdAndDate(habit.getId(), LocalDate.now())).thenReturn(true);

        verify(recordRepository, never()).save(any());
        verifyNoInteractions(recordMapper);

        assertThrows(RecordAlreadyExistsException.class, () -> recordService.markComplete(habit.getId(), user));
    }
}
