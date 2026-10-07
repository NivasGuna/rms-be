package com.arigs.rms.service;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.UserCreateRequest;
import com.arigs.rms.dto.request.UserUpdateRequest;
import com.arigs.rms.dto.response.UserResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * User administration use cases.
 */
public interface UserService {

    UserResponse create(UserCreateRequest request);

    UserResponse update(UUID id, UserUpdateRequest request);

    UserResponse get(UUID id);

    PageResponse<UserResponse> search(String keyword, Pageable pageable);
}
