package br.com.meli.projetointegrador.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Double width;
    private Double height;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<Batch> batchList = new ArrayList<>();

    public Product(String name, Double price, Double width, Double height) {
        this.name = name;
        this.price = price;
        this.width = width;
        this.height = height;
    }
}
