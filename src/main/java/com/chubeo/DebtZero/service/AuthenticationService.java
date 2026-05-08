package com.chubeo.DebtZero.service;

import com.chubeo.DebtZero.config.JwtTokenProvider;
import com.chubeo.DebtZero.dto.request.AuthenticationRequest;
import com.chubeo.DebtZero.dto.request.RefreshTokenRequest;
import com.chubeo.DebtZero.dto.response.AuthenticationResponse;
import com.chubeo.DebtZero.entity.CustomUserDetails;
import com.chubeo.DebtZero.entity.RefreshToken;
import com.chubeo.DebtZero.exception.AppException;
import com.chubeo.DebtZero.exception.ErrorCode;
import com.chubeo.DebtZero.repository.RefreshTokenRepository;
import com.chubeo.DebtZero.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtTokenProvider;
    RefreshTokenService refreshTokenService;
    RefreshTokenRepository refreshTokenRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtTokenProvider.generateToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser());
            return new AuthenticationResponse(accessToken, refreshToken.getToken());
        } catch (BadCredentialsException e){
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        } catch (UsernameNotFoundException e){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    public AuthenticationResponse refresh(RefreshTokenRequest request){
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_NOT_FOUND));

        refreshTokenService.verifyExpiration(refreshToken);

        //Take user, wrap into UserDetails
        CustomUserDetails userDetails = new CustomUserDetails(refreshToken.getUser());

        String accessToken = jwtTokenProvider.generateToken(userDetails);

        return new AuthenticationResponse(accessToken, refreshToken.getToken());

    }

    public void logout(String refreshToken){
        RefreshToken token = refreshTokenService.findByToken(refreshToken);
        refreshTokenService.deleteByUser(token.getUser());
    }
}
