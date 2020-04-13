package com.jovana.service.integration;

import com.jovana.entity.product.Product;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.entity.product.dto.UpdateStockRequest;
import com.jovana.entity.shippingaddress.ShippingAddress;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.repositories.product.ProductRepository;
import com.jovana.repositories.product.StockRepository;
import com.jovana.service.impl.product.ProductService;
import com.jovana.service.integration.auth.WithMockCustomUser;
import com.jovana.service.util.RequestTestDataProvider;
import org.flywaydb.test.annotation.FlywayTest;
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

        @WithMockCustomUser
        @DisplayName("Then Product is fetched from database when id is valid")
        @Test
        public void testGetProductById() {
            // prepare
            Long TEST_PRODUCT_ID = 10L;
            // exercise
            Product product = productService.getProductById(TEST_PRODUCT_ID);
            // verify
            assertNotNull(product, "Product is null");
            assertNotNull(product.getStock(), "Stock is null");
        }

    }

    @DisplayName("When we want to add a new product")
    @Nested
    class AddProductTest {

        @WithMockCustomUser(username = "admin", authorities = {"ROLE_ADMIN"})
        @DisplayName("Then product is created when valid ProductRequest is passed")
        @Test
        public void testAddProductSuccess() {
            // prepare
            ProductRequest productRequest = RequestTestDataProvider.getProductRequests().get("casualDress");

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

    @DisplayName("When we want to add a new product")
    @Nested
    class UpdateProductStockTest {

        private final Long TEST_PRODUCT_ID = 11L;

        @WithMockCustomUser(username = "admin", authorities = {"ROLE_ADMIN"})
        @DisplayName("Then product is created when valid ProductRequest is passed")
        @Test
        public void testUpdateProductStockSuccess() {
            // prepare
            UpdateStockRequest updateStockRequest = RequestTestDataProvider.getStockRequests().get("updateStockRequest");

            Product productBefore = productService.getProductById(TEST_PRODUCT_ID);
            assertEquals(1L, productBefore.getStock().getNumberOfUnitsInStock());

            // exercise
            Long numberOfProductsInStock = productService.updateProductStock(TEST_PRODUCT_ID, updateStockRequest);

            // verify
            Product productAfter = productService.getProductById(TEST_PRODUCT_ID);
            assertEquals(updateStockRequest.getNumberOfUnitsInStock(), productAfter.getStock().getNumberOfUnitsInStock());
            assertEquals(updateStockRequest.getNumberOfUnitsInStock(), numberOfProductsInStock);
        }
    }

    @DisplayName("When we want to update product")
    @Nested
    class UpdateProductTest {

        private final Long TEST_PRODUCT_ID = 12L;

        @WithMockCustomUser(username = "admin", authorities = {"ROLE_ADMIN"})
        @DisplayName("Then product is updated when valid ProductRequest is passed")
        @Test
        public void testUpdateProductSuccess() {
            // prepare
            ProductRequest updateProductRequest = RequestTestDataProvider.getProductRequests().get("updateRequest");

            // exercise
            productService.updateProduct(TEST_PRODUCT_ID, updateProductRequest);

            // verify
            Product updatedProduct = productService.getProductById(TEST_PRODUCT_ID);

            assertAll("Verify updated product",
                    () -> assertEquals(updatedProduct.getName(), updateProductRequest.getName()),
                    () -> assertEquals(updatedProduct.getMaterial(), updateProductRequest.getMaterial()),
                    () -> assertEquals(updatedProduct.getDescription(), updateProductRequest.getDescription()),
                    () -> assertEquals(updatedProduct.getPrice(), updateProductRequest.getPrice()),
                    () -> assertEquals(updatedProduct.getSizes().size(), updateProductRequest.getSizes().size()),
                    () -> assertEquals(updatedProduct.getColors().size(), updateProductRequest.getColors().size()),
                    () -> assertEquals(updatedProduct.getStock().getNumberOfUnitsInStock(), updateProductRequest.getNumberOfUnitsInStock())
            );
        }
    }

}
