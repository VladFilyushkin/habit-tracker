package com.vladislav.service.impl;

import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.entity.Habit;
import com.vladislav.entity.User;
import com.vladislav.exception.HabitNotFoundException;
import com.vladislav.mapper.HabitMapper;
import com.vladislav.repository.HabitRepository;
import com.vladislav.service.HabitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.vladislav.constant.MessageConstant.HABIT_NOT_FOUND_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;

    @Override
    @Transactional
    public HabitRs save(HabitRq habitRq, User user) {
        var habit = habitMapper.fromDtoToEntity(habitRq);
        habit.setUser(user);
        Habit saved = habitRepository.save(habit);
        log.info("Habit created with id={} and userId={}", saved.getId(), user.getId());
        return habitMapper.fromEntityToDto(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public HabitRs findById(Long id, User user) {
        log.info("Getting habit by id={} and userId={}", id, user.getId());
        Habit habit = habitRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> {
                    log.error(String.format(HABIT_NOT_FOUND_EXCEPTION, id));
                    return new HabitNotFoundException(String.format(HABIT_NOT_FOUND_EXCEPTION, id));
                });
        return habitMapper.fromEntityToDto(habit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitRs> findAll(User user) {
        log.info("Getting all habits with userId={}", user.getId());
        return habitMapper.fromEntityListToDtoList(habitRepository.findAllByUserId(user.getId()));
    }

    @Override
    @Transactional
    public HabitRs update(Long id, UpdatedHabitRq updatedHabitRq, User user) {
        log.info("Updating habit with id={} and userId={}", id, user.getId());
        Habit habit = habitRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                {
                    log.error(String.format(HABIT_NOT_FOUND_EXCEPTION, id));
                    return new HabitNotFoundException(String.format(HABIT_NOT_FOUND_EXCEPTION, id));
                });
        habitMapper.updateFromDto(updatedHabitRq, habit);
        return habitMapper.fromEntityToDto(habitRepository.save(habit));
    }

    @Override
    @Transactional
    public void delete(Long id, User user) {
        log.info("Deleting habit with id={} and userId={}", id, user.getId());
        if (!habitRepository.existsByIdAndUserId(id, user.getId())) {
            log.error(String.format(HABIT_NOT_FOUND_EXCEPTION, id));
            throw new HabitNotFoundException(String.format(HABIT_NOT_FOUND_EXCEPTION, id));
        }
        habitRepository.deleteById(id);
    }
}
