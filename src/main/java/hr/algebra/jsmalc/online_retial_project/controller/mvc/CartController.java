package hr.algebra.jsmalc.online_retial_project.controller.mvc;

import hr.algebra.jsmalc.online_retial_project.domain.Cart;
import hr.algebra.jsmalc.online_retial_project.domain.Product;
import hr.algebra.jsmalc.online_retial_project.service.CartService;
import hr.algebra.jsmalc.online_retial_project.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("catalog/cart/")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping("add")
    public String add(@RequestParam Long productId,
                      @RequestParam(defaultValue = "/catalog/welcome") String returnUrl,
                      @RequestParam(defaultValue = "1") int quantity, RedirectAttributes redirectAttributes) {
        cartService.addToCart(productId,quantity);
        redirectAttributes.addFlashAttribute("message", "Product added to cart");
        return "redirect:" + returnUrl;
    }

    @PostMapping("remove")
    public String removeFromCart(@RequestParam Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/catalog/cart/view";
    }

    @GetMapping("view")
    public String viewCart(Model model) {
        model.addAttribute("cart", cartService.getCart());
        return "cart";
    }

    @PostMapping("placeOrder")
    public String placeOrder() {
        Cart cart = cartService.getCart();
        // Will be delegated to shipment
//        cart.getItems().forEach(cartItem -> {
//            Product product = productService.getProduct(cartItem.getProduct().getId()).get();
//            product.setCurrentStock(product.getCurrentStock() - cartItem.getQuantity());
//            productService.updateProduct(product, product);
//        });


        cartService.clearCart();
        return "redirect:/catalog/welcome"; // or an order-confirmed page
    }
}
