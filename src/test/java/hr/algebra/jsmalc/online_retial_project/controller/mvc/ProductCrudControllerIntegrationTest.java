package hr.algebra.jsmalc.online_retial_project.controller.mvc;

import hr.algebra.jsmalc.online_retial_project.service.JwtService;
import hr.algebra.jsmalc.online_retial_project.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(ProductCrudController.class)
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@Import(SecurityAutoConfiguration.class)
public class ProductCrudControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;


    @Test
    @DisplayName("POST /catalog/newProductScreen - should redirect the user to the page with the list of all products" +
            "after sucessful creation of a product")
    void createNewProduct_ShouldRedirectToWelcomeScreen() throws Exception {
        mockMvc.perform(post("/catalog/newProductScreen"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog/welcome"));
    }



}
