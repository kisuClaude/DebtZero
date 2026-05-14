package com.chubeo.DebtZero.controller;

import com.chubeo.DebtZero.dto.request.UserCreationRequest;
import com.chubeo.DebtZero.dto.response.ApiResponse;
import com.chubeo.DebtZero.dto.response.UserCreationResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.chubeo.DebtZero.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/create-user")
    public ApiResponse<UserCreationResponse> createUser(@Valid @RequestBody UserCreationRequest request){
        return ApiResponse.<UserCreationResponse>builder()
                .message("Create user successfully")
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping("get-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserCreationResponse>> getAllUsers(){
        return ApiResponse.<List<UserCreationResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }
}
