package hr.algebra.jsmalc.online_retial_project.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderedItems;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShippingStatus shippingStatus;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
        this.shippingStatus = ShippingStatus.PENDING;
    }

    public String getShippingStatusClass() {
        return switch (shippingStatus) {
            case PENDING -> "badge-pending";
            case SHIPPED -> "badge-shipped";
            case DELIVERED -> "badge-delivered";
            case CANCELLED -> "badge-cancelled";
        };
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getGrandTotal() {
        return orderedItems.stream().map(OrderItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add).toString();
    }

    public String getShippingStatus() {
        return shippingStatus.toString();
    }
}
