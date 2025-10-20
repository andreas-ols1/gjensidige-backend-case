package no.gjensidige.product.controller;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

/**
 *
 * RestController to handle CRUD operations for Products
 *
 */

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService ps) {
        this.productService = ps;
    }

    @GetMapping
    ResponseEntity<List<ProductDTO>> getProducts() {

        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<ProductDTO> getProduct(@PathVariable("id") Long id) {

        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PostMapping
    ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO inputProduct) {

        ProductDTO createdProduct = productService.createProduct(inputProduct);

        URI createdLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdProduct.getId()).toUri();

        return ResponseEntity.created(createdLocation).body(createdProduct);
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO inputProduct) {

        return ResponseEntity.ok(productService.updateProduct(id, inputProduct));

    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<ProductDTO> deleteProduct(@PathVariable("id") Long id) {

        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
