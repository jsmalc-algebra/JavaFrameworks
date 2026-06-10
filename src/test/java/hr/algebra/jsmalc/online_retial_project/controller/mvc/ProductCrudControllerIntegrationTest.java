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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @DisplayName("POST /catalog/create - should redirect the user to the page with the list of all products" +
            "after successful creation of a product")
    void createNewProduct_ShouldRedirectToWelcomeScreen() throws Exception {
        mockMvc.perform(post("/catalog/create")
                        .param("name","Strawberry Jam")
                        .param("description","Tests of the Homeland")
                        .param("size","1kg")
                        .param("price","123.45")
                        .param("currentStock","100")
                        .param("lowStockThreshold","50")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog/welcome"));
    }



}
