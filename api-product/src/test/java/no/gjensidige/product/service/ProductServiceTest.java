package no.gjensidige.product.service;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    public ModelMapper mm = new ModelMapper();

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    ModelMapper modelMapper;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllProducts() {

        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<Product> productList = new ArrayList<>();
        uniqueNames.forEach(name -> {
            Product p = new Product();
            p.setProductName(name);
            productList.add(p);
        });

        when(productRepository.findAll()).thenReturn(productList);

        List<Product> productList1 = productService.getAllProducts();

        verify(productRepository).findAll();

        assertEquals(3, productList1.size());

    }

    @Test
    public void getProduct() {
        Product p = new Product();
        p.setId(1L);
        Optional<Product> op = Optional.of(p);

        when(productRepository.findById(anyLong())).thenReturn(op);

        Product product = productService.getProduct(1l);

        assertEquals(p, product);
    }

    @Test
    public void deleteProduct() {
        Product p = new Product();
        p.setId(1L);
        Optional<Product> op = Optional.of(p);
        when(productRepository.findById(anyLong())).thenReturn(op);

        Product product = productService.deleteProduct(1l);
        verify(productRepository).delete(p);

        assertEquals(p, product);
    }

    @Test
    public void deleteProductWithException() {
        Optional<Product> op = Optional.empty();

        when(productRepository.findById(anyLong())).thenReturn(op);

        // JUnit 5 style of ExpectedException
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(10l));

        verify(productRepository).findById(10l);
    }

    @Test
    public void convertToDTO() {
        Product product = new Product();
        product.setCategory("Hardware");
        product.setProductName("Seagate Baracuda 500GB");
        product.setNumberSold(BigInteger.valueOf(200));
        product.setUnitPrice(55.50);

        when(modelMapper.map(product, ProductDTO.class)).thenReturn(mm.map(product, ProductDTO.class));
        ProductDTO productDTO = productService.convertToDTO(product);
    }

    @Test
    public void convertToEntity() {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory("Hardware");
        productDTO.setProductName("Seagate Baracuda 500GB");
        productDTO.setNumbersold(BigInteger.valueOf(200));
        productDTO.setPrice(55.50);

        when(modelMapper.map(productDTO, Product.class)).thenReturn(mm.map(productDTO, Product.class));
        Product product = productService.convertToEntity(productDTO);

        assertEquals(product.getProductName(), productDTO.getProductName());
        assertEquals(product.getNumberSold(), productDTO.getNumberSold());
        assertEquals(product.getCategory(), productDTO.getCategory());

    }
}