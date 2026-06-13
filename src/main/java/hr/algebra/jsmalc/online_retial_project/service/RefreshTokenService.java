package hr.algebra.jsmalc.online_retial_project.service;
import hr.algebra.jsmalc.online_retial_project.domain.RefreshToken;
import hr.algebra.jsmalc.online_retial_project.repository.RefreshTokenRepository;
import hr.algebra.jsmalc.online_retial_project.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;
    private UsersRepository userRepository;

    public RefreshToken createRefreshToken(String username){

        Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findByUserInfo_Username(username);

        existingRefreshToken.ifPresent(refreshToken -> refreshTokenRepository.deleteByToken(refreshToken.getToken()));

        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userRepository.getByUsername(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000)) // set expiry of refresh token to 10 minutes - you can configure it application.properties file
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if(refreshToken.isPresent()){
            refreshTokenRepository.delete(refreshToken.get());
        } else {
            throw new RuntimeException("Refresh Token is not in DB..!!");
        }
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

    public void deleteByUsername(String username) {
        refreshTokenRepository.deleteByUserInfo_Username(username);
    }

}