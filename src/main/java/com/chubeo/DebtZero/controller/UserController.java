package com.chubeo.DebtZero.controller;

import com.chubeo.DebtZero.dto.request.UserCreationRequest;
import com.chubeo.DebtZero.dto.response.ApiResponse;
import com.chubeo.DebtZero.dto.response.UserCreationResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chubeo.DebtZero.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/create-user")
    public ApiResponse<UserCreationResponse> createUser(@RequestBody UserCreationRequest request){
        return ApiResponse.<UserCreationResponse>builder()
                .message("Create user successfully")
                .result(userService.createUser(request))
                .build();
    }


}
