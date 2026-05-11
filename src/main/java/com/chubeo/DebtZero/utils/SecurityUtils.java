package com.chubeo.DebtZero.utils;

import com.chubeo.DebtZero.entity.User;
import com.chubeo.DebtZero.exception.AppException;
import com.chubeo.DebtZero.exception.ErrorCode;
import com.chubeo.DebtZero.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityUtils {
    UserRepository userRepository;

    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public String getCurrentUsername(){
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
