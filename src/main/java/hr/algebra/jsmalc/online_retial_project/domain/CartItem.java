package hr.algebra.jsmalc.online_retial_project.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
        private Product product;
        private int quantity;

    public CartItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public BigDecimal getSubtotal() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}


