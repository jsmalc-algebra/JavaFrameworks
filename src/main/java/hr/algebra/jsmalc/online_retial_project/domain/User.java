package hr.algebra.jsmalc.online_retial_project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "USERS")
public class User {
    @Id
    @Column(name = "username")
    private String username;
    private String password;
    private Boolean enabled;
    @OneToMany(mappedBy = "username", fetch = FetchType.EAGER)
    private List<UserRole> roles;
}
