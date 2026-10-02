package com.vladislav.mapper;

import com.vladislav.dto.response.RecordRs;
import com.vladislav.entity.Habit;
import com.vladislav.entity.Record;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecordMapper {
    @Mapping(target = "habit", source = "habit")
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())")
    Record createEntity(Habit habit);

    @Mapping(target = "habitId", source = "habit.id")
    RecordRs fromEntityToDto(Record record);

    @Mapping(target = "habitId", source = "habit.id")
    List<RecordRs> fromListEntityToListDto(List<Record> recordList);
}
