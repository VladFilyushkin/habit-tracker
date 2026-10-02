package com.vladislav.service;

import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.entity.User;

import java.util.List;

public interface HabitService {

    HabitRs save(HabitRq habitRq, User user);

    HabitRs findById(Long id, User user);

    List<HabitRs> findAll(User user);

    HabitRs update(Long id, UpdatedHabitRq updatedHabitRq, User user);

    void delete(Long id, User user);
}
