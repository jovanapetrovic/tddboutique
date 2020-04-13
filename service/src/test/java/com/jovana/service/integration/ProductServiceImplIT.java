package com.jovana.service.integration;

import com.jovana.entity.product.Product;
import com.jovana.entity.product.dto.ProductFullResponse;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.entity.product.dto.ProductResponse;
import com.jovana.entity.product.dto.UpdateStockRequest;
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

import java.util.Set;

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

        @WithMockCustomUser
        @DisplayName("Then Product is fetched from database when id is valid")
        @Test
        public void testViewOneProductSuccess() {
            // prepare
            Long TEST_PRODUCT_ID = 10L;

            // exercise
            ProductFullResponse productResponse = productService.viewOneProduct(TEST_PRODUCT_ID);

            // verify
            Product product = productService.getProductById(TEST_PRODUCT_ID);

            assertAll("Verify product response",
                    () -> assertNotNull(productResponse),
                    () -> assertNotNull(productResponse.getId()),
                    () -> assertNotNull(productResponse.getName()),
                    () -> assertEquals(productResponse.getName(), product.getName()),
                    () -> assertNotNull(productResponse.getMaterial()),
                    () -> assertEquals(productResponse.getMaterial(), product.getMaterial()),
                    () -> assertNotNull(productResponse.getDescription()),
                    () -> assertEquals(productResponse.getDescription(), product.getDescription()),
                    () -> assertNotNull(productResponse.getPrice()),
                    () -> assertEquals(productResponse.getPrice(), product.getPrice()),
                    () -> assertNotNull(productResponse.getSizes()),
                    () -> assertEquals(productResponse.getSizes().size(), product.getSizes().size()),
                    () -> assertNotNull(productResponse.getColors()),
                    () -> assertEquals(productResponse.getColors().size(), product.getColors().size()),
                    () -> assertTrue(productResponse.isInStock()),
                    () -> assertEquals(productResponse.isInStock(), product.getStock().getNumberOfUnitsInStock() > 0),
                    () -> assertNotNull(productResponse.getImages())
            );
        }

        @WithMockCustomUser
        @DisplayName("Then all products are fetched from database if there are any")
        @Test
        public void testViewAllProductsSuccess() {
            // exercise
            Set<ProductResponse> products = productService.viewAllProducts();
            // verify
            assertNotNull(products);
            assertEquals(4, products.size());
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
            Set<ProductResponse> productsBefore = productService.viewAllProducts();

            // exercise
            Long productId = productService.addProduct(productRequest);

            // verify
            Product newProduct = productService.getProductById(productId);
            Set<ProductResponse> productsAfter = productService.viewAllProducts();

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
            assertEquals(1, productsAfter.size() - productsBefore.size());
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

    @DisplayName("When we want to delete product")
    @Nested
    class DeleteProductTest {

        private final Long TEST_PRODUCT_ID = 13L;

        @WithMockCustomUser(username = "admin", authorities = {"ROLE_ADMIN"})
        @DisplayName("Then product is delete when valid product id is passed")
        @Test
        public void testDeleteProductSuccess() {
            // exercise
            boolean isDeleted = productService.deleteProduct(TEST_PRODUCT_ID);

            // verify
            Product deletedProduct = productService.getProductById(TEST_PRODUCT_ID);

            assertAll("Verify deleted product",
                    () -> assertTrue(isDeleted),
                    () -> assertTrue(deletedProduct.isDeleted()),
                    () -> assertEquals(0l, deletedProduct.getStock().getNumberOfUnitsInStock())
        );
        }
    }

}
