package br.com.meli.projetointegrador.service;

import br.com.meli.projetointegrador.dto.ProductByBatchResponse;
import br.com.meli.projetointegrador.dto.ProductDTOi;
import br.com.meli.projetointegrador.model.Product;

import java.util.List;
/**
 * Interface de serviço responsável por processar dados de Product.
 * @author Jeferson Barbosa Souza
 * @author Lucas Troleiz Lopes
 */
public interface ProductService {
    Product findById(Long id);

    List<ProductByBatchResponse> getAllProductThatHaveBatch(Long id, String orderBy);

    List<ProductDTOi> findAllByBatchListExists();

    List<ProductDTOi> findAllByBatchListExistsBySection(String category);

    Integer getTotalQuantity(Long id);

    List<Product> findAll();

    Product save(Product product);

}
