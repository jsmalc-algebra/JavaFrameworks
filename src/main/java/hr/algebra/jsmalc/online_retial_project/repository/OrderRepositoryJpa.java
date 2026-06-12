package hr.algebra.jsmalc.online_retial_project.repository;

import hr.algebra.jsmalc.online_retial_project.domain.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepositoryJpa extends CrudRepository<Order, Long> {
    List<Order> getOrdersByUsername(String username);
}
