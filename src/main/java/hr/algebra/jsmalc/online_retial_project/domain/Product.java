package hr.algebra.jsmalc.online_retial_project.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String manufacturer;
    private String size;
    private Double price;
}
