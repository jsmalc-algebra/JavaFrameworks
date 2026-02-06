package hr.algebra.jsmalc.online_retial_project.repository;

import hr.algebra.jsmalc.online_retial_project.domain.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class ProductRepositoryJdbc implements ProductRepository {

    private static final String SELECT_PRODUCT = "SELECT * FROM PRODUCT";
    private static final String INSERT_PRODUCT = "INSERT INTO PRODUCT (NAME, MANUFACTURER, SIZE, PRICE) VALUES (?,?,?,?)";
    private static final String UPDATE_PRODUCT = "UPDATE PRODUCT SET NAME = ?, MANUFACTURER = ?, SIZE = ?, PRICE = ? WHERE ID = ?;";
    private static final String DELETE_PRODUCT = "DELETE FROM PRODUCT WHERE ID = ?;";
    private JdbcTemplate jdbcTemplate;


    public ProductRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(SELECT_PRODUCT, new ProductRowMapper());
    }

    @Override
    public void addProduct(Product product) {
        jdbcTemplate.update(INSERT_PRODUCT,
                product.getName(),
                product.getManufacturer(),
                product.getSize(),
                product.getPrice()
        );
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        Product product = jdbcTemplate.queryForObject(
                SELECT_PRODUCT + " WHERE ID = ?", new ProductRowMapper(), id
        );
        return Optional.of(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        jdbcTemplate.update(DELETE_PRODUCT, productId);
    }

    @Override
    public void updateProduct(Product originalProduct, Product updatedProduct) {
        jdbcTemplate.update(UPDATE_PRODUCT,
                updatedProduct.getName(),
                updatedProduct.getManufacturer(),
                updatedProduct.getSize(),
                updatedProduct.getPrice(),
                originalProduct.getId()
        );
    }

    private class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product newProduct = new Product();
            newProduct.setId(rs.getLong("id"));
            newProduct.setName(rs.getString("name"));
            newProduct.setManufacturer(rs.getString("manufacturer"));
            newProduct.setSize(rs.getString("size"));
            newProduct.setPrice(rs.getBigDecimal("price"));
            return newProduct;
        }
    }
}
