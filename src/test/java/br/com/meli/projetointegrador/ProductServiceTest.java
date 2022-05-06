package br.com.meli.projetointegrador;

import br.com.meli.projetointegrador.dto.ProductByBatchResponse;
import br.com.meli.projetointegrador.dto.ProductByBatchResponseImpl;
import br.com.meli.projetointegrador.dto.ProductDTOi;
import br.com.meli.projetointegrador.dto.ProductDTOiImpl;
import br.com.meli.projetointegrador.exception.InexistentProductException;
import br.com.meli.projetointegrador.model.*;
import br.com.meli.projetointegrador.repository.BatchRepository;
import br.com.meli.projetointegrador.repository.ProductRepository;
import br.com.meli.projetointegrador.service.BatchService;
import br.com.meli.projetointegrador.service.ProductService;
import br.com.meli.projetointegrador.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private BatchService batchService;

    @Mock
    private BatchRepository batchRepository;

    @BeforeEach
    private void initializeProductService() {
        MockitoAnnotations.openMocks(this);
        this.productService = new ProductServiceImpl(productRepository, batchRepository, batchService);
    }

    @Test
    public void findByIdTest() {
        Mockito.when(productRepository.findById(Mockito.any()))
                .thenReturn(java.util.Optional.of(new Product(1L, "Product 1", 20.0, 2.0, 2.0, Collections.singletonList(new Batch()))));

        assertEquals("Product 1", productService.findById(1L).getName());
    }

    @Test
    public void inexistentProductException(){
        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(InexistentProductException.class, () -> productService.findById(1L));
    }

    @Test
    public void getProductsThatHaveBatch() {
        List<ProductByBatchResponse> listProductBatch = Collections.singletonList(
                new ProductByBatchResponseImpl(BigInteger.ONE, 6.0,"Shampoo", 17.5, 5.0,
                        BigInteger.TWO, "2022-02-02", 20)
        );
        List<ProductByBatchResponse> listProductQuantity = Collections.singletonList(
                new ProductByBatchResponseImpl(BigInteger.ONE, 17.5, "Shampoo",5.0, 6.0, BigInteger.valueOf(3),
                        "2023-02-02", 50)
        );
        List<ProductByBatchResponse> listProductExpiration = Collections.singletonList(
                new ProductByBatchResponseImpl(BigInteger.ONE,17.5,"Shampoo" ,5.0, 6.0, BigInteger.valueOf(4),
                        "2024-08-08", 30)
        );

        Mockito.when(batchRepository.getAllProductThatHaveBatch(Mockito.anyLong())).thenReturn(listProductBatch);
        Mockito.when(batchRepository.getAllProductThatHaveBatchQuantity(Mockito.anyLong())).thenReturn(listProductQuantity);
        Mockito.when(batchRepository.getAllProductThatHaveBatchExpiration(Mockito.anyLong())).thenReturn(listProductExpiration);

        assertAll(
                () -> assertEquals(BigInteger.TWO, productService.getAllProductThatHaveBatch(1L, "L").get(0).getBatchId()),
                () -> assertEquals(50, productService.getAllProductThatHaveBatch(1L, "C").get(0).getCurrentQuantity()),
                () -> assertEquals("2024-08-08",
                        productService.getAllProductThatHaveBatch(1L, "F").get(0).getExpirationDate()),
                () -> assertTrue(productService.getAllProductThatHaveBatch(1L, "XYZW").isEmpty())
        );
    }

    @Test
    public void findAllProductsTest(){
        List<Product> products = Arrays.asList(
                new Product(1L, "Tomato", 2.00, 5.0, 4.0, Collections.singletonList(new Batch())),
                new Product(2L, "Meat", 40.00, 10.0, 10.0, Collections.singletonList(new Batch())));

        Mockito.when(productRepository.findAll()).thenReturn(products);

        assertEquals(products.size(), productService.findAll().size());
    }

    @Test
    public void findAllByBatchListExistsTest(){
        List<ProductDTOi> productDTOis = Arrays.asList(
                new ProductDTOiImpl(1L, "Tomato", 45),
                new ProductDTOiImpl(2L, "Meat", 30));

        Mockito.when(productRepository.findAllByBatchListExists()).thenReturn(productDTOis);

        assertEquals(productDTOis.size(), productService.findAllByBatchListExists().size());
    }

    @Test
    public void findAllByBatchListExistsBySectionTest(){
        List<ProductDTOi> productDTOis = Arrays.asList(
                new ProductDTOiImpl(1L, "Chicken", 45),
                new ProductDTOiImpl(2L, "Meat", 30));

        Mockito.when(productRepository.findAllByBatchListExistsBySection(Mockito.any())).thenReturn(productDTOis);

        assertEquals(productDTOis.size(), productService.findAllByBatchListExistsBySection("FROZEN").size());
    }

    @Test
    public void getTotalQuantity(){
        Product productTomato= new Product(1L, "Tomato", 2.00, 5.0, 4.0, Collections.singletonList(new Batch()));
        Product productMeat= new Product(2L, "Meat", 40.00, 10.0, 10.0, Collections.singletonList(new Batch()));

        Section section1 = new Section(1L, "Section 1", Category.FRESH, 6, 6, new Warehouse(), Collections.singletonList(new Batch()));
        Section section2 = new Section(2L, "Section 2", Category.FROZEN, 6, 6, new Warehouse(), Collections.singletonList(new Batch()));

        List<Batch> batchList = Arrays.asList(
                new Batch(1L, 15.0, 12.0, 200, 200,
                        LocalDate.of(2022, 1, 1), LocalDateTime.of(2022, 1, 1, 0, 0),
                        LocalDate.of(2022, 2, 2), productTomato, new InboundOrder(), section1),
                new Batch(2L, 0.0, -15.0, 50, 50,
                        LocalDate.of(2022, 3, 3), LocalDateTime.of(2022, 1, 3, 0, 0),
                        LocalDate.of(2022, 4, 4), productMeat, new InboundOrder(), section2));

        Mockito.when(batchService.getBatchesWithExpirationDateGreaterThan3Weeks(Mockito.any())).thenReturn(Collections.singletonList(batchList.get(0)));

        assertEquals(200, productService.getTotalQuantity(1L));
    }


    @Test
    public void Catalog(){
        List<Product> products = Arrays.asList(
                new Product( "Tomato", 2.00, 5.0, 4.0),
                new Product("Meat", 40.00, 10.0, 10.0));

        Mockito.when(productRepository.findAll()).thenReturn(products);

        assertEquals(products.size(), productService.findAll().size());
    }
    private Product generateProduct(){
        return   new Product( "Tomato", 2.00, 5.0, 4.0);
    }

    @Test
    public void saveTest() {
        Product product = generateProduct();
        Mockito.when(productRepository.save(Mockito.any())).thenReturn((product));
        assertEquals(product.getId(), productService.save(product).getId());
    }

}
