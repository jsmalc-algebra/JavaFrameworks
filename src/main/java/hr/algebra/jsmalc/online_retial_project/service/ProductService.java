package hr.algebra.jsmalc.online_retial_project.service;

import hr.algebra.jsmalc.online_retial_project.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();
    Product addProduct(Product product);
    Optional<Product> getProduct(Long id);
    void deleteProduct(Long productId);
    Product updateProduct(Product originalProduct, Product updatedProduct);
}
