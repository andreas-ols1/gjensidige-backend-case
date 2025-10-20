package no.gjensidige.product.service;

import java.util.List;

import org.modelmapper.ModelMapper;
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

    // Changed from Autowired to constructor injection
    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    public ProductService(ProductRepository pr, ModelMapper mm) {
        this.productRepository = pr;
        this.modelMapper = mm;
    }

    public List<ProductDTO> getAllProducts() {

        return productRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public ProductDTO getProduct(Long id) {

        Product p = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        return convertToDTO(p);

    }

    // @Todo create delete functionality
    public ProductDTO deleteProduct(Long id) {

        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(existingProduct);

        return convertToDTO(existingProduct);
    }

    public ProductDTO createProduct(ProductDTO inputProduct) {

        Product product = convertToEntity(inputProduct);

        product = productRepository.save(product);

        return convertToDTO(product);
    }

    // @Todo create update functionality
    public ProductDTO updateProduct(Long id, ProductDTO inputProduct) {

        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        // SkipNullEnabled in ProductApp.java, so mapper skips null entries in DTO
        modelMapper.map(inputProduct, existingProduct);

        existingProduct = productRepository.save(existingProduct);

        return convertToDTO(existingProduct);
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
