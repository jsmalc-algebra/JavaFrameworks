package hr.algebra.jsmalc.online_retial_project.service;

import hr.algebra.jsmalc.online_retial_project.domain.Product;
import hr.algebra.jsmalc.online_retial_project.repository.ProductRepositoryJpa;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepositoryJpa productRepository;

    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public Product addProduct(Product product) {
      return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProduct(Long id) {
       return productRepository.findById(id);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Product updateProduct(Product originalProduct, Product updatedProduct) {
        updatedProduct.setId(originalProduct.getId());
        return productRepository.save(updatedProduct);
    }
}
