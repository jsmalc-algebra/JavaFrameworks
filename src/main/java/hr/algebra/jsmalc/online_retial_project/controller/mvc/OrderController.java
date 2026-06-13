package hr.algebra.jsmalc.online_retial_project.controller.mvc;

import hr.algebra.jsmalc.online_retial_project.domain.Order;
import hr.algebra.jsmalc.online_retial_project.domain.ShippingStatus;
import hr.algebra.jsmalc.online_retial_project.service.MyUserDetailsService;
import hr.algebra.jsmalc.online_retial_project.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("orders/")
public class OrderController {
    private final OrderService orderService;
    private final MyUserDetailsService userDetailsService;


    public OrderController(OrderService orderService, MyUserDetailsService userDetailsService) {
        this.orderService = orderService;
        this.userDetailsService = userDetailsService;
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
            model.addAttribute("statuses", ShippingStatus.values());
            return "order-detail";
        } else { return "order-not-found"; }
    }

    @PostMapping("update")
    public String updateOrder(@RequestParam Long orderId, @RequestParam String newStatus) {
        Order order = orderService.getOrder(orderId).get();
        order.setShippingStatus(ShippingStatus.valueOf(newStatus));
        orderService.updateOrder(order,order);
        return "redirect:/orders/overview";
    }


}
