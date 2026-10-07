package com.arigs.rms.service.impl;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.UserCreateRequest;
import com.arigs.rms.dto.request.UserUpdateRequest;
import com.arigs.rms.dto.response.UserResponse;
import com.arigs.rms.entity.AppUser;
import com.arigs.rms.exception.DuplicateResourceException;
import com.arigs.rms.exception.ResourceNotFoundException;
import com.arigs.rms.mapper.UserMapper;
import com.arigs.rms.repository.AppUserRepository;
import com.arigs.rms.service.UserService;
import jakarta.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Default user administration service.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse create(UserCreateRequest request) {
        ensureUniqueBusinessKeys(request.username(), request.email(), request.employeeCode());
        AppUser user = new AppUser();
        user.setUsername(request.username().trim());
        user.setEmail(request.email().trim().toLowerCase(Locale.ROOT));
        user.setEmployeeCode(request.employeeCode().trim());
        user.setFullName(request.fullName().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setPasswordChangedAt(Instant.now());
        user.setPasswordExpiresAt(Instant.now().plusSeconds(90L * 24 * 60 * 60));
        user.setRoles(request.roles());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse update(UUID id, UserUpdateRequest request) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setEmail(request.email().trim().toLowerCase(Locale.ROOT));
        user.setFullName(request.fullName().trim());
        user.setRoles(request.roles());
        user.setActive(request.active());
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse get(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> search(String keyword, Pageable pageable) {
        return PageResponse.from(userRepository.findAll(searchSpec(keyword), pageable).map(userMapper::toResponse));
    }

    private void ensureUniqueBusinessKeys(String username, String email, String employeeCode) {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (userRepository.existsByEmployeeCodeIgnoreCase(employeeCode)) {
            throw new DuplicateResourceException("Employee code already exists");
        }
    }

    private Specification<AppUser> searchSpec(String keyword) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (StringUtils.hasText(keyword)) {
                String like = "%" + keyword.toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("username")), like),
                        cb.like(cb.lower(root.get("email")), like),
                        cb.like(cb.lower(root.get("employeeCode")), like),
                        cb.like(cb.lower(root.get("fullName")), like)));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
