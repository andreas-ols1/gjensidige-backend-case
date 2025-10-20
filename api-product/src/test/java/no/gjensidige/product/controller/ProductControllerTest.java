package no.gjensidige.product.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private MockMvc mockMvc;

    private ProductController productController;

    @Mock
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productController = new ProductController(productService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void getProducts() throws Exception {
        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<ProductDTO> productList = new ArrayList<>();
        uniqueNames.forEach(name -> {
            ProductDTO mockProduct = createMockProduct(1L);
            mockProduct.setProductName(name);
            productList.add(mockProduct);
        });

        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        verify(productService).getAllProducts();

    }

    @Test
    public void getProduct() throws Exception {
        ProductDTO mockProduct = createMockProduct(1L);

        when(productService.getProduct(1L)).thenReturn(mockProduct);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(productService).getProduct(1l);
    }

    @Test
    public void createProduct() throws Exception {

        ProductDTO mockProduct = createMockProduct(1L);
        when(productService.createProduct(any(ProductDTO.class)))
                .thenReturn(mockProduct);

        mockMvc.perform(post("/products")
                .contentType("application/json")
                .content(
                        "{\"productName\":\"Test Product 1\",\"unitPrice\":99.99,\"unitCost\":49.99,\"numberSold\":10}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        "http://localhost/products/1"))
                .andExpect(jsonPath("$.id").value(1L));

        verify(productService).createProduct(any(ProductDTO.class));
    }

    @Test
    public void updateProduct() throws Exception {

        ProductDTO updatedProduct = createMockProduct(1L);
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setNumberSold(BigInteger.valueOf(1000));

        when(productService.updateProduct(anyLong(), any(ProductDTO.class)))
                .thenReturn(updatedProduct);

        mockMvc.perform(put("/products/1")
                .contentType("application/json")
                .content(
                        "{\"productName\":\"Updated Product\",\"numberSold\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productName").value("Updated Product"))
                .andExpect(jsonPath("$.numberSold").value(1000))
                .andExpect(jsonPath("$.unitCost").value(49.99))
                .andExpect(jsonPath("$.unitPrice").value(99.99));

        verify(productService).updateProduct(anyLong(), any(ProductDTO.class));
    }

    @Test
    public void deleteProduct() throws Exception {

        ProductDTO mockProduct = createMockProduct(1L);

        when(productService.deleteProduct(1l)).thenReturn(mockProduct);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(productService).deleteProduct(1l);
        ;
    }

    private ProductDTO createMockProduct(Long id) {
        ProductDTO dto = new ProductDTO();
        dto.setId(id);
        dto.setProductName("Test Product " + id);
        dto.setUnitPrice(99.99);
        dto.setUnitCost(49.99);
        dto.setNumberSold(BigInteger.valueOf(10));
        return dto;
    }
}