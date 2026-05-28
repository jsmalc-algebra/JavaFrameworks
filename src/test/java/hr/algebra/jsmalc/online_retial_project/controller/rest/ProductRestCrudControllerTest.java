package hr.algebra.jsmalc.online_retial_project.controller.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.jsmalc.online_retial_project.domain.Product;
import hr.algebra.jsmalc.online_retial_project.service.JwtService;
import hr.algebra.jsmalc.online_retial_project.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ProductRestCrudController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@DisplayName("ProductRestCrudController Integration Tests")
public class ProductRestCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Product testProduct1;

    private Product testProduct2;

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
    @DisplayName("GET /warehouse/rest/all")
    class GetAllTests {

        @Test
        @DisplayName("Should return all products with HTTP 200")
        void findAll_ReturnsAllProducts() throws Exception {
            List<Product> products = Arrays.asList(testProduct1, testProduct2);
            when(productService.findAll()).thenReturn(products);

            mockMvc.perform(get("/warehouse/rest/all")
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].manufacturer", is("Tests of the Homeland")))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].name", is("Testurized Milk")));

            verify(productService, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no products exsist")
        void findAll_ReturnsEmptyList_WhenNoProductsExist() throws Exception {
            when(productService.findAll()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/warehouse/rest/all")
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(productService, times(1)).findAll();
        }

    }

    @Nested
    @DisplayName("GET /warehouse/rest/{id}")
    class GetByIdTests {

        @Test
        @DisplayName("Should return product with HTTP 200 when product exists")
        void findById_ReturnsProduct_WhenProductExists() throws Exception {
            when(productService.getProduct(1L)).thenReturn(Optional.of(testProduct1));

            mockMvc.perform(get("/warehouse/rest/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.manufacturer", is("Tests of the Homeland")))
                    .andExpect(jsonPath("$.name", is("Jam")))
                    .andExpect(jsonPath("$.size", is("1kg")))
                    .andExpect(jsonPath("$.price", is("123.45")));

            verify(productService, times(1)).getProduct(1L);
        }

        @Test
        @DisplayName("Should return HTTP 404 when product doesn't exist")
        void findById_ReturnsNotFound_WhenProductDoesNotExist() throws Exception {
            when(productService.getProduct(999L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/warehouse/rest/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(productService, times(2)).getProduct(999L);
        }
    }

    @Nested
    @DisplayName("POST /warehouse/rest/new")
    class CreateProductTests {

        @Test
        @DisplayName("Should create product and return HTTP 201")
        void createProduct_ReturnsCreatedProduct() throws Exception {
            Product newProduct = new Product();
            newProduct.setName("New Product");
            newProduct.setManufacturer("New prod manf");

            mockMvc.perform(post("/warehouse/rest/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name", is("New Product")))
                    .andExpect(jsonPath("$.manufacturer", is("New prod manf")));

            verify(productService, times(1)).addProduct(any());
        }

        @Test
        @DisplayName("Should create product with all fields")
        void createNewProduct_WIthAllFields_ReturnsCreatedProduct() throws Exception {

            mockMvc.perform(post("/warehouse/rest/new")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testProduct1)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.manufacturer", is("Tests of the Homeland")));

            verify(productService, times(1)).addProduct(any());
        }
    }

    @Nested
    @DisplayName("PUT /warehouse/rest/{id}")
    class UpdateProductTests {

        @Test
        @DisplayName("Should update product and return HTTP 200 when product exists")
        void updateProduct_ReturnsUpdatedProduct_WhenProductExists() throws Exception {
            Product updatedProduct = new Product();
            updatedProduct.setId(1L);
            updatedProduct.setManufacturer("Updated Manufacturer Name");
            updatedProduct.setName("Updated Product Name");
            updatedProduct.setSize("Updated Product Size");
            updatedProduct.setPrice(new BigDecimal("999.99"));

            when(productService.getProduct(1L)).thenReturn(Optional.of(testProduct1));
            when(productService.updateProduct(eq(updatedProduct), any())).thenReturn(updatedProduct);

            mockMvc.perform(put("/warehouse/rest/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.manufacturer", is("Updated Manufacturer Name")))
                    .andExpect(jsonPath("$.name", is("Updated Product Name")))
                    .andExpect(jsonPath("$.size", is("Updated Product Size")))
                    .andExpect(jsonPath("$.price", is("999.99")));

            verify(productService, times(1)).getProduct(1L);
            verify(productService, times(1)).updateProduct(eq(testProduct1), any());
        }

        @Test
        @DisplayName("Should return HTTP 404 when product to update does not exist")
        void updateProduct_ReturnsNotFound_WhenProductDoesNotExist() throws Exception {
            Product productToUpdate = new Product();
            productToUpdate.setName("Updated Product Name");

            when(productService.getProduct(999L)).thenReturn(Optional.empty());

            mockMvc.perform(put("/warehouse/rest/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToUpdate)))
                    .andExpect(status().isNotFound());

            verify(productService, times(1)).getProduct(999L);
            verify(productService, never()).updateProduct(any(), any());
        }
    }

    @Nested
    @DisplayName("DELETE /warehouse/rest/{id}")
    class DeleteProductTests {

        @Test
        @DisplayName("Should delete product and return HTTP 204 when product exsists")
        void deleteProduct_ReturnsDeletedProduct_WhenProductExists() throws Exception {
            when(productService.getProduct(1L)).thenReturn(Optional.of(testProduct1));
            doNothing().when(productService).deleteProduct(1L);

            mockMvc.perform(delete("/warehouse/rest/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(productService, times(1)).getProduct(1L);
            verify(productService, times(1)).deleteProduct(1L);
        }

        @Test
        @DisplayName("Should return HTTP 404 when product to delete does not exist")
        void deleteProduct_ReturnsNotFound_WhenProductDoesNotExist() throws Exception {
            when(productService.getProduct(999L)).thenReturn(Optional.empty());

            mockMvc.perform(delete("/warehouse/rest/{id}", 999L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(productService, times(1)).getProduct(999L);
            verify(productService, never()).deleteProduct(anyLong());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle invalid JSON in POST request")
        void createEvent_WithInvalidJson_ReturnsBadRequest() throws Exception {
            mockMvc.perform(post("/warehouse/rest/new")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ invalid JSON }"))
                    .andExpect(status().isBadRequest());

            verify(productService, times(1)).addProduct(any());
        }

        @Test
        @DisplayName("Should handle invalid JSON in PUT request")
        void updateEvent_WithInvalidJson_ReturnsBadRequest() throws Exception {
            mockMvc.perform(put("/warehouse/rest/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid JSON }"))
                    .andExpect(status().isBadRequest());

            verify(productService, never()).updateProduct(any(),any());
        }

        @Test
        @DisplayName("Should handle path variable type mismatch")
        void findById_WithInvalidIdFormat_ReturnsBadRequest() throws Exception {
            mockMvc.perform(get("/warehouse/rest/{id}", "invalidId")
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
