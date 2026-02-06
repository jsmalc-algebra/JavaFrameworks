package hr.algebra.jsmalc.online_retial_project.repository;


import hr.algebra.jsmalc.online_retial_project.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserInfo_Username(String username);
    void deleteByToken(String token);
    void deleteByUserInfo_Username(String userInfoUsername);
}
