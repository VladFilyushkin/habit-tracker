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
import org.springframework.test.util.ReflectionTestUtils;

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

        var id = 1L;
        var habit = getHabitForUnit();
        ReflectionTestUtils.setField(habit, "id", id);

        var record = getRecordForUnit();
        var recordRs = getRecordRsForUnit();

        when(habitRepository.findById(id)).thenReturn(Optional.of(habit));
        when(recordRepository.existsByHabitIdAndDate(id, LocalDate.now())).thenReturn(false);
        when(recordMapper.createEntity(habit)).thenReturn(record);
        when(recordRepository.save(record)).thenReturn(record);
        when(recordMapper.fromEntityToDto(record)).thenReturn(recordRs);

        var result = recordService.markComplete(id);

        assertEquals(recordRs.getDate(), result.getDate());
        assertEquals(recordRs.getHabitId(), result.getHabitId());
        verify(recordRepository).save(record);
    }

    @Test
    void shouldThrowExceptionWhenHabitNotFoundById() {
        var id = RandomGenerator.getDefault().nextLong();

        when(habitRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> recordService.markComplete(id));
    }

    @Test
    void shouldThrowExceptionWhenAlreadyMarkedToday() {

        var id = 1L;
        var habit = getHabitForUnit();
        ReflectionTestUtils.setField(habit, "id", id);

        when(habitRepository.findById(id)).thenReturn(Optional.of(habit));
        when(recordRepository.existsByHabitIdAndDate(id, LocalDate.now())).thenReturn(true);

        assertThrows(RecordAlreadyExistsException.class, () -> recordService.markComplete(id));
    }
}
