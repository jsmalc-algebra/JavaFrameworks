package hr.algebra.jsmalc.online_retial_project.controller;

import hr.algebra.jsmalc.online_retial_project.domain.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("catalog/")
public class WelcomeScreenController {

    private List<Product> products;

    public WelcomeScreenController() {
        products = new ArrayList<>();
        products.add(new Product(
                1L,
                "Liquid yogurt XXL",
                "Pilos",
                "1.5kg",
                1.69
                ));
        products.add(new Product(
                2L,
                "Crunchy peanut butter XXL",
                "McKennedy",
                "1kg",
                5.79


        ));
        products.add(new Product(
                3L,
                "Homemade burek with cheese",
                "Store bakery",
                "220g",
                0.85
        ));
        products.add(new Product(
                4L,
                "Mayo",
                "STAR",
                "165g",
                1.89
        ));
        products.add(new Product(
                5L,
                "Long-life milk XXL",
                "Ducat",
                "8x1L",
                6.99
        ));
    }

    @GetMapping("welcome")
    public String showWelcomeScreen(Model model) {
        model.addAttribute("products", products);
        return "welcome";
    }

    @GetMapping("details")
    public String showProductDetails(Model model, @RequestParam Long id) {
        Product product = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("product", product);
        return "product-details";
    }
}
