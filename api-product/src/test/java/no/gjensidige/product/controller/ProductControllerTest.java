package no.gjensidige.product.controller;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getProducts() {
        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<ProductDTO> productList = new ArrayList<>();
        uniqueNames.forEach(name -> {
            ProductDTO p = new ProductDTO();
            p.setProductName(name);
            productList.add(p);
        });

        when(productService.getAllProducts()).thenReturn(productList);

        ResponseEntity<List<ProductDTO>> response = productController.getProducts();

        List<ProductDTO> productList1 = response.getBody();

        verify(productService).getAllProducts();

        assertEquals(3, productList1.size());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void getProduct() {
        ProductDTO p = new ProductDTO();
        p.setId(1l);

        when(productService.getProduct(1l)).thenReturn(p);

        ResponseEntity<ProductDTO> response = productController.getProduct(1l);
        ProductDTO product = response.getBody();

        verify(productService).getProduct(1l);
        assertEquals(1l, product.getId().longValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void createProduct() {
    }

    @Test
    public void updateProduct() {
    }

    @Test
    public void deleteProduct() {

        ProductDTO p = new ProductDTO();
        p.setId(1l);

        when(productService.deleteProduct(1l)).thenReturn(p);

        ResponseEntity<ProductDTO> response = productController.deleteProduct(1l);
        ProductDTO product = response.getBody();

        verify(productService).deleteProduct(1l);

        assertEquals(1l, product.getId().longValue());

    }
}