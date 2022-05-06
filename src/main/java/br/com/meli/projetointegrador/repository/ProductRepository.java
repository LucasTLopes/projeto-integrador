package br.com.meli.projetointegrador.repository;

import br.com.meli.projetointegrador.dto.ProductDTOi;
import br.com.meli.projetointegrador.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select p.id, p.name, sum(b.current_quantity) as quantity from product p inner join batch b on p.id=b.product_id group by p.id", nativeQuery = true)
    List<ProductDTOi> findAllByBatchListExists();

    @Query(value = "select p.id, p.name, sum(b.current_quantity) as quantity from product p inner join batch b on p.id=b.product_id   " +
            "join section s on s.id=b.section_id where s.category=?1 group by p.id", nativeQuery = true)
    List<ProductDTOi> findAllByBatchListExistsBySection(String category);

    Product findAllById(Long id);
}
