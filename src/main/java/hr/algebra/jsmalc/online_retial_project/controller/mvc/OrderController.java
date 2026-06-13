package hr.algebra.jsmalc.online_retial_project.controller.mvc;

import hr.algebra.jsmalc.online_retial_project.domain.Order;
import hr.algebra.jsmalc.online_retial_project.domain.OrderItem;
import hr.algebra.jsmalc.online_retial_project.domain.Product;
import hr.algebra.jsmalc.online_retial_project.domain.ShippingStatus;
import hr.algebra.jsmalc.online_retial_project.service.MyUserDetailsService;
import hr.algebra.jsmalc.online_retial_project.service.OrderService;
import hr.algebra.jsmalc.online_retial_project.service.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("orders/")
public class OrderController {
    private final OrderService orderService;
    private final MyUserDetailsService userDetailsService;

    private final ProductService productService;


    public OrderController(OrderService orderService, MyUserDetailsService userDetailsService, ProductService productService) {
        this.orderService = orderService;
        this.userDetailsService = userDetailsService;
        this.productService = productService;
    }

    @GetMapping("overview")
    public String showOverviewScreen(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        if (userDetailsService.isUserStaff(username)) {model.addAttribute("orders",orderService.findAll());}
        else {model.addAttribute("orders",orderService.getOrdersByUsername(username));}
        return "order-overview";
    }

    @GetMapping("details")
    public String showDetailsScreen(@RequestParam Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Order> orderOptional = orderService.getOrder(id);
        if (orderOptional.isEmpty()) {return "order-not-found";}
        Order order = orderOptional.get();
        String username = userDetails.getUsername();

        if(userDetailsService.isUserStaff(username) || order.getUsername().equals(username)) {
            model.addAttribute("order", order);
            return "order-detail";
        } else { return "order-not-found"; }
    }

    @GetMapping("updateScreen")
    public String showUpdateScreen(@RequestParam Long id, Model model) {
        Optional<Order> orderOptional = orderService.getOrder(id);
        if (orderOptional.isEmpty()) {return "order-not-found";}

        Order order = orderOptional.get();
        List<OrderItem> orderItems = order.getOrderedItems();
        List<Product> products = productService.findAll();

        Set<Product> productSet = orderItems.stream()
                .map(OrderItem::getProduct)
                .collect(Collectors.toSet());

        products.stream()
                .filter(product -> !productSet.contains(product))
                .map(product -> new OrderItem(product,0))
                .forEach(orderItems::add);

        order.setOrderedItems(orderItems);
        model.addAttribute("order", order);
        model.addAttribute("statuses", ShippingStatus.values());
        return "order-edit";
    }

    @PostMapping("update")
    public String updateOrder(@ModelAttribute("order") Order orderToUpdate) {
        orderToUpdate.getOrderedItems().removeIf(orderItem -> orderItem.getQuantity()==0);
        orderService.updateOrder(orderToUpdate,orderToUpdate);

        return "redirect:/orders/details?id=" + orderToUpdate.getId();
    }


}
