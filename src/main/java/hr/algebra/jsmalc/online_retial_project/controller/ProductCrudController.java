package hr.algebra.jsmalc.online_retial_project.controller;

import hr.algebra.jsmalc.online_retial_project.domain.Product;
import hr.algebra.jsmalc.online_retial_project.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;

@Controller
@RequestMapping("catalog/")
public class ProductCrudController {
    private final ProductRepository productRepository;

    public ProductCrudController(ProductRepository productRepository) {this.productRepository = productRepository;}

    @PostMapping("/create")
    public String createNewProduct(Model model, @ModelAttribute("product") Product product) {
        Long lastID = productRepository.findAll().stream().max(Comparator.comparing(Product::getId)).get().getId();
        product.setId(lastID + 1);
        productRepository.addProduct(product);
        return "redirect:/catalog/welcome";
    }
}
