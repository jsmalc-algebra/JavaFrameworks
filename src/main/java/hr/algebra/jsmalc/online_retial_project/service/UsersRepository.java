package hr.algebra.jsmalc.online_retial_project.service;


import hr.algebra.jsmalc.online_retial_project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
