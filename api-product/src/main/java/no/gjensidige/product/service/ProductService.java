package no.gjensidige.product.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.repository.ProductRepository;

/**
 * ProductService
 *
 * Class responsible of data manipulation between dto and entity
 *
 *
 */

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    public Product getProduct(Long id) {

        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

    }

    // @Todo create delete functionality
    public Product deleteProduct(Long id) {

        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(existingProduct);

        return existingProduct;
    }

    public Product createProduct(ProductDTO inputProduct) {

        Product product = convertToEntity(inputProduct);

        return productRepository.save(product);
    }

    // @Todo create update functionality
    public Product updateProduct(Long id, ProductDTO inputProduct) {

        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        // SkipNullEnabled in ProductApp.java, so mapper skips null entries in DTO
        modelMapper.map(inputProduct, existingProduct);

        return productRepository.save(existingProduct);
    }

    public ProductDTO convertToDTO(Product product) {

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        return productDTO;
    }

    public Product convertToEntity(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);

        return product;
    }

}
