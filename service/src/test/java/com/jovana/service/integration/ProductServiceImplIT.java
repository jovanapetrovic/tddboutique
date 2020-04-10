package com.jovana.service.integration;

import com.jovana.entity.product.Product;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.repositories.product.ProductRepository;
import com.jovana.repositories.product.StockRepository;
import com.jovana.service.impl.product.ProductService;
import com.jovana.service.integration.auth.WithMockCustomUser;
import com.jovana.service.util.RequestTestDataProvider;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by jovana on 07.04.2020
 */
@FlywayTest(locationsForMigrate = {"db.additional.product"})
public class ProductServiceImplIT extends AbstractTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;

    @DisplayName("When we want to find a Product by id")
    @Nested
    class GetProductTest {

        private final Long PRODUCT_ID_EXISTS = 10L;

        @WithMockCustomUser
        @DisplayName("Then Product is fetched from database when id is valid")
        @Test
        public void testGetProductById() {
            // exercise
            Product product = productService.getProductById(PRODUCT_ID_EXISTS);
            // verify
            assertNotNull(product, "Product is null");
            assertNotNull(product.getStock(), "Stock is null");
        }

    }

    @WithMockCustomUser
    @DisplayName("When we want to add a new product")
    @Nested
    class AddProductTest {

        private ProductRequest productRequest;

        @BeforeEach
        void setUp() {
            // set requests
            productRequest = RequestTestDataProvider.getProductRequests().get("casualDress");
        }

        @WithMockCustomUser(username = "admin")
        @DisplayName("Then product is created when valid ProductRequest is passed")
        @Test
        public void testAddProductSuccess() {
            // exercise
            Long productId = productService.addProduct(productRequest);

            // verify
            Product newProduct = productService.getProductById(productId);

            assertAll("Verify new product",
                    () -> assertNotNull(newProduct),
                    () -> assertNotNull(newProduct.getName()),
                    () -> assertEquals(newProduct.getName(), productRequest.getName()),
                    () -> assertNotNull(newProduct.getMaterial()),
                    () -> assertEquals(newProduct.getMaterial(), productRequest.getMaterial()),
                    () -> assertNotNull(newProduct.getDescription()),
                    () -> assertEquals(newProduct.getDescription(), productRequest.getDescription()),
                    () -> assertNotNull(newProduct.getPrice()),
                    () -> assertEquals(newProduct.getPrice(), productRequest.getPrice()),
                    () -> assertNotNull(newProduct.getSizes()),
                    () -> assertEquals(newProduct.getSizes().size(), productRequest.getSizes().size()),
                    () -> assertNotNull(newProduct.getColors()),
                    () -> assertEquals(newProduct.getColors().size(), productRequest.getColors().size()),
                    () -> assertNotNull(newProduct.getStock()),
                    () -> assertEquals(newProduct.getStock().getNumberOfUnitsInStock(), productRequest.getNumberOfUnitsInStock())
            );
        }
    }

}
