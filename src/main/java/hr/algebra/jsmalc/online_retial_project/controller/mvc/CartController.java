package hr.algebra.jsmalc.online_retial_project.controller.mvc;

import hr.algebra.jsmalc.online_retial_project.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("catalog/cart/")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("add")
    public String add(@RequestParam Long productId, @RequestParam(defaultValue = "/catalog/welcome") String returnUrl) {
        cartService.addToCart(productId);
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
        // TODO: persist the order to DB, trigger email, etc.
        cartService.clearCart();
        return "redirect:/catalog/welcome"; // or an order-confirmed page
    }
}
