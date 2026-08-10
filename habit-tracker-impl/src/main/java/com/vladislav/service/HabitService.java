package com.vladislav.service;

import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;

import java.util.List;

public interface HabitService {

    HabitRs save(HabitRq habitRq);

    HabitRs findById(Long id);

    List<HabitRs> findAll();

    HabitRs update(Long id, UpdatedHabitRq updatedHabitRq);

    void delete(Long id);

}
