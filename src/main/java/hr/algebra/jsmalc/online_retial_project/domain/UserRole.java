package hr.algebra.jsmalc.online_retial_project.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Entity
@Data
@Table(name = "AUTHORITIES")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;
    @Column(name = "username")
    private String username;

    public String getCssClass() {
        return switch (authority) {
            case "ROLE_USER" -> "badge-role-user";
            case "ROLE_STAFF" -> "badge-role-staff";
            case "ROLE_MANAGER" -> "badge-role-admin";
            default -> throw new IllegalStateException("Unexpected value: " + authority);
        };
    }

//    @ManyToOne
//    @JoinColumn(name = "username", referencedColumnName = "username")
//    private User user;
}
