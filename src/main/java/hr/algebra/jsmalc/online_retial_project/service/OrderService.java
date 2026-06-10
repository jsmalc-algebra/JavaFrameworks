package hr.algebra.jsmalc.online_retial_project.service;

import hr.algebra.jsmalc.online_retial_project.domain.Order;
import hr.algebra.jsmalc.online_retial_project.repository.OrderRepositoryJpa;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService{
    private final OrderRepositoryJpa orderRepository;

    public List<Order> findAll() {
        return (List<Order>) orderRepository.findAll();
    }

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public Order updateOrder(Order originalOrder, Order updatedOrder) {
        updatedOrder.setId(originalOrder.getId());
        return orderRepository.save(updatedOrder);
    }


}
