package hr.algebra.jsmalc.online_retial_project.repository;

import hr.algebra.jsmalc.online_retial_project.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductRepositoryJdbc Tests")
class ProductRepositoryJdbcTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProductRepositoryJdbc productRepositoryJdbc;

    private Product testProduct1;
    private Product testProduct2;

    private static final String SELECT_PRODUCT      = "SELECT * FROM PRODUCT";
    private static final String INSERT_PRODUCT      = "INSERT INTO PRODUCT (NAME, MANUFACTURER, SIZE, PRICE) VALUES (?,?,?,?)";
    private static final String UPDATE_PRODUCT      = "UPDATE PRODUCT SET NAME = ?, MANUFACTURER = ?, SIZE = ?, PRICE = ? WHERE ID = ?;";
    private static final String DELETE_PRODUCT      = "DELETE FROM PRODUCT WHERE ID = ?;";
    private static final String SELECT_PRODUCT_BY_ID = SELECT_PRODUCT + " WHERE ID = ?";

    @BeforeEach
    void setUp() {
        testProduct1 = new Product();
        testProduct1.setId(1L);
        testProduct1.setName("Jam");
        testProduct1.setManufacturer("Tests of the Homeland");
        testProduct1.setSize("1kg");
        testProduct1.setPrice(new BigDecimal("123.45"));

        testProduct2 = new Product();
        testProduct2.setId(2L);
        testProduct2.setName("Testurized Milk");
        testProduct2.setManufacturer("Doubloon");
        testProduct2.setSize("2L");
        testProduct2.setPrice(new BigDecimal("453.21"));
    }

    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("Should return all products when products exist")
        void findAll_ReturnsAllProducts() {
            List<Product> expected = Arrays.asList(testProduct1, testProduct2);
            when(jdbcTemplate.query(eq(SELECT_PRODUCT), any(RowMapper.class))).thenReturn(expected);

            List<Product> result = productRepositoryJdbc.findAll();

            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(testProduct1, testProduct2);
            verify(jdbcTemplate, times(1)).query(eq(SELECT_PRODUCT), any(RowMapper.class));
        }

        @Test
        @DisplayName("Should return empty list when no products exist")
        void findAll_ReturnsEmptyList_WhenNoProductsExist() {
            when(jdbcTemplate.query(eq(SELECT_PRODUCT), any(RowMapper.class))).thenReturn(Collections.emptyList());

            List<Product> result = productRepositoryJdbc.findAll();

            assertThat(result).isEmpty();
            verify(jdbcTemplate, times(1)).query(eq(SELECT_PRODUCT), any(RowMapper.class));
        }
    }


    @Nested
    @DisplayName("getProduct(Long id)")
    class GetProductTests {

        @Test
        @DisplayName("Should return Optional containing product when product exists")
        void getProduct_ReturnsProduct_WhenProductExists() {
            when(jdbcTemplate.queryForObject(eq(SELECT_PRODUCT_BY_ID), any(RowMapper.class), eq(1L)))
                    .thenReturn(testProduct1);

            Optional<Product> result = productRepositoryJdbc.getProduct(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
            assertThat(result.get().getName()).isEqualTo("Jam");
            assertThat(result.get().getManufacturer()).isEqualTo("Tests of the Homeland");
            assertThat(result.get().getSize()).isEqualTo("1kg");
            assertThat(result.get().getPrice()).isEqualByComparingTo(new BigDecimal("123.45"));
            verify(jdbcTemplate, times(1)).queryForObject(eq(SELECT_PRODUCT_BY_ID), any(RowMapper.class), eq(1L));
        }

        @Test
        @DisplayName("Should pass the correct ID to the query")
        void getProduct_PassesCorrectId_ToQuery() {
            when(jdbcTemplate.queryForObject(eq(SELECT_PRODUCT_BY_ID), any(RowMapper.class), eq(99L)))
                    .thenReturn(testProduct2);

            productRepositoryJdbc.getProduct(99L);

            verify(jdbcTemplate).queryForObject(eq(SELECT_PRODUCT_BY_ID), any(RowMapper.class), eq(99L));
        }

        @Test
        @DisplayName("Should propagate exception thrown by JdbcTemplate when product is not found")
        void getProduct_ThrowsException_WhenProductDoesNotExist() {
            when(jdbcTemplate.queryForObject(eq(SELECT_PRODUCT_BY_ID), any(RowMapper.class), eq(999L)))
                    .thenThrow(new org.springframework.dao.EmptyResultDataAccessException(1));

            assertThrows(org.springframework.dao.EmptyResultDataAccessException.class,
                    () -> productRepositoryJdbc.getProduct(999L));
        }
    }


    @Nested
    @DisplayName("addProduct(Product product)")
    class AddProductTests {

        @Test
        @DisplayName("Should call jdbcTemplate.update with correct SQL and all product fields")
        void addProduct_CallsJdbcTemplateUpdate_WithCorrectArguments() {
            productRepositoryJdbc.addProduct(testProduct1);

            verify(jdbcTemplate, times(1)).update(
                    eq(INSERT_PRODUCT),
                    eq(testProduct1.getName()),
                    eq(testProduct1.getManufacturer()),
                    eq(testProduct1.getSize()),
                    eq(testProduct1.getPrice())
            );
        }

        @Test
        @DisplayName("Should call jdbcTemplate.update with correct SQL when optional fields are null")
        void addProduct_CallsJdbcTemplateUpdate_WhenOptionalFieldsAreNull() {
            Product minimalProduct = new Product();
            minimalProduct.setName("Bare Product");

            productRepositoryJdbc.addProduct(minimalProduct);

            verify(jdbcTemplate, times(1)).update(
                    eq(INSERT_PRODUCT),
                    eq("Bare Product"),
                    isNull(),
                    isNull(),
                    isNull()
            );
        }

        @Test
        @DisplayName("Should use correct argument order in INSERT statement")
        void addProduct_UsesCorrectArgumentOrder() {
            ArgumentCaptor<Object[]> captor = ArgumentCaptor.forClass(Object[].class);

            // Capture varargs as an Object array
            productRepositoryJdbc.addProduct(testProduct1);

            verify(jdbcTemplate).update(
                    eq(INSERT_PRODUCT),
                    eq("Jam"),
                    eq("Tests of the Homeland"),
                    eq("1kg"),
                    eq(new BigDecimal("123.45"))
            );
        }
    }


    @Nested
    @DisplayName("updateProduct(Product original, Product updated)")
    class UpdateProductTests {

        @Test
        @DisplayName("Should call jdbcTemplate.update with correct SQL and updated fields")
        void updateProduct_CallsJdbcTemplateUpdate_WithCorrectArguments() {
            Product updatedProduct = new Product();
            updatedProduct.setName("Updated Jam");
            updatedProduct.setManufacturer("Updated Manufacturer");
            updatedProduct.setSize("2kg");
            updatedProduct.setPrice(new BigDecimal("200.00"));

            productRepositoryJdbc.updateProduct(testProduct1, updatedProduct);

            verify(jdbcTemplate, times(1)).update(
                    eq(UPDATE_PRODUCT),
                    eq("Updated Jam"),
                    eq("Updated Manufacturer"),
                    eq("2kg"),
                    eq(new BigDecimal("200.00")),
                    eq(1L)   // ID comes from originalProduct
            );
        }

        @Test
        @DisplayName("Should use original product's ID, not the updated product's ID")
        void updateProduct_UsesOriginalProductId() {
            Product updatedProduct = new Product();
            updatedProduct.setId(99L); // different ID — should be ignored
            updatedProduct.setName("Updated Name");

            productRepositoryJdbc.updateProduct(testProduct1, updatedProduct);

            // The ID passed to the query must be testProduct1's ID (1L), not 99L
            verify(jdbcTemplate).update(
                    eq(UPDATE_PRODUCT),
                    any(),
                    any(),
                    any(),
                    any(),
                    eq(1L)
            );
        }
    }

    @Nested
    @DisplayName("deleteProduct(Long productId)")
    class DeleteProductTests {

        @Test
        @DisplayName("Should call jdbcTemplate.update with correct SQL and product ID")
        void deleteProduct_CallsJdbcTemplateUpdate_WithCorrectId() {
            productRepositoryJdbc.deleteProduct(1L);

            verify(jdbcTemplate, times(1)).update(eq(DELETE_PRODUCT), eq(1L));
        }

        @Test
        @DisplayName("Should pass the correct ID when deleting a different product")
        void deleteProduct_PassesCorrectId_ForDifferentProduct() {
            productRepositoryJdbc.deleteProduct(42L);

            verify(jdbcTemplate, times(1)).update(eq(DELETE_PRODUCT), eq(42L));
        }

        @Test
        @DisplayName("Should propagate exception thrown by JdbcTemplate on delete failure")
        void deleteProduct_PropagatesException_OnFailure() {
            doThrow(new org.springframework.dao.DataAccessException("DB error") {})
                    .when(jdbcTemplate).update(eq(DELETE_PRODUCT), eq(1L));

            assertThrows(org.springframework.dao.DataAccessException.class,
                    () -> productRepositoryJdbc.deleteProduct(1L));
        }
    }
}