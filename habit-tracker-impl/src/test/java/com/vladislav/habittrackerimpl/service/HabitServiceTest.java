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
class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private HabitMapper habitMapper;

    @InjectMocks
    private HabitServiceImpl habitService;

    @Test
    void shouldSaveHabitAndAssignCurrentUser() {
        var user = getUserForUnit();
        var habit = getHabitForUnit();
        var habitRq = getHabitRqForUnit();
        var habitRs = getHabitRsForUnit();

        when(habitMapper.fromDtoToEntity(habitRq)).thenReturn(habit);
        when(habitRepository.save(habit)).thenReturn(habit);
        when(habitMapper.fromEntityToDto(habit)).thenReturn(habitRs);

        var result = habitService.save(habitRq, user);

        assertEquals(habitRs.getTarget(), result.getTarget());
        assertEquals(habitRs.getName(), result.getName());
        assertEquals(habitRs.getDescription(), result.getDescription());
        verify(habitRepository).save(habit);
    }

    @Test
    void shouldFindHabitByIdForOwner() {
        var user = getUserForUnit();
        var habit = getHabitWithIdForUnit();
        var habitRs = getHabitRsForUnit();

        when(habitRepository.findByIdAndUserId(habit.getId(), user.getId())).thenReturn(Optional.of(habit));
        when(habitMapper.fromEntityToDto(habit)).thenReturn(habitRs);

        var result = habitService.findById(habit.getId(),user);

        assertEquals(habitRs.getTarget(), result.getTarget());
        assertEquals(habitRs.getName(), result.getName());
        assertEquals(habitRs.getDescription(), result.getDescription());
        verify(habitRepository).findByIdAndUserId(habit.getId(), user.getId());
    }

    @Test
    void shouldThrowExceptionWhenHabitIsNotFindById() {
        var user = getUserForUnit();
        var habit = getHabitWithIdForUnit();

        when(habitRepository.findByIdAndUserId(habit.getId(),user.getId())).thenReturn(Optional.empty());
        assertThrows(HabitNotFoundException.class, () -> habitService.findById(habit.getId(),user));
    }

    @Test
    void shouldFindAllHabitsForCurrentUserOnly() {
        var user = getUserForUnit();
        var habit = getHabitWithIdForUnit();
        var habitRs = getHabitRsForUnit();
        var habitList = List.of(habit);
        var habitRsList = List.of(habitRs);

        when(habitRepository.findAllByUserId(user.getId())).thenReturn(habitList);
        when(habitMapper.fromEntityListToDtoList(habitList)).thenReturn(habitRsList);

        var result = habitService.findAll(user);

        assertEquals(habitRsList.size(), result.size());
        assertEquals(habitRs.getDescription(), result.getFirst().getDescription());
        assertEquals(habitRs.getName(), result.getFirst().getName());
        assertEquals(habitRs.getTarget(), result.getFirst().getTarget());
        verify(habitRepository).findAllByUserId(user.getId());
    }

    @Test
    void shouldUpdateHabitForCurrentUser() {
        var user = getUserForUnit();
        var habit = getHabitWithIdForUnit();
        var updatedHabitRq = getUpdatedHabitRqForUnit();
        var habitRs = HabitRs.builder().name("new name").target(8).description("new description").build();

        when(habitRepository.findByIdAndUserId(habit.getId(), user.getId())).thenReturn(Optional.of(habit));
        when(habitRepository.save(habit)).thenReturn(habit);
        when(habitMapper.fromEntityToDto(habit)).thenReturn(habitRs);

        var result = habitService.update(habit.getId(), updatedHabitRq, user);

        assertEquals(updatedHabitRq.getTarget(), result.getTarget());
        assertEquals(updatedHabitRq.getName(), result.getName());
        assertEquals(updatedHabitRq.getDescription(), result.getDescription());
        verify(habitMapper).updateFromDto(updatedHabitRq, habit);
        verify(habitRepository).save(habit);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingHabitIsNotFoundById() {
        var id = RandomGenerator.getDefault().nextLong();
        var user = getUserForUnit();
        var updatedHabitRq = getUpdatedHabitRqForUnit();

        when(habitRepository.findByIdAndUserId(id, user.getId())).thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitService.update(id, updatedHabitRq,user));
        verify(habitRepository, never()).save(any());
    }

    @Test
    void shouldDeleteHabitForUser() {

        var user = getUserForUnit();
        var habit = getHabitWithIdForUnit();

        when(habitRepository.existsByIdAndUserId(habit.getId(), user.getId())).thenReturn(true);

        habitService.delete(habit.getId(), user);
        verify(habitRepository).deleteById(habit.getId());
    }

    @Test
    void shouldThrowExceptionWhenDeletingHabitIsNotFound() {
        var user = getUserForUnit();
        var id = RandomGenerator.getDefault().nextLong();

        when(habitRepository.existsByIdAndUserId(id, user.getId())).thenReturn(false);

        assertThrows(HabitNotFoundException.class, () -> habitService.delete(id, user));
    }
}
