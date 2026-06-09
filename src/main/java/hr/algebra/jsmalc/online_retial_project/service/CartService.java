package hr.algebra.jsmalc.online_retial_project.service;

import hr.algebra.jsmalc.online_retial_project.domain.Cart;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class CartService {

    @Getter
    private final Cart cart = new Cart();
    private final ProductService productService;


    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public void addToCart(Long productId) {
        productService.getProduct(productId)
                .ifPresent(cart::addItem);
    }

    public void removeFromCart(Long productId) {
        cart.removeItem(productId);
    }

    public void clearCart() {
        cart.getItems().clear();
    }
}
