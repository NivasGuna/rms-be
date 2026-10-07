package com.arigs.rms.mapper;

import com.arigs.rms.dto.response.UserResponse;
import com.arigs.rms.entity.AppUser;
import org.mapstruct.Mapper;

/**
 * Maps user entities to DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(AppUser user);
}
