package com.vladislav.service.impl;

import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.entity.Habit;
import com.vladislav.exception.HabitNotFoundException;
import com.vladislav.mapper.HabitMapper;
import com.vladislav.repository.HabitRepository;
import com.vladislav.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.vladislav.constant.MessageConstant.HABIT_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;

    @Override
    @Transactional
    public HabitRs save(HabitRq habitRq) {
        var habit = habitMapper.fromDtoToEntity(habitRq);
        Habit saved = habitRepository.save(habit);
        return habitMapper.fromEntityToDto(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public HabitRs findById(Long id) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException(String.format(HABIT_NOT_FOUND_EXCEPTION, id)));
        return habitMapper.fromEntityToDto(habit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitRs> findAll() {
        List<Habit> habitList = habitRepository.findAll();
        return habitMapper.fromEntityListToDtoList(habitList);
    }

    @Override
    @Transactional
    public HabitRs update(Long id, UpdatedHabitRq updatedHabitRq) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException(String.format(HABIT_NOT_FOUND_EXCEPTION, id)));
        habitMapper.updateFromDto(updatedHabitRq, habit);
        return habitMapper.fromEntityToDto(habitRepository.save(habit));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (habitRepository.existsById(id)) {
            habitRepository.deleteById(id);
        } else {
            throw new HabitNotFoundException(String.format(HABIT_NOT_FOUND_EXCEPTION, id));
        }
    }
}
