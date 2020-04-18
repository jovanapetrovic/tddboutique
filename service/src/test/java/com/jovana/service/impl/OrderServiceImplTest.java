package com.jovana.service.impl;

import com.google.common.collect.Lists;
import com.jovana.entity.order.OrderItem;
import com.jovana.entity.order.dto.CartItemDTO;
import com.jovana.entity.order.dto.CartRequest;
import com.jovana.entity.order.dto.CartResponse;
import com.jovana.entity.order.exception.InvalidCartSizeException;
import com.jovana.entity.product.Product;
import com.jovana.entity.user.User;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.order.OrderItemRepository;
import com.jovana.service.impl.order.OrderService;
import com.jovana.service.impl.order.OrderServiceImpl;
import com.jovana.service.impl.product.ProductService;
import com.jovana.service.impl.user.UserService;
import com.jovana.service.util.TestDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Created by jovana on 13.04.2020
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;

    @DisplayName("When we want to find an order item by id")
    @Nested
    class GetOrderItemTest {

        @DisplayName("Then OrderItem is fetched from database when id is valid")
        @Test
        public void testGetOrderItemByIdSuccess() {
            // prepare
            Long TEST_ORDER_ITEM_ID = 10L;
            when(orderItemRepository.findById(any(Long.class))).thenReturn(Optional.of(mock(OrderItem.class)));
            // exercise
            OrderItem orderItem = orderService.getOrderItemById(TEST_ORDER_ITEM_ID);
            // verify
            assertNotNull(orderItem, "OrderItem is null");
        }

        @DisplayName("Then error is thrown when OrderItem with passed id doesn't exist")
        @Test
        public void testGetOrderItemByIdFailsWhenOrderItemWithPassedIdDoesntExist() {
            // prepare
            Long TEST_ORDER_ITEM_ID = 9999L;
            when(orderItemRepository.findById(any(Long.class))).thenReturn(Optional.empty());
            // verify
            assertThrows(EntityNotFoundException.class,
                    () -> orderService.getOrderItemById(TEST_ORDER_ITEM_ID), "OrderItem with id=" + TEST_ORDER_ITEM_ID + " doesn't exist");
        }
    }

    @DisplayName("When we want to add items to cart")
    @Nested
    class AddItemsToCartTest {

        private User userMock;
        private Product productMock;

        private CartRequest cartRequest = new CartRequest();
        private CartItemDTO cartItem1;
        private CartItemDTO cartItem2;

        private OrderItem orderItem1;
        private OrderItem orderItem2;

        @BeforeEach
        void setUp() {
            userMock = mock(User.class);
            productMock = mock(Product.class);

            cartItem1 = TestDataProvider.getCartItems().get("cartItem1");
            cartItem2 = TestDataProvider.getCartItems().get("cartItem2");

            orderItem1 = TestDataProvider.getOrderItems().get("orderItem1");
            orderItem2 = TestDataProvider.getOrderItems().get("orderItem2");
        }

        @DisplayName("Then order items are created when valid CartRequest is passed")
        @Test
        public void testAddItemsToCartSuccess() {
            // prepare
            Long TEST_USER_ID = 10L;
            cartRequest.setCartItems(Lists.newArrayList(cartItem1, cartItem2));
            List<OrderItem> orderItems = Lists.newArrayList(orderItem1, orderItem2);

            when(userService.getUserById(anyLong())).thenReturn(userMock);
            when(productService.getProductById(anyLong())).thenReturn(productMock);
            when(productService.validateAndProcessProductStock(any(Product.class), anyLong())).thenReturn(true);

            when(orderItemRepository.saveAll(anyIterable())).thenReturn(orderItems);

            // exercise
            CartResponse cartResponse = orderService.addItemsToCart(TEST_USER_ID, cartRequest);

            // verify
            assertAll("Verify cart response",
                    () -> assertNotNull(cartResponse),
                    () -> assertEquals(2, cartResponse.getOrderedProducts().size()),
                    () -> assertEquals(0, cartResponse.getOutOfStockProducts().size())
            );
            verify(orderItemRepository, times(1)).saveAll(anySet());
        }

        @DisplayName("Then creating order items is skipped when product is out of stock")
        @Test
        public void testAddItemsToCartSkipsWhenProductIsOutOfStock() {
            // prepare
            Long TEST_USER_ID = 10L;
            cartRequest.setCartItems(Lists.newArrayList(cartItem1));
            List<OrderItem> orderItems = Lists.newArrayList(orderItem1);

            when(userService.getUserById(anyLong())).thenReturn(userMock);
            when(productService.getProductById(anyLong())).thenReturn(productMock);
            when(productService.validateAndProcessProductStock(any(Product.class), anyLong())).thenReturn(false);

            when(orderItemRepository.saveAll(anyIterable())).thenReturn(orderItems);

            // exercise
            CartResponse cartResponse = orderService.addItemsToCart(TEST_USER_ID, cartRequest);

            // verify

            assertAll("Verify cart response",
                    () -> assertNotNull(cartResponse),
                    () -> assertEquals(0, cartResponse.getOrderedProducts().size()),
                    () -> assertEquals(1, cartResponse.getOutOfStockProducts().size())
            );
            verify(orderItemRepository, times(1)).saveAll(anySet());
        }

        @DisplayName("Then add items to cart fails when cart is empty")
        @Test
        public void testAddItemsToCartFailsWhenCartIsEmpty() {
            // prepare
            Long TEST_USER_ID = 10L;
            cartRequest.setCartItems(Lists.newArrayList());

            when(userService.getUserById(anyLong())).thenReturn(userMock);

            // verify
            assertThrows(InvalidCartSizeException.class,
                    () -> orderService.addItemsToCart(TEST_USER_ID, cartRequest), "Cart is empty.");

            verify(orderItemRepository, times(0)).saveAll(anySet());
        }

        @DisplayName("Then add items to cart fails when cart has more than 10 items")
        @Test
        public void testAddItemsToCartFailsWhenCartHasMoreThan10Items() {
            // prepare
            Long TEST_USER_ID = 10L;
            CartItemDTO cartItemMock = mock(CartItemDTO.class);
            cartRequest.setCartItems(Lists.newArrayList(
                    cartItemMock, cartItemMock, cartItemMock, cartItemMock, cartItemMock,
                    cartItemMock, cartItemMock, cartItemMock, cartItemMock, cartItemMock, cartItemMock));

            when(userService.getUserById(anyLong())).thenReturn(userMock);

            // verify
            assertThrows(InvalidCartSizeException.class,
                    () -> orderService.addItemsToCart(TEST_USER_ID, cartRequest), "Cart has more than 10 items.");

            verify(orderItemRepository, times(0)).saveAll(anySet());
        }

    }

}
