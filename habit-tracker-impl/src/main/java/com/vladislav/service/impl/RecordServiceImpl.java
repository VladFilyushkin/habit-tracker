package com.vladislav.service.impl;

import com.vladislav.dto.response.RecordRs;
import com.vladislav.entity.Habit;
import com.vladislav.entity.Record;
import com.vladislav.entity.User;
import com.vladislav.exception.HabitNotFoundException;
import com.vladislav.exception.RecordAlreadyExistsException;
import com.vladislav.mapper.RecordMapper;
import com.vladislav.repository.HabitRepository;
import com.vladislav.repository.RecordRepository;
import com.vladislav.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.vladislav.constant.MessageConstant.*;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final HabitRepository habitRepository;
    private final RecordMapper recordMapper;


    @Override
    @Transactional
    public RecordRs markComplete(Long id, User user) {
        Habit habit = habitRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new HabitNotFoundException(String.format(HABIT_NOT_FOUND_EXCEPTION, id)));

        if (recordRepository.existsByHabitIdAndDate(id, LocalDate.now())) {
            throw new RecordAlreadyExistsException(String.format(RECORD_ALREADY_EXISTS_EXCEPTION, id));
        }

        Record record = recordMapper.createEntity(habit);
        Record saved = recordRepository.save(record);
        return recordMapper.fromEntityToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordRs> getAllByHabitId(Long id, User user) {
        if (!habitRepository.existsByIdAndUserId(id, user.getId())) {
            throw new HabitNotFoundException(String.format(HABIT_NOT_FOUND_EXCEPTION, id));
        }

        List<Record> recordList = recordRepository.findByHabitId(id);
        return recordMapper.fromListEntityToListDto(recordList);
    }
}
