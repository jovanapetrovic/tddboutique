package com.jovana.service.impl.product;

import com.jovana.entity.product.Product;
import com.jovana.entity.product.Stock;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.entity.product.dto.UpdateStockRequest;
import com.jovana.entity.product.exception.ProductNameAlreadyExistsException;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.product.ProductRepository;
import com.jovana.repositories.product.StockRepository;
import com.jovana.service.security.IsAdmin;
import com.jovana.service.security.IsAdminOrUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        product.setSizes(productRequest.getSizes());
        product.setColors(productRequest.getColors());
        product.setStock(productRequest.getNumberOfUnitsInStock());

        Product newProduct = productRepository.save(product);
        return newProduct.getId();
    }

    @Override
    public Long updateProductStock(Long productId, UpdateStockRequest updateStockRequest) {
        Product product = getProductById(productId);
        Stock stock = stockRepository.findByProductId(updateStockRequest.getNumberOfUnitsInStock());
        if (stock != null) {
            stock.setNumberOfUnitsInStock(updateStockRequest.getNumberOfUnitsInStock());
        } else {
            stock = new Stock(product, updateStockRequest.getNumberOfUnitsInStock());
        }
        Stock updatedStock = stockRepository.save(stock);
        return updatedStock.getId();
    }

    private void validateProductName(String name) {
        if (productRepository.findByName(name) != null) {
            throw new ProductNameAlreadyExistsException(name, "Product already exists in the db with this name.");
        }
    }
}
