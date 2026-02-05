package hr.algebra.jsmalc.online_retial_project.controller;

import hr.algebra.jsmalc.online_retial_project.domain.Product;
import hr.algebra.jsmalc.online_retial_project.repository.ProductRepository;
import hr.algebra.jsmalc.online_retial_project.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("catalog/")
public class ProductCrudController {
    private final ProductService productService;

    public ProductCrudController(ProductService productService) { this.productService = productService; }

    @PostMapping("/create")
    public String createNewProduct(@ModelAttribute("product") Product product) {
        productService.addProduct(product);
        return "redirect:/catalog/welcome";
    }
}
