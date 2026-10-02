package com.vladislav.mapper;

import com.vladislav.dto.request.RegisterRq;
import com.vladislav.dto.response.RegisterRs;
import com.vladislav.entity.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", expression ="java(passwordEncoder.encode(registerRq.getPassword()))")
    User fromDtoToEntity(RegisterRq registerRq, @Context PasswordEncoder passwordEncoder);
    @Mapping(target = "username", source = "username")
    RegisterRs fromEntityToDto(User user);
}
