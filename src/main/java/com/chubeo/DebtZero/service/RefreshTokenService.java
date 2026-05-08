package com.chubeo.DebtZero.service;

import com.chubeo.DebtZero.entity.RefreshToken;
import com.chubeo.DebtZero.entity.User;
import com.chubeo.DebtZero.exception.AppException;
import com.chubeo.DebtZero.exception.ErrorCode;
import com.chubeo.DebtZero.repository.RefreshTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenService {

    @NonFinal
    @Value("${jwt.refresh-exipration}")
    Long refreshExpiration;

    RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(User user){
        deleteByUser(user);

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plusMillis(refreshExpiration);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(token);
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setUser(user);

        refreshTokenRepository.save(refreshToken);

        return  refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.deleteByUser(token.getUser());
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
        return token;
    }

    //Delete junk tokens in database
    public void deleteByUser(User user){
        refreshTokenRepository.deleteByUser(user);
    }

    public RefreshToken findByToken(String token){
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_NOT_FOUND));
    }


}
