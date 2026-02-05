package hr.algebra.jsmalc.online_retial_project.controller;

import hr.algebra.jsmalc.online_retial_project.domain.Product;
import hr.algebra.jsmalc.online_retial_project.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("catalog/")
public class WelcomeScreenController {

    private final ProductService productService;

    public WelcomeScreenController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("welcome")
    public String showWelcomeScreen(Model model) {
        model.addAttribute("products", productService.findAll());
        return "welcome";
    }

    @GetMapping("details")
    public String showProductDetails(Model model, @RequestParam Long id) {
        Optional<Product> productOptional = productService.getProduct(id);
        productOptional.ifPresent(model::addAttribute);
        return "product-details";
    }

    @GetMapping("updateScreen")
    public String showProductUpdateScreen(Model model, @RequestParam Long id) {
        Optional<Product> productOptional = productService.getProduct(id);
        productOptional.ifPresent(model::addAttribute);
        return "product-edit";
    }

    @PostMapping("update")
    public String updateProduct(@ModelAttribute("product") Product productToUpdate) {
        Optional<Product> productOptional = productService.getProduct(productToUpdate.getId());

        productOptional.ifPresent(product -> productService.updateProduct(product, productToUpdate));
        return "redirect:/catalog/welcome";
    }

    @PostMapping("deleteProduct")
    public String deleteProduct(@RequestParam Long id) {
        productService.deleteProduct(id);
        return "redirect:/catalog/welcome";
    }

    @GetMapping("newProductScreen")
    public String showNewProductScreen(Model model) {
        model.addAttribute("product", new Product());
        return "create-new-product";
    }
}
