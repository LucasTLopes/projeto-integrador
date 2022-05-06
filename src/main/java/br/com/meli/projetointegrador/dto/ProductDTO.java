package br.com.meli.projetointegrador.dto;

import br.com.meli.projetointegrador.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private Double width;
    private Double height;

    public static ProductDTO map(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getWidth(), product.getHeight());
    }
    public static Product map(ProductDTO productDTO) {
        return new Product(productDTO.getName(), productDTO.getPrice(), productDTO.getWidth(), productDTO.getHeight());
    }
    public static List<ProductDTO> map(List<Product> productList) {
        return productList.stream().map(ProductDTO::map).collect(Collectors.toList());
    }
}