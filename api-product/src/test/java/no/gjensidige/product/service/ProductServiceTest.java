package no.gjensidige.product.service;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
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
        MockitoAnnotations.openMocks(this);

        when(modelMapper.map(any(Product.class), any())).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            return mm.map(product, invocation.getArgument(1));
        });

        when(modelMapper.map(any(ProductDTO.class), any())).thenAnswer(invocation -> {
            ProductDTO dto = invocation.getArgument(0);
            return mm.map(dto, invocation.getArgument(1));
        });
    }

    @Test
    public void getAllProducts() {
        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<Product> productList = new ArrayList<>();
        uniqueNames.forEach(name -> {
            Product p = new Product();
            p.setProductName(name);
            p.setUnitPrice(100.0);
            productList.add(p);
        });

        when(productRepository.findAll()).thenReturn(productList);

        List<ProductDTO> result = productService.getAllProducts();

        verify(productRepository).findAll();
        assertEquals(3, result.size());
        assertEquals(productList.get(0).getProductName(), result.get(0).getProductName());

    }

    @Test
    public void getProduct() {
        Product p = new Product();
        p.setId(1L);
        p.setProductName("Test Product");
        p.setUnitPrice(99.99);
        Optional<Product> op = Optional.of(p);

        when(productRepository.findById(anyLong())).thenReturn(op);

        ProductDTO result = productService.getProduct(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getProductName());
        assertEquals(99.99, result.getUnitPrice());
    }

    @Test
    public void deleteProduct() {
        Product p = new Product();
        p.setId(1L);
        p.setProductName("To Delete");
        p.setUnitPrice(50.0);
        Optional<Product> op = Optional.of(p);

        when(productRepository.findById(anyLong())).thenReturn(op);
        ProductDTO result = productService.deleteProduct(1L);

        verify(productRepository).delete(p);
        assertNotNull(result);
        assertEquals("To Delete", result.getProductName());
    }

    @Test
    public void deleteProductWithException() {
        Optional<Product> op = Optional.empty();
        when(productRepository.findById(anyLong())).thenReturn(op);

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

        ProductDTO productDTO = productService.convertToDTO(product);

        assertNotNull(productDTO);
        assertEquals("Seagate Baracuda 500GB", productDTO.getProductName());
        assertEquals(55.50, productDTO.getUnitPrice());
    }

    @Test
    public void convertToEntity() {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory("Hardware");
        productDTO.setProductName("Seagate Baracuda 500GB");
        productDTO.setNumberSold(BigInteger.valueOf(200));
        productDTO.setUnitPrice(55.50);

        Product product = productService.convertToEntity(productDTO);

        assertEquals(product.getProductName(), productDTO.getProductName());
        assertEquals(product.getNumberSold(), productDTO.getNumberSold());
        assertEquals(product.getCategory(), productDTO.getCategory());

    }
}