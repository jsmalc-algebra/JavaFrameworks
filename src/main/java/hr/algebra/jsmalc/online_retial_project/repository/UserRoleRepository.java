package hr.algebra.jsmalc.online_retial_project.repository;

import hr.algebra.jsmalc.online_retial_project.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Transactional
    void deleteByUsername(String username);
}
