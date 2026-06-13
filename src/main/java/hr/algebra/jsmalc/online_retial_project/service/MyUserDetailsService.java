package hr.algebra.jsmalc.online_retial_project.service;

import hr.algebra.jsmalc.online_retial_project.domain.User;
import hr.algebra.jsmalc.online_retial_project.domain.UserRole;
import hr.algebra.jsmalc.online_retial_project.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MyUserDetailsService /*implements UserDetailsService*/ {

    private UsersRepository usersRepository;

    //@Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.getByUsername(username);

        List<UserRole> userRoles = user.getRoles();

        return User
                .builder()
                .username(username)
                .password(user.getPassword())
                .roles(userRoles)
                .build();
    }

    public Boolean isUserStaff(String username) throws UsernameNotFoundException {
        return usersRepository.getByUsername(username).getRoles()
                .stream().anyMatch(role -> role.getAuthority().equals("ROLE_STAFF"));
    }

    public List<User> findAllUsers() {
        return usersRepository.findAll();
    }

    public void addUser(User user) {
        usersRepository.save(user);
    }

    public void deleteUser(String username) {
        usersRepository.deleteByUsername(username);
    }
}

