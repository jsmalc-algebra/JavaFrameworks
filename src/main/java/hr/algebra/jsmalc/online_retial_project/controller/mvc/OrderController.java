package hr.algebra.jsmalc.online_retial_project.controller.mvc;

import hr.algebra.jsmalc.online_retial_project.domain.Order;
import hr.algebra.jsmalc.online_retial_project.domain.ShippingStatus;
import hr.algebra.jsmalc.online_retial_project.service.MyUserDetailsService;
import hr.algebra.jsmalc.online_retial_project.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String showOverviewScreen(@RequestParam String username, Model model) {
        if (userDetailsService.isUserStaff(username)) {model.addAttribute("orders",orderService.findAll());}
        else {model.addAttribute("orders",orderService.getOrdersByUsername(username));}
        return "order-overview";
    }

    @GetMapping("details")
    public String showDetailsScreen(@RequestParam Long id, Model model) {
        Optional<Order> orderOptional = orderService.getOrder(id);
        if (orderOptional.isEmpty()) {return "order-not-found";}
        model.addAttribute("order", orderOptional.get());
        model.addAttribute("statuses", ShippingStatus.values());
        return "order-detail";
    }
}
