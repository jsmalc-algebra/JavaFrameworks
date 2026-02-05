package hr.algebra.jsmalc.online_retial_project.repository;

import hr.algebra.jsmalc.online_retial_project.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryJpa extends CrudRepository<Product, Long> {
}
