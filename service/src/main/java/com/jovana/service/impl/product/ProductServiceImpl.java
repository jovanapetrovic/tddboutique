package com.jovana.service.impl.product;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jovana.entity.product.ColorCode;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.SizeCode;
import com.jovana.entity.product.dto.ProductFullResponse;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.entity.product.dto.ProductResponse;
import com.jovana.entity.product.dto.UpdateStockRequest;
import com.jovana.entity.product.exception.ProductNameAlreadyExistsException;
import com.jovana.entity.product.image.Image;
import com.jovana.entity.product.image.dto.ImageResponse;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.product.ProductRepository;
import com.jovana.repositories.product.StockRepository;
import com.jovana.service.security.IsAdmin;
import com.jovana.service.security.IsAdminOrUser;
import com.jovana.service.security.IsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by jovana on 07.04.2020
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;

    @IsAdminOrUser
    @Override
    public Product getProductById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            LOGGER.info("Product with id = {} was not found in the db.", productId);
            throw new EntityNotFoundException("No product found with id = " + productId);
        }
        return product.get();
    }

    @IsAdmin
    @Override
    public Long addProduct(ProductRequest productRequest) {
        validateProductName(productRequest.getName());

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setMaterial(productRequest.getMaterial());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setSizes(SizeCode.getSizeCodesFromSizeStrings(productRequest.getSizes()));
        product.setColors(ColorCode.getColorCodesFromColorStrings(productRequest.getColors()));
        product.setStock(productRequest.getNumberOfUnitsInStock());

        Product newProduct = productRepository.save(product);
        return newProduct.getId();
    }

    @IsAdmin
    @Override
    public Long updateProduct(Long productId, ProductRequest productRequest) {
        Product product = getProductById(productId);

        if (!product.getName().equals(productRequest.getName())) {
            validateProductName(productRequest.getName());
        }

        product.setName(productRequest.getName());
        product.setMaterial(productRequest.getMaterial());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setSizes(SizeCode.getSizeCodesFromSizeStrings(productRequest.getSizes()));
        product.setColors(ColorCode.getColorCodesFromColorStrings(productRequest.getColors()));
        product.setStock(productRequest.getNumberOfUnitsInStock());

        Product updatedProduct = productRepository.save(product);
        return updatedProduct.getId();
    }

    @IsAdmin
    @Override
    public Long updateProductStock(Long productId, UpdateStockRequest updateStockRequest) {
        Product product = getProductById(productId);
        product.setStock(updateStockRequest.getNumberOfUnitsInStock());
        Product updatedProduct = productRepository.save(product);
        return updatedProduct.getStock().getNumberOfUnitsInStock();
    }

    @IsUser
    @Override
    public boolean validateAndProcessProductStock(Product product, Long productQuantity) {
        Long currentProductStock = product.getStock().getNumberOfUnitsInStock();
        if (productQuantity > currentProductStock) {
            return false;
        }

        product.setStock(currentProductStock - productQuantity);
        productRepository.save(product);
        return true;
    }

    @IsAdmin
    @Override
    public boolean deleteProduct(Long productId) {
        Product product = getProductById(productId);
        product.setDeleted(true);
        product.setStock(0L);
        Product deletedProduct = productRepository.save(product);
        return deletedProduct.isDeleted();
    }

    @IsAdminOrUser
    @Override
    public ProductFullResponse viewOneProduct(Long productId) {
        ProductFullResponse product = productRepository.findOneWithImages(productId);
        if (product == null) {
            throw new EntityNotFoundException("No product found with id = " + productId);
        }
        return product;
    }

    @IsAdminOrUser
    @Override
    public Set<ProductResponse> viewAllProducts() {
        Set<ProductResponse> products = productRepository.findAllWithImages();
        LOGGER.info("Found {} products.", products.size());
        return products;
    }


    private void validateProductName(String name) {
        if (productRepository.findByName(name) != null) {
            throw new ProductNameAlreadyExistsException(name, "Product already exists in the db with this name.");
        }
    }
}
