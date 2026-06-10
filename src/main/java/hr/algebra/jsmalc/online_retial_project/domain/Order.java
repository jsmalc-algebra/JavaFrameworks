package hr.algebra.jsmalc.online_retial_project.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "products_id")
    private List<OrderItem> orderedItems;
    private LocalDateTime date;
    private Boolean shipped;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
        this.shipped = false;
    }
}
