package com.chubeo.DebtZero.controller;

import com.chubeo.DebtZero.dto.request.AuthenticationRequest;
import com.chubeo.DebtZero.dto.request.RefreshTokenRequest;
import com.chubeo.DebtZero.dto.response.ApiResponse;
import com.chubeo.DebtZero.dto.response.AuthenticationResponse;
import com.chubeo.DebtZero.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> userLogin(@RequestBody AuthenticationRequest authenticationRequest){
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(authenticationRequest))
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refresh(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> userLogout(@RequestBody RefreshTokenRequest request){
        authenticationService.logout(request.getRefreshToken());
        return ApiResponse.<Void>builder()
                .message("Logout successfully")
                .build();
    }
}
