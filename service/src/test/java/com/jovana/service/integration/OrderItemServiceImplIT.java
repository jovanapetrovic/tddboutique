package com.jovana.service.integration;

import com.google.common.collect.Lists;
import com.jovana.entity.order.OrderItem;
import com.jovana.entity.order.OrderItemState;
import com.jovana.entity.order.dto.*;
import com.jovana.entity.product.Product;
import com.jovana.repositories.order.OrderItemRepository;
import com.jovana.service.impl.order.OrderItemService;
import com.jovana.service.impl.product.ProductService;
import com.jovana.service.integration.auth.WithMockCustomUser;
import com.jovana.service.util.TestDataProvider;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by jovana on 13.04.2020
 */
@FlywayTest(locationsForMigrate = {"db.additional.orderitem"})
public class OrderItemServiceImplIT extends AbstractTest {

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductService productService;

    @DisplayName("When we want to get one or more order items")
    @Nested
    class GetAndViewOrderItemTest {

        @WithMockCustomUser
        @DisplayName("Then OrderItem is fetched from database when id is valid")
        @Test
        public void testGetOrderItemByIdSuccess() {
            // prepare
            Long TEST_CART_ITEM_ID = 10L;
            // exercise
            OrderItem orderItem = orderItemService.getOrderItemById(TEST_CART_ITEM_ID);
            // verify
            assertNotNull(orderItem, "OrderItem is null");
        }

        @WithMockCustomUser
        @DisplayName("Then all cart items are fetched from database if there are any")
        @Test
        public void testViewAllProductsSuccess() {
            // prepare
            Long TEST_USER_ID = 10L;
            BigDecimal TOTAL_PRICE = new BigDecimal("60.00");
            // exercise
            CartResponse cartResponse = orderItemService.viewCart(TEST_USER_ID);

            // verify
            assertAll("Verify cart response",
                    () -> assertNotNull(cartResponse),
                    () -> assertEquals(1, cartResponse.getCartItems().size()),
                    () -> assertEquals(TOTAL_PRICE, cartResponse.getTotalPrice())
            );

            CartItemResponse cartItem = cartResponse.getCartItems().iterator().next();
            assertAll("Verify cart item",
                    () -> assertNotNull(cartItem),
                    () -> assertNotNull(cartItem.getOrderItemId()),
                    () -> assertNotNull(cartItem.getProductId()),
                    () -> assertNotNull(cartItem.getProductName()),
                    () -> assertNotNull(cartItem.getProductSize()),
                    () -> assertNotNull(cartItem.getProductColor()),
                    () -> assertNotNull(cartItem.getImages()),
                    () -> assertNotNull(cartItem.getQuantity()),
                    () -> assertEquals(3, cartItem.getQuantity()),
                    () -> assertEquals(TOTAL_PRICE, cartItem.getTotalPricePerProduct())
            );
        }

        @WithMockCustomUser
        @DisplayName("Then an order summary is created")
        @Test
        public void testGetOrderSummarySuccess() {
            // prepare
            Long TEST_USER_ID = 10L;
            BigDecimal TOTAL_PRICE = new BigDecimal("60.00");

            // exercise
            OrderSummary orderSummary = orderItemService.getOrderSummary(TEST_USER_ID);

            // verify
            assertAll("Verify order summary",
                    () -> assertNotNull(orderSummary),
                    () -> assertEquals(1, orderSummary.getOrderItems().size()),
                    () -> assertNotNull(orderSummary.getProductNames()),
                    () -> assertEquals("Casual dress", orderSummary.getProductNames()),
                    () -> assertEquals(TOTAL_PRICE, orderSummary.getTotalPrice())
            );
        }

    }

    @WithMockCustomUser
    @DisplayName("When we want to add items to cart")
    @Nested
    class AddItemsToCartTest {

        private CartRequest cartRequest = new CartRequest();
        private CartItemDTO cartItem1;
        private CartItemDTO cartItem2;


        @BeforeEach
        void setUp() {
            cartItem1 = TestDataProvider.getCartItems().get("cartItem1");
            cartItem2 = TestDataProvider.getCartItems().get("cartItem2");
        }

        @WithMockCustomUser
        @DisplayName("Then order items are created when valid CartRequest is passed")
        @Test
        public void testAddToCartSuccess() {
            // prepare
            Long TEST_USER_ID = 11L;
            Long TEST_PRODUCT_ID = 10L;
            cartRequest.setCartItems(Lists.newArrayList(cartItem1, cartItem2));

            Set<OrderItem> orderItemsBefore = orderItemRepository.findAllUserCartItems(TEST_USER_ID);
            Product productBefore = productService.getProductById(TEST_PRODUCT_ID);
            Long productStockBefore = productBefore.getStock().getNumberOfUnitsInStock();

            // exercise
            AddToCartResponse addToCartResponse = orderItemService.addItemsToCart(TEST_USER_ID, cartRequest);

            // verify
            Set<OrderItem> orderItemsAfter = orderItemRepository.findAllUserCartItems(TEST_USER_ID);
            Product productAfter = productService.getProductById(TEST_PRODUCT_ID);
            Long productStockAfter = productAfter.getStock().getNumberOfUnitsInStock();

            assertAll("Verify cart response",
                    () -> assertNotNull(addToCartResponse),
                    () -> assertEquals(2, addToCartResponse.getOrderedProducts().size()),
                    () -> assertEquals(0, addToCartResponse.getOutOfStockProducts().size())
            );

            assertAll("Verify new order items in db",
                    () -> assertEquals(2, orderItemsAfter.size() - orderItemsBefore.size()),
                    () -> orderItemsAfter.iterator().forEachRemaining(item -> assertEquals(OrderItemState.CART, item.getOrderState()))
            );

            assertAll("Verify product stock change",
                    () -> assertEquals(2, productStockBefore - productStockAfter)
            );
        }
    }

    @DisplayName("When we want to remove an item from cart")
    @Nested
    class RemoveItemFromCartTest {

        @WithMockCustomUser
        @DisplayName("Then OrderItem is deleted from database if it is user's cart item")
        @Test
        public void testRemoveItemFromCartSuccess() {
            // prepare
            Long TEST_USER_ID = 12L;
            Long TEST_CART_ITEM_ID = 12L;

            // exercise
            boolean isRemoved = orderItemService.removeItemFromCart(TEST_USER_ID, TEST_CART_ITEM_ID);

            // verify
            Optional<OrderItem> orderItem = orderItemRepository.findById(TEST_CART_ITEM_ID);

            assertAll("Verify cart item has been removed",
                    () -> assertTrue(isRemoved),
                    () -> assertTrue(orderItem.isEmpty())
            );
        }
    }

}
