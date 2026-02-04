package hr.algebra.jsmalc.online_retial_project.repository;

import hr.algebra.jsmalc.online_retial_project.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryMock implements ProductRepository {

    private List<Product> products;

    public ProductRepositoryMock() {
        products = new ArrayList<>();
        products.add(new Product(
                1L,
                "Liquid yogurt XXL",
                "Pilos",
                "1.5kg",
                1.69
        ));
        products.add(new Product(
                2L,
                "Crunchy peanut butter XXL",
                "McKennedy",
                "1kg",
                5.79


        ));
        products.add(new Product(
                3L,
                "Homemade burek with cheese",
                "Store bakery",
                "220g",
                0.85
        ));
        products.add(new Product(
                4L,
                "Mayo",
                "STAR",
                "165g",
                1.89
        ));
        products.add(new Product(
                5L,
                "Long-life milk XXL",
                "Ducat",
                "8x1L",
                6.99
        ));
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public void addProduct(Product product) {
        products.add(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        products.removeIf(product ->product.getId().equals(productId));
    }

    @Override
    public Optional<Product> getProduct(Long productId) {
        return products.stream().filter(product -> product.getId().equals(productId)).findFirst();
    }

    @Override
    public void updateProduct(Product originalProduct,Product productToUpdate) {
            originalProduct.setName(productToUpdate.getName());
            originalProduct.setPrice(productToUpdate.getPrice());
            originalProduct.setManufacturer(productToUpdate.getManufacturer());
            originalProduct.setSize(productToUpdate.getSize());

    }
}
