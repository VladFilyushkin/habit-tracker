package com.vladislav.habittrackerimpl.service;

import com.vladislav.dto.response.HabitRs;
import com.vladislav.exception.HabitNotFoundException;
import com.vladislav.mapper.HabitMapper;
import com.vladislav.repository.HabitRepository;
import com.vladislav.service.impl.HabitServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;

import static com.vladislav.habittrackerimpl.TestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HabitServiceITest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private HabitMapper habitMapper;

    @InjectMocks
    private HabitServiceImpl habitService;

    @Test
    void shouldSaveHabit() {
        var habit = getHabitForUnit();
        var habitRq = getHabitRqForUnit();
        var habitRs = getHabitRsForUnit();

        when(habitMapper.fromDtoToEntity(habitRq)).thenReturn(habit);
        when(habitRepository.save(habit)).thenReturn(habit);
        when(habitMapper.fromEntityToDto(habit)).thenReturn(habitRs);

        var result = habitService.save(habitRq);

        assertEquals(habitRs.getTarget(), result.getTarget());
        assertEquals(habitRs.getName(), result.getName());
        assertEquals(habitRs.getDescription(), result.getDescription());
        verify(habitRepository).save(habit);
    }

    @Test
    void shouldFindHabitById() {
        var id = RandomGenerator.getDefault().nextLong();
        var habit = getHabitForUnit();
        var habitRs = getHabitRsForUnit();

        when(habitRepository.findById(id)).thenReturn(Optional.of(habit));
        when(habitMapper.fromEntityToDto(habit)).thenReturn(habitRs);

        var result = habitService.findById(id);

        assertEquals(habitRs.getTarget(), result.getTarget());
        assertEquals(habitRs.getName(), result.getName());
        assertEquals(habitRs.getDescription(), result.getDescription());
        verify(habitRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenHabitIsNotFindById() {

        var id = RandomGenerator.getDefault().nextLong();

        when(habitRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(HabitNotFoundException.class, () -> habitService.findById(id));
    }

    @Test
    void shouldFindAll() {

        var habit = getHabitForUnit();
        var habitRs = getHabitRsForUnit();
        var habitList = List.of(habit);
        var habitRsList = List.of(habitRs);

        when(habitRepository.findAll()).thenReturn(habitList);
        when(habitMapper.fromEntityListToDtoList(habitList)).thenReturn(habitRsList);

        var result = habitService.findAll();

        assertEquals(habitRsList.size(), result.size());
        assertEquals(habitRs.getDescription(), result.getFirst().getDescription());
        assertEquals(habitRs.getName(), result.getFirst().getName());
        assertEquals(habitRs.getTarget(), result.getFirst().getTarget());
        verify(habitRepository).findAll();
    }

    @Test
    void shouldUpdateHabit() {

        var id = RandomGenerator.getDefault().nextLong();
        var habit = getHabitForUnit();
        var updatedHabitRq = getUpdatedHabitRqForUnit();
        var habitRs = HabitRs.builder().name("new name").target(8).description("new description").build();

        when(habitRepository.findById(id)).thenReturn(Optional.of(habit));
        when(habitRepository.save(habit)).thenReturn(habit);
        when(habitMapper.fromEntityToDto(habit)).thenReturn(habitRs);
        var result = habitService.update(id, updatedHabitRq);

        assertEquals(updatedHabitRq.getTarget(), result.getTarget());
        assertEquals(updatedHabitRq.getName(), result.getName());
        assertEquals(updatedHabitRq.getDescription(), result.getDescription());
        verify(habitMapper).updateFromDto(updatedHabitRq, habit);
        verify(habitRepository).save(habit);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingHabitIsNotFoundById() {

        var id = RandomGenerator.getDefault().nextLong();
        var updatedHabitRq = getUpdatedHabitRqForUnit();

        when(habitRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitService.update(id, updatedHabitRq));
    }

    @Test
    void shouldDeleteHabit() {

        var id = RandomGenerator.getDefault().nextLong();

        when(habitRepository.existsById(id)).thenReturn(true);

        habitService.delete(id);
        verify(habitRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeletingHabitIsNotFound() {

        var id = RandomGenerator.getDefault().nextLong();

        when(habitRepository.existsById(id)).thenReturn(false);

        assertThrows(HabitNotFoundException.class, () -> habitService.delete(id));
    }


}
