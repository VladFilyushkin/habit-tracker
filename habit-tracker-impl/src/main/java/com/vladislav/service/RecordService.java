package com.vladislav.service;

import com.vladislav.dto.response.RecordRs;
import com.vladislav.entity.User;

import java.util.List;

public interface RecordService {

    RecordRs markComplete(Long id, User user);

    List<RecordRs> getAllByHabitId(Long id, User user);
}
