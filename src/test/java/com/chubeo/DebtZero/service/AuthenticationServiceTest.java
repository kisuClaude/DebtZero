package com.chubeo.DebtZero.service;

import com.chubeo.DebtZero.config.JwtTokenProvider;
import com.chubeo.DebtZero.dto.request.AuthenticationRequest;
import com.chubeo.DebtZero.dto.response.AuthenticationResponse;
import com.chubeo.DebtZero.entity.CustomUserDetails;
import com.chubeo.DebtZero.entity.RefreshToken;
import com.chubeo.DebtZero.entity.Role;
import com.chubeo.DebtZero.entity.User;
import com.chubeo.DebtZero.exception.AppException;
import com.chubeo.DebtZero.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void shouldAuthenticateSuccessfully() {
        AuthenticationRequest request =
                new AuthenticationRequest("anh", "123");

        User user = new User();
        user.setUsername("anh");

        Role role = new Role();
        role.setName("ROLE_USER");

        user.setRoles(Set.of(role));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(jwtTokenProvider.generateToken(userDetails)).thenReturn("access-token");

        when(refreshTokenService.createRefreshToken(user))
                .thenReturn(refreshToken);


        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals("access-token", response.getToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    void shouldThrowInvalidCredentialsException() {

        // Arrange
        AuthenticationRequest request =
                new AuthenticationRequest("anh", "wrong-password");

        when(authenticationManager.authenticate(any()))
                .thenThrow(
                        new BadCredentialsException("Bad credentials")
                );

        // Act + Assert
        AppException exception = assertThrows(
                AppException.class,
                () -> authenticationService.authenticate(request)
        );

        assertEquals(
                ErrorCode.INVALID_CREDENTIALS,
                exception.getErrorCode()
        );
    }
}
