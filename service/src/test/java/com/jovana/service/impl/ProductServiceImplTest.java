package com.jovana.service.impl;

import com.jovana.entity.product.Product;
import com.jovana.entity.product.Stock;
import com.jovana.entity.product.dto.ProductFullResponse;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.entity.product.dto.ProductResponse;
import com.jovana.entity.product.dto.UpdateStockRequest;
import com.jovana.entity.product.exception.ProductNameAlreadyExistsException;
import com.jovana.entity.product.image.Image;
import com.jovana.entity.product.image.ImageType;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.product.ProductRepository;
import com.jovana.repositories.product.StockRepository;
import com.jovana.service.impl.product.ProductService;
import com.jovana.service.impl.product.ProductServiceImpl;
import com.jovana.service.util.RequestTestDataProvider;
import com.jovana.service.util.TestDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Created by jovana on 07.04.2020
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductService productService = new ProductServiceImpl();
    @Mock
    private ProductRepository productRepository;
    @Mock
    private StockRepository stockRepository;

    @DisplayName("When we want to get one or more products")
    @Nested
    class GetAndViewProductTest {

        private final Long TEST_PRODUCT_ID = 10L;
        private final Long PRODUCT_ID_NOT_EXISTS = 9999L;

        @DisplayName("Then Product is fetched from database when id is valid")
        @Test
        public void testGetProductById() {
            // prepare
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(mock(Product.class)));
            // exercise
            Product product = productService.getProductById(TEST_PRODUCT_ID);
            // verify
            assertNotNull(product, "Product is null");
        }

        @DisplayName("Then error is thrown when Product with passed id doesn't exist")
        @Test
        public void testGetProductByIdFailsWhenPassedIdDoesntExist() {
            // prepare
            when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
            // verify
            assertThrows(EntityNotFoundException.class,
                    () -> productService.getProductById(PRODUCT_ID_NOT_EXISTS), "Product with id=" + PRODUCT_ID_NOT_EXISTS + " doesn't exist");
        }

        @DisplayName("Then one Product is fetched from database when id is valid")
        @Test
        public void testViewOneProductSuccess() {
            // prepare
            ProductFullResponse productMock = mock(ProductFullResponse.class);
            when(productRepository.findOneWithImages(anyLong())).thenReturn(productMock);

            // exercise
            ProductFullResponse product = productService.viewOneProduct(TEST_PRODUCT_ID);

            // verify
            assertNotNull(product, "Product is null");
        }

        @DisplayName("Then error is thrown when Product with passed id doesn't exist")
        @Test
        public void testViewOneProductFailsWhenPassedIdDoesntExist() {
            when(productRepository.findOneWithImages(anyLong())).thenReturn(null);
            // verify
            assertThrows(EntityNotFoundException.class,
                    () -> productService.viewOneProduct(PRODUCT_ID_NOT_EXISTS), "Product with id=" + PRODUCT_ID_NOT_EXISTS + " doesn't exist");
        }

        @DisplayName("Then all products are fetched from database if there are any")
        @Test
        public void testViewAllProductsSuccess() {
            // prepare
            ProductResponse productMock1 = mock(ProductResponse.class);
            ProductResponse productMock2 = mock(ProductResponse.class);
            ProductResponse productMock3 = mock(ProductResponse.class);
            Set<ProductResponse> products = Sets.newSet(productMock1, productMock2, productMock3);

            when(productRepository.findAllWithImages()).thenReturn(products);

            // exercise
            Set<ProductResponse> productResponses = productService.viewAllProducts();

            // verify
            assertNotNull(productResponses);
            assertEquals(3, productResponses.size());
        }

        @DisplayName("Then only products where name or description contain search text are fetched")
        @Test
        public void testSearchProductsSuccess() {
            // prepare
            String TEST_SEARCH_TEXT = "summer";
            ProductResponse productMock1 = mock(ProductResponse.class);
            ProductResponse productMock2 = mock(ProductResponse.class);
            Set<ProductResponse> products = Sets.newSet(productMock1, productMock2);

            when(productRepository.findAllWhereNameOrDescriptionContainsText(anyString())).thenReturn(products);

            // exercise
            Set<ProductResponse> productResponses = productService.searchProducts(TEST_SEARCH_TEXT);

            // verify
            assertNotNull(productResponses);
            assertEquals(2, productResponses.size());
        }
    }

    @DisplayName("When we want to add a new product")
    @Nested
    class AddProductTest {

        private ProductRequest casualDressRequest;
        private Product casualDressProduct;

        @BeforeEach
        void setUp() {
            casualDressRequest = RequestTestDataProvider.getProductRequests().get("casualDress");
            casualDressProduct = TestDataProvider.getProducts().get("casualDress");
        }

        @DisplayName("Then product is created when valid ProductRequest is passed")
        @Test
        public void testAddProductSuccess() {
            // prepare
            when(productRepository.save(any(Product.class))).thenReturn(casualDressProduct);
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(casualDressProduct));

            // exercise
            Long productId = productService.addProduct(casualDressRequest);

            // verify
            Product newProduct = productService.getProductById(productId);

            assertAll("Verify new product",
                    () -> assertNotNull(newProduct),
                    () -> assertNotNull(newProduct.getName()),
                    () -> assertEquals(newProduct.getName(), casualDressRequest.getName()),
                    () -> assertNotNull(newProduct.getMaterial()),
                    () -> assertEquals(newProduct.getMaterial(), casualDressRequest.getMaterial()),
                    () -> assertNotNull(newProduct.getDescription()),
                    () -> assertEquals(newProduct.getDescription(), casualDressRequest.getDescription()),
                    () -> assertNotNull(newProduct.getPrice()),
                    () -> assertEquals(newProduct.getPrice(), casualDressRequest.getPrice()),
                    () -> assertNotNull(newProduct.getSizes()),
                    () -> assertEquals(newProduct.getSizes().size(), casualDressRequest.getSizes().size()),
                    () -> assertNotNull(newProduct.getColors()),
                    () -> assertEquals(newProduct.getColors().size(), casualDressRequest.getColors().size()),
                    () -> assertNotNull(newProduct.getStock()),
                    () -> assertEquals(newProduct.getStock().getNumberOfUnitsInStock(), casualDressRequest.getNumberOfUnitsInStock())
            );
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @DisplayName("Then error is thrown when name already exists")
        @Test
        public void testAddProductFailsWhenNameAlreadyExists() {
            // prepare
            when(productRepository.findByName(any(String.class))).thenReturn(casualDressProduct);

            // verify
            assertThrows(ProductNameAlreadyExistsException.class,
                    () -> productService.addProduct(casualDressRequest), "Name already exists");
            verify(productRepository, times(0)).save(any(Product.class));
        }

    }

    @DisplayName("When we want to update product Stock")
    @Nested
    class UpdateProductStockTest {

        @DisplayName("Then Stock is updated when request is valid")
        @Test
        public void testUpdateProductStockSuccess() {
            // prepare
            Long TEST_PRODUCT_ID = 10L;
            UpdateStockRequest updateStockRequest = RequestTestDataProvider.getStockRequests().get("updateStockRequest");
            Product eveningDressProductBefore = TestDataProvider.getProducts().get("eveningDress");
            Product eveningDressProductAfter = TestDataProvider.getProducts().get("eveningDress");
            eveningDressProductAfter.setStock(updateStockRequest.getNumberOfUnitsInStock());

            when(productRepository.findById(anyLong())).thenReturn(Optional.of(eveningDressProductBefore));
            when(productRepository.save(any(Product.class))).thenReturn(eveningDressProductAfter);

            // exercise
            Long numberOfProductsInStock = productService.updateProductStock(TEST_PRODUCT_ID, updateStockRequest);

            // verify

            assertAll("Verify updated stock",
                    () -> assertNotNull(numberOfProductsInStock),
                    () -> assertEquals(updateStockRequest.getNumberOfUnitsInStock(), numberOfProductsInStock)
            );
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @DisplayName("Then Stock is updated when product quantity is smaller than stock")
        @Test
        public void testValidateAndProcessProductStockSuccess() {
            // prepare
            Long productQuantity = 10L;
            Product casualDressProduct = TestDataProvider.getProducts().get("casualDress");
            Long casualDressStockBefore = casualDressProduct.getStock().getNumberOfUnitsInStock();
            // exercise
            boolean isStockUpdated = productService.validateAndProcessProductStock(casualDressProduct, productQuantity);

            // verify
            assertAll("Verify updated stock",
                    () -> assertTrue(isStockUpdated),
                    () -> assertEquals(productQuantity,casualDressStockBefore - casualDressProduct.getStock().getNumberOfUnitsInStock())
            );
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @DisplayName("Then Stock skips update when product quantity is bigger than stock")
        @Test
        public void testValidateAndProcessProductStockSkipsUpdate() {
            // prepare
            Long productQuantity = 100L;
            Product casualDressProduct = TestDataProvider.getProducts().get("casualDress");

            // exercise
            boolean isStockUpdated = productService.validateAndProcessProductStock(casualDressProduct, productQuantity);

            // verify
            assertFalse(isStockUpdated);
            verify(productRepository, times(0)).save(any(Product.class));
        }

    }

    @DisplayName("When we want to update Product")
    @Nested
    class UpdateProductTest {

        private final Long TEST_PRODUCT_ID = 11L;

        private ProductRequest updateProductRequest;
        private Product eveningDressProduct;
        private Product eveningDressProductUpdated;

        @BeforeEach
        void setUp() {
            updateProductRequest = RequestTestDataProvider.getProductRequests().get("updateRequest");

            eveningDressProduct = TestDataProvider.getProducts().get("eveningDress");
            eveningDressProductUpdated = TestDataProvider.getProducts().get("eveningDressUpdate");
        }

        @DisplayName("Then Product is updated when ProductRequest is valid")
        @Test
        public void testUpdateProductSuccess() {
            // prepare
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(eveningDressProduct));
            when(productRepository.save(any(Product.class))).thenReturn(eveningDressProductUpdated);

            // execute
            productService.updateProduct(TEST_PRODUCT_ID, updateProductRequest);

            // verify
            Product updatedProduct = productService.getProductById(TEST_PRODUCT_ID);

            assertAll("Verify updated product",
                    () -> assertNotNull(updatedProduct.getName()),
                    () -> assertEquals(updatedProduct.getName(), updateProductRequest.getName()),
                    () -> assertNotNull(updatedProduct.getMaterial()),
                    () -> assertEquals(updatedProduct.getMaterial(), updateProductRequest.getMaterial()),
                    () -> assertNotNull(updatedProduct.getDescription()),
                    () -> assertEquals(updatedProduct.getDescription(), updateProductRequest.getDescription()),
                    () -> assertNotNull(updatedProduct.getPrice()),
                    () -> assertEquals(updatedProduct.getPrice(), updateProductRequest.getPrice()),
                    () -> assertNotNull(updatedProduct.getSizes()),
                    () -> assertEquals(updatedProduct.getSizes().size(), updateProductRequest.getSizes().size()),
                    () -> assertNotNull(updatedProduct.getColors()),
                    () -> assertEquals(updatedProduct.getColors().size(), updateProductRequest.getColors().size()),
                    () -> assertNotNull(updatedProduct.getStock()),
                    () -> assertEquals(updatedProduct.getStock().getNumberOfUnitsInStock(), updateProductRequest.getNumberOfUnitsInStock())
            );
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @DisplayName("Then Product is updated when ProductRequest is valid")
        @Test
        public void testUpdateProductSuccessWhenNameIsUpdatedToo() {
            // prepare
            updateProductRequest.setName("Evening silk dress");
            eveningDressProductUpdated.setName("Evening silk dress");

            when(productRepository.findById(anyLong())).thenReturn(Optional.of(eveningDressProduct));
            when(productRepository.save(any(Product.class))).thenReturn(eveningDressProductUpdated);

            // execute
            productService.updateProduct(TEST_PRODUCT_ID, updateProductRequest);

            // verify
            Product updatedProduct = productService.getProductById(TEST_PRODUCT_ID);

            assertAll("Verify updated product",
                    () -> assertNotNull(updatedProduct.getName()),
                    () -> assertEquals(updatedProduct.getName(), updateProductRequest.getName())
            );
            verify(productRepository, times(1)).save(any(Product.class));
        }

    }

    @DisplayName("When we want to delete Product")
    @Nested
    class DeleteProductTest {

        private final Long TEST_PRODUCT_ID = 12L;

        @DisplayName("Then Product is deleted when valid product id is passed")
        @Test
        public void testDeleteProductSuccess() {
            // prepare
            Product deletedProduct = TestDataProvider.getProducts().get("deletedProduct");

            when(productRepository.findById(anyLong())).thenReturn(Optional.of(mock(Product.class)));
            when(productRepository.save(any(Product.class))).thenReturn(deletedProduct);

            // execute
            boolean isDeleted = productService.deleteProduct(TEST_PRODUCT_ID);

            // verify
            assertAll("Verify updated product",
                    () -> assertTrue(isDeleted),
                    () -> assertTrue(deletedProduct.isDeleted()),
                    () -> assertEquals(0L, deletedProduct.getStock().getNumberOfUnitsInStock())
            );
            verify(productRepository, times(1)).save(any(Product.class));
        }

    }

}
