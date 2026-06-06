package hr.algebra.jsmalc.online_retial_project.service;

import hr.algebra.jsmalc.online_retial_project.domain.Product;
import hr.algebra.jsmalc.online_retial_project.repository.ProductRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(ProductServiceImplTest.class)
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
public class ProductServiceImplTest {

    @MockitoBean
    private ProductRepositoryJpa productRepository;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;


    private Product testProduct1;

    private Product testProduct2;

    private ProductService productService;

    @BeforeEach
    public void setup() {
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

        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    @DisplayName("should return all products from repository")
    public void should_return_all_products() {
        List<Product> products = Arrays.asList(testProduct1, testProduct2);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> productList = productService.findAll();

        verify(productRepository, times(1)).findAll();
        assertSame(products, productList);
    }

    @Test
    @DisplayName("should call save method while saving a new Product object only once")
    public void should_call_save_method_while_saving_a_new_product() {
        when(productRepository.save(testProduct1)).thenReturn(testProduct1);

        Product savedProduct = productService.addProduct(testProduct1);

        verify(productRepository, times(1)).save(any(Product.class));
        assertEquals(testProduct1, savedProduct);
    }

    @Test
    @DisplayName("Should call delete method while deleting a new Product object only once")
    public void should_call_delete_method_while_deleting_a_new_product() {

        productService.deleteProduct(testProduct1.getId());

        verify(productRepository, times(1)).deleteById(any(Long.class));
    }

   @Test
   @DisplayName("Should return one product from the repository defined by it's ID")
   public void should_return_one_product_by_id() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(testProduct1));

        Optional<Product> productOptional = productService.getProduct(testProduct1.getId());

        verify(productRepository, times(1)).findById(anyLong());
        assertTrue(productOptional.isPresent());
   }

   @Test
   @DisplayName("should update the product")
   public void should_update_the_product() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct2);

        productService.updateProduct(testProduct1, testProduct2);

        verify(productRepository, times(1)).save(any(Product.class));
        assertSame(testProduct2.getId(),testProduct1.getId());
        assertSame("Testurized Milk",testProduct2.getName());
        assertSame("Doubloon",testProduct2.getManufacturer());

   }

}
