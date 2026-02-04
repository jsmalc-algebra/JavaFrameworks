package hr.algebra.jsmalc.online_retial_project.repository;

import hr.algebra.jsmalc.online_retial_project.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    void addProduct(Product product);
    Optional<Product> getProduct(Long id);
    void deleteProduct(Long productId);
    void updateProduct(Product originalProduct, Product updatedProduct);
}
