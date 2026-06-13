package hr.algebra.jsmalc.online_retial_project.controller.mvc;

import hr.algebra.jsmalc.online_retial_project.domain.User;
import hr.algebra.jsmalc.online_retial_project.domain.UserRole;
import hr.algebra.jsmalc.online_retial_project.repository.UserRoleRepository;
import hr.algebra.jsmalc.online_retial_project.repository.UsersRepository;
import hr.algebra.jsmalc.online_retial_project.service.MyUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Arrays.stream;

@Controller
@RequestMapping("admin/")
public class AdminController {

    private final MyUserDetailsService userDetailsService;
    private final UserRoleRepository userRoleRepository;
    private final UsersRepository usersRepository;

    public AdminController(MyUserDetailsService userDetailsService, UserRoleRepository userRoleRepository, UsersRepository usersRepository) {
        this.userDetailsService = userDetailsService;
        this.userRoleRepository = userRoleRepository;
        this.usersRepository = usersRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("roles");
    }

    @GetMapping("landing")
    public String landing() {
        return "manager-landing";
    }

    @GetMapping("users")
    public String users(Model model) {
        model.addAttribute("users",userDetailsService.findAllUsers());
        return "manager-user-list";
    }

    @GetMapping("users/new")
    public String newUser(Model model) {
        model.addAttribute("user",new User());
        return "create-user";
    }

    @PostMapping("users/create")
    public String createUser(@ModelAttribute("user") User user, @RequestParam List<String> roles) {
        userDetailsService.addUser(user);

        List<UserRole> userRoles = roles.stream()
                .map(role ->{
                    UserRole userRole = new UserRole();
                    userRole.setAuthority(role);
                    userRole.setUsername(user.getUsername());
                    return userRole;
                }).toList();

        userRoleRepository.saveAll(userRoles);

        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit")
    public String editUserForm(@RequestParam String username, Model model) {
        User user = userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("currentRoles", user.getRoles());
        return "edit-user";
    }

    @PostMapping("/users/update")
    public String editUser(@ModelAttribute User user,
                           @RequestParam(required = false) List<String> roles) {

        userDetailsService.addUser(user);

        // delete old roles and replace with the new selection
        userRoleRepository.deleteByUsername(user.getUsername());

        List<UserRole> userRoles = roles.stream()
                .map(authority -> {
                    UserRole ur = new UserRole();
                    ur.setAuthority(authority);
                    ur.setUsername(user.getUsername());
                    return ur;
                })
                .toList();
        userRoleRepository.saveAll(userRoles);


        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam String username) {
        usersRepository.deleteByUsername(username);
        userRoleRepository.deleteByUsername(username);
        return "redirect:/admin/users";
    }
}
