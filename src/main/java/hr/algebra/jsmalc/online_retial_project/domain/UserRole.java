package hr.algebra.jsmalc.online_retial_project.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "AUTHORITIES")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String authority;
    @Column(name = "username")
    private String username;

//    @ManyToOne
//    @JoinColumn(name = "username", referencedColumnName = "username")
//    private User user;
}
