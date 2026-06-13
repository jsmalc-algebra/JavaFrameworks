package hr.algebra.jsmalc.online_retial_project.controller.mvc;

import hr.algebra.jsmalc.online_retial_project.domain.Cart;
import hr.algebra.jsmalc.online_retial_project.domain.Order;
import hr.algebra.jsmalc.online_retial_project.domain.Product;
import hr.algebra.jsmalc.online_retial_project.service.CartService;
import hr.algebra.jsmalc.online_retial_project.service.MyUserDetailsService;
import hr.algebra.jsmalc.online_retial_project.service.OrderService;
import hr.algebra.jsmalc.online_retial_project.service.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final OrderService orderService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService, OrderService orderService, ProductService productService1) {
        this.cartService = cartService;
        this.orderService = orderService;
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
    public String placeOrder(@AuthenticationPrincipal UserDetails userDetails) {
        Cart cart = cartService.getCart();

        orderService.addOrder(
                Order.builder()
                        .orderedItems(cart.getItems())
                        .username(userDetails.getUsername())
                        .build()
        );

        cart.getItems().forEach(cartItem -> {
            Product product = productService.getProduct(cartItem.getProduct().getId()).get();
            product.setCurrentStock(product.getCurrentStock() - cartItem.getQuantity());
            productService.updateProduct(product, product);
        });


        cartService.clearCart();
        return "redirect:/catalog/welcome";
    }
}
