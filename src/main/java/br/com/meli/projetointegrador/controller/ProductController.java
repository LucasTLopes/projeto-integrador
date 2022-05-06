package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.dto.*;
import br.com.meli.projetointegrador.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.meli.projetointegrador.model.Category;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Classe controladora responsável por lidar com as rotas referentes a classe product.
 * Possui rotas para listagem de produtos.
 * @author Jeferson Barbosa Souza
 * @author Lucas Troleiz Lopes
 */
@RestController
@RequestMapping("/api/v1/fresh-products/")
public class ProductController {


    @Autowired
    private ProductService productService;

    @GetMapping("/product")
    @PreAuthorize("hasRole('ROLE_STOCK_MANAGER')")
    public ResponseEntity<List<ProductByBatchResponse>> getProductById(@RequestParam(name = "productId") Long productId,
                                                                       @RequestParam(name = "orderBy", required = false, defaultValue = "L") String orderBy) {

        return new ResponseEntity<>(productService.getAllProductThatHaveBatch(productId, orderBy), HttpStatus.OK);
    }

    /**
     * Método responsável por listar os produtos do repositorio
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public List<ProductDTOi> productListAll() {
        return productService.findAllByBatchListExists();
    }

    /**
     * Método responsável por listar os produtos do repositorio por categoria
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public List<ProductDTOi> productListAllByCategory(@RequestParam String category) {
        return productService.findAllByBatchListExistsBySection(category);
    }

    /**
     * Método responsável por listar os produtos em catalago do meli
     */
    @GetMapping("/catalog")
    @PreAuthorize("hasRole('ROLE_STOCK_MANAGER')")
    public List<ProductDTO> catalogListAll() {
        return ProductDTO.map(productService.findAll());
    }

    /**
     * Método responsável por inserir produto no catalago
     */
    @PostMapping("/catalog/add")
    @PreAuthorize("hasRole('ROLE_STOCK_MANAGER')")
    public ResponseEntity<ProductDTO> insertProductInCatalog(@Valid @RequestBody ProductDTO productDTO) {
        return  new ResponseEntity<>(ProductDTO.map(productService.save(ProductDTO.map(productDTO))), HttpStatus.CREATED);
    }

//    /**
//     * Método responsável por inserir produto no catalago
//     */
//    @PutMapping("/catalog/")
//    @PreAuthorize("hasRole('ROLE_STOCK_MANAGER')")
//    public ResponseEntity<ProductDTO> updateProductInCatalog(@@PathVariable Long productid) {
//        return new ResponseEntity<>(ProductDTO.map(productService.findById(productid)), HttpStatus.CREATED;
//    }


}