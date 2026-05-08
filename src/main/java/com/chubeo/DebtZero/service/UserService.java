package com.chubeo.DebtZero.service;

import com.chubeo.DebtZero.dto.request.UserCreationRequest;
import com.chubeo.DebtZero.dto.response.UserCreationResponse;
import com.chubeo.DebtZero.dto.response.UserResponse;
import com.chubeo.DebtZero.entity.Role;
import com.chubeo.DebtZero.entity.User;
import com.chubeo.DebtZero.exception.AppException;
import com.chubeo.DebtZero.exception.ErrorCode;
import com.chubeo.DebtZero.mapper.UserMapper;
import com.chubeo.DebtZero.repository.RoleRepository;
import com.chubeo.DebtZero.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserCreationResponse createUser(UserCreationRequest request){
        if(userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        if (userRepository.existsByphoneNumber(request.getPhoneNumber()))
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISED);

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User user = userMapper.toUser(request);
        user.setRoles(new HashSet<>(Set.of(role)));
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserCreationResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

}
