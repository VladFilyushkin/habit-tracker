package com.vladislav.mapper;

import com.vladislav.dto.request.HabitRq;
import com.vladislav.dto.request.UpdatedHabitRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.entity.Habit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HabitMapper {
    HabitRs fromEntityToDto(Habit habit);
    Habit fromDtoToEntity(HabitRq habitRq);
    List<HabitRs> fromEntityListToDtoList(List<Habit> habits);
    void updateFromDto(UpdatedHabitRq habitUpdateRq, @MappingTarget Habit habit);
}
