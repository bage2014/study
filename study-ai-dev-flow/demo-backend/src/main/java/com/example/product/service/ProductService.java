package com.example.product.service;

import com.example.product.exception.NotFoundException;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Creates a new product.
     *
     * @param product the product to create
     * @return the created product with generated ID
     */
    public Product createProduct(Product product) {
        logger.debug("Creating product: {}", product.getName());
        Product saved = productRepository.save(product);
        logger.info("Product created with ID: {}", saved.getId());
        return saved;
    }

    /**
     * Retrieves all products.
     *
     * @return list of all products
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        logger.debug("Fetching all products");
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the product ID
     * @return the product if found
     * @throws NotFoundException if product not found
     */
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        logger.debug("Fetching product with ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    /**
     * Updates an existing product.
     *
     * @param id             the ID of the product to update
     * @param productDetails the updated product details
     * @return the updated product
     * @throws NotFoundException if product not found
     */
    public Product updateProduct(Long id, Product productDetails) {
        logger.debug("Updating product with ID: {}", id);
        Product existingProduct = getProductById(id);
        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setQuantity(productDetails.getQuantity());
        Product updated = productRepository.save(existingProduct);
        logger.info("Product updated with ID: {}", updated.getId());
        return updated;
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     * @throws NotFoundException if product not found
     */
    public void deleteProduct(Long id) {
        logger.debug("Deleting product with ID: {}", id);
        Product product = getProductById(id);
        productRepository.delete(product);
        logger.info("Product deleted with ID: {}", id);
    }
}
