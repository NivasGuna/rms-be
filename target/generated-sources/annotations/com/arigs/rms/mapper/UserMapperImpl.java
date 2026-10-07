package com.arigs.rms.mapper;

import com.arigs.rms.dto.response.UserResponse;
import com.arigs.rms.entity.AppUser;
import com.arigs.rms.entity.Role;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-07T11:34:34+0530",
    comments = "version: 1.6.2, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 21.0.12.1 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toResponse(AppUser user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        String email = null;
        String employeeCode = null;
        String fullName = null;
        Set<Role> roles = null;
        boolean active = false;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        employeeCode = user.getEmployeeCode();
        fullName = user.getFullName();
        Set<Role> set = user.getRoles();
        if ( set != null ) {
            roles = new LinkedHashSet<Role>( set );
        }
        active = user.isActive();

        UserResponse userResponse = new UserResponse( id, username, email, employeeCode, fullName, roles, active );

        return userResponse;
    }
}
