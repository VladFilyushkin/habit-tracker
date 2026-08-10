package com.vladislav.service;

import com.vladislav.dto.response.RecordRs;

import java.util.List;

public interface RecordService {

    RecordRs markComplete(Long id);
    List<RecordRs> getAllByHabitId(Long id);


}
