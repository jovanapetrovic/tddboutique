package com.jovana.service.impl;

import com.google.common.collect.Lists;
import com.jovana.entity.order.OrderItem;
import com.jovana.entity.order.OrderState;
import com.jovana.entity.order.dto.*;
import com.jovana.entity.order.exception.InvalidCartSizeException;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.dto.ProductFullResponse;
import com.jovana.entity.product.dto.ProductResponse;
import com.jovana.entity.user.User;
import com.jovana.exception.ActionNotAllowedException;
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
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    class GetAndViewOrderItemTest {

        @Spy
        private CartItemResponse cartItem1;
        @Spy
        private CartItemResponse cartItem2;
        @Spy
        private CartItemResponse cartItem3;

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

        @DisplayName("Then all cart items are fetched from database if there are any")
        @Test
        public void testViewCartSuccess() {
            // prepare
            Long TEST_USER_ID = 10L;
            BigDecimal CART_ITEM_1_PRICE = BigDecimal.valueOf(19.99);
            BigDecimal CART_ITEM_2_PRICE = BigDecimal.valueOf(40.00);
            BigDecimal CART_ITEM_3_PRICE = BigDecimal.valueOf(11.99);

            cartItem1.setTotalPricePerProduct(CART_ITEM_1_PRICE);
            cartItem2.setTotalPricePerProduct(CART_ITEM_2_PRICE);
            cartItem3.setTotalPricePerProduct(CART_ITEM_3_PRICE);

            Set<CartItemResponse> cartItems = Sets.newSet(cartItem1, cartItem2, cartItem3);
            when(orderItemRepository.findAllCartItemsWithProductDataByUserId(TEST_USER_ID)).thenReturn(cartItems);

            // exercise
            CartResponse cartResponse = orderService.viewCart(TEST_USER_ID);

            // verify
            assertNotNull(cartResponse);
            assertEquals(3, cartResponse.getCartItems().size());
            assertEquals(CART_ITEM_1_PRICE.add(CART_ITEM_2_PRICE).add(CART_ITEM_3_PRICE), cartResponse.getTotalPrice());
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
            AddToCartResponse addToCartResponse = orderService.addItemsToCart(TEST_USER_ID, cartRequest);

            // verify
            assertAll("Verify cart response",
                    () -> assertNotNull(addToCartResponse),
                    () -> assertEquals(2, addToCartResponse.getOrderedProducts().size()),
                    () -> assertEquals(0, addToCartResponse.getOutOfStockProducts().size())
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
            AddToCartResponse addToCartResponse = orderService.addItemsToCart(TEST_USER_ID, cartRequest);

            // verify

            assertAll("Verify cart response",
                    () -> assertNotNull(addToCartResponse),
                    () -> assertEquals(0, addToCartResponse.getOrderedProducts().size()),
                    () -> assertEquals(1, addToCartResponse.getOutOfStockProducts().size())
            );
            verify(orderItemRepository, times(1)).saveAll(anySet());
        }

        @DisplayName("Then error is thrown when cart is empty")
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

        @DisplayName("Then error is thrown when cart has more than 10 items")
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

    @DisplayName("When we want to remove an item from cart")
    @Nested
    class RemoveItemFromCartTest {

        @DisplayName("Then OrderItem is deleted from database if it is user's cart item")
        @Test
        public void testRemoveItemFromCartSuccess() {
            // prepare
            Long TEST_USER_ID = 10L;
            Long TEST_CART_ITEM_ID = 10L;
            OrderItem orderItemMock = mock(OrderItem.class);
            User userMock = mock(User.class);

            when(orderItemRepository.findById(any(Long.class))).thenReturn(Optional.of(orderItemMock));
            when(orderItemMock.getUser()).thenReturn(userMock);
            when(userMock.getId()).thenReturn(TEST_USER_ID);
            when(orderItemMock.getOrderState()).thenReturn(OrderState.CART);

            // exercise
            boolean isRemoved = orderService.removeItemFromCart(TEST_USER_ID, TEST_CART_ITEM_ID);
            // verify
            assertTrue(isRemoved);
        }

        @DisplayName("Then error is thrown when OrderItem is not user's item")
        @Test
        public void testRemoveItemFromCartFailsWhenOrderItemDoesntBelongToUser() {
            // prepare
            Long TEST_USER_ID = 10L;
            Long TEST_ACTUAL_USER_ID = 11L;
            Long TEST_ORDER_ITEM_ID = 9999L;
            OrderItem orderItemMock = mock(OrderItem.class);
            User userMock = mock(User.class);

            when(orderItemRepository.findById(any(Long.class))).thenReturn(Optional.of(orderItemMock));
            when(orderItemMock.getUser()).thenReturn(userMock);
            when(userMock.getId()).thenReturn(TEST_ACTUAL_USER_ID);

            // verify
            assertThrows(ActionNotAllowedException.class,
                    () -> orderService.removeItemFromCart(TEST_USER_ID, TEST_ORDER_ITEM_ID), "This item is not in your cart");
        }

        @DisplayName("Then error is thrown when OrderItem is not a cart item")
        @Test
        public void testRemoveItemFromCartFailsWhenOrderItemIsNotInUsersCart() {
            // prepare
            Long TEST_USER_ID = 10L;
            Long TEST_ORDER_ITEM_ID = 9999L;
            OrderItem orderItemMock = mock(OrderItem.class);
            User userMock = mock(User.class);

            when(orderItemRepository.findById(any(Long.class))).thenReturn(Optional.of(orderItemMock));
            when(orderItemMock.getUser()).thenReturn(userMock);
            when(userMock.getId()).thenReturn(TEST_USER_ID);
            when(orderItemMock.getOrderState()).thenReturn(OrderState.ORDER);

            // verify
            assertThrows(ActionNotAllowedException.class,
                    () -> orderService.removeItemFromCart(TEST_USER_ID, TEST_ORDER_ITEM_ID), "This item is not in your cart");
        }
    }

}
