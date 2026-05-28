package hr.algebra.jsmalc.online_retial_project.controller.mvc;


import hr.algebra.jsmalc.online_retial_project.domain.Product;
import hr.algebra.jsmalc.online_retial_project.service.JwtService;
import hr.algebra.jsmalc.online_retial_project.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(WelcomeScreenController.class)
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
class WelcomeScreenControllerIntegrationTest {

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;
    @Autowired
    private MockMvc mockMvc;

    private Product createTestProduct(Long id, String name, String manufacturer, String size, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setManufacturer(manufacturer);
        product.setSize(size);
        product.setPrice(price);
        return product;
    }

    @Test
    @DisplayName("GET /catalog/welcome - should return welcome view with products")
    void showWelcomeScreen() throws Exception {
        // Arrange
        Product product1 = createTestProduct(
                1L,
                "Strawberry Jam",
                "Tests of the homeland",
                "1kg",
                new BigDecimal("123.45")
        );

        Product product2 = createTestProduct(
                2L,
                "Spahetti",
                "Burrial",
                "2kg",
                new BigDecimal("123.45")
        );
        List<Product> products = List.of(product1, product2);

        when(productService.findAll()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/catalog/welcome"))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasSize(2)))
                .andExpect(model().attribute("products", hasItem(
                        hasProperty("name", is("Strawberry Jam"))
                )));

        verify(productService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /catalog/welcome - should return empty list when no products")
    void showEmptyWelcomeScreen() throws Exception {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/catalog/welcome"))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasSize(0)));
    }

    @Test
    @DisplayName("GET /catalog/newProductScreen - should return a screen which allows adding new products")
    void showNewProductScreen_ShouldReturnNewProductView() throws Exception {
        mockMvc.perform(get("/catalog/newProductScreen"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-new-product"))
                .andExpect(model().attributeExists("product"));

        verify(productService, times(0)).findAll();
    }

    @Test
    @DisplayName("GET /catalog/updateScreen?id=[productID] - should return product-edit view with product if it exists")
    void showUpdateProductScreen_ShouldReturnProductEditView() throws Exception {

        Product product1 = createTestProduct(
                1L,
                "Strawberry Jam",
                "Tests of the homeland",
                "1kg",
                new BigDecimal("123.45")
        );
        when(productService.getProduct(1L)).thenReturn(Optional.of(product1));

        mockMvc.perform(get("/catalog/updateScreen")
                    .param("id","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-edit"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", hasProperty("name", is("Strawberry Jam"))));

        verify(productService, times(1)).getProduct(1L);
    }

    @Test
    @DisplayName("GET /catalog/details?id=[productID] - should return product-not-found view if product doesn't exist")
    void showProductDetails_ShouldReturnProductNotFoundView() throws Exception {
        when(productService.getProduct(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/catalog/details")
                    .param("id","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-not-found"))
                .andExpect(model().attributeDoesNotExist("product"));

        verify(productService, times(1)).getProduct(1L);

    }


    @Test
    @DisplayName("GET /catalog/updateScreen?id=[productID] - should return product-not-found view if product doesn't exist")
    void showUpdateProductScreen_ShouldReturnProductNotFoundView() throws Exception {
        when(productService.getProduct(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/catalog/updateScreen")
                    .param("id","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-not-found"))
                .andExpect(model().attributeDoesNotExist("product"));

        verify(productService, times(1)).getProduct(1L);
    }

    @Test
    @DisplayName("GET /catalog/details?id=[productID] - should return product-details view if it exists")
    void showProductDetails_ShouldReturnProductDetailsView() throws Exception {
        Product product1 = createTestProduct(
                1L,
                "Strawberry Jam",
                "Tests of the homeland",
                "1kg",
                new BigDecimal("123.45")
        );

        when(productService.getProduct(1L)).thenReturn(Optional.of(product1));

        mockMvc.perform(get("/catalog/details")
                    .param("id","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-details"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", hasProperty("name", is("Strawberry Jam"))));

        verify(productService, times(1)).getProduct(1L);
    }

    @Test
    @DisplayName("POST /catalog/update - should update product and redirect")
    void showUpdateProductScreen_ShouldRedirectToProductUpdateScreen() throws Exception {
        Product product1 = createTestProduct(
                1L,
                "Strawberry Jam",
                "Tests of the homeland",
                "1kg",
                new BigDecimal("123.45")
        );

        when(productService.getProduct(1L)).thenReturn(Optional.of(product1));

        mockMvc.perform(post("/catalog/update")
                    .param("id","1")
                    .param("name","Strawberry Jam")
                    .param("description","Tests of the Editland")
                    .param("size","1kg")
                    .param("price","123.45"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog/welcome"));

        verify(productService, times(1)).getProduct(1L);
        verify(productService, times(1)).updateProduct(eq(product1), any());
    }

    @Test
    @DisplayName("POST /catalog/deleteProduct - should delete event and redirect")
    void showDeleteProductScreen_ShouldRedirectToWelcomeScreen() throws Exception {
        mockMvc.perform(post("/catalog/deleteProduct")
                    .param("id","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog/welcome"));
    }


}
