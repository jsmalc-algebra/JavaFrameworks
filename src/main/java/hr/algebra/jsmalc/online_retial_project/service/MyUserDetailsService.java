package hr.algebra.jsmalc.online_retial_project.service;

import hr.algebra.jsmalc.online_retial_project.domain.User;
import hr.algebra.jsmalc.online_retial_project.domain.UserRole;
import hr.algebra.jsmalc.online_retial_project.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MyUserDetailsService /*implements UserDetailsService*/ {

    private UsersRepository usersRepository;

    //@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.getByUsername(username);

        String[] rolesString = new String[user.getRoles().size()];

        for (int i = 0; i < user.getRoles().size(); i++) {
            rolesString[i] = user.getRoles().get(i).getAuthority();
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .roles(rolesString)
                .build();
    }

    public Boolean isUserStaff(String username) throws UsernameNotFoundException {
        User user = usersRepository.getByUsername(username);
        List<UserRole> roles = user.getRoles();
        Boolean flag = roles.stream().anyMatch(userRole -> userRole.getAuthority().equals("ROLE_ADMIN"));
        return flag;
//        return usersRepository.findByUsername(username).getRoles()
//                .stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        // TODO: Change when Customer/Staff/Manager roles are implemented
    }
}

