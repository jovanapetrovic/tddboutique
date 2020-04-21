package com.jovana.service.impl;

import com.google.common.collect.Lists;
import com.jovana.entity.order.Order;
import com.jovana.entity.order.OrderItem;
import com.jovana.entity.order.OrderItemState;
import com.jovana.entity.order.dto.*;
import com.jovana.entity.order.exception.InvalidCartSizeException;
import com.jovana.entity.product.Product;
import com.jovana.entity.user.User;
import com.jovana.exception.ActionNotAllowedException;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.order.OrderItemRepository;
import com.jovana.service.impl.order.OrderItemService;
import com.jovana.service.impl.order.OrderItemServiceImpl;
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
public class OrderItemServiceImplTest {

    @InjectMocks
    private OrderItemService orderItemService = new OrderItemServiceImpl();
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;

    @DisplayName("When we want to get one or more order items")
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
            when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(mock(OrderItem.class)));
            // exercise
            OrderItem orderItem = orderItemService.getOrderItemById(TEST_ORDER_ITEM_ID);
            // verify
            assertNotNull(orderItem, "OrderItem is null");
        }

        @DisplayName("Then error is thrown when OrderItem with passed id doesn't exist")
        @Test
        public void testGetOrderItemByIdFailsWhenOrderItemWithPassedIdDoesntExist() {
            // prepare
            Long TEST_ORDER_ITEM_ID = 9999L;
            when(orderItemRepository.findById(anyLong())).thenReturn(Optional.empty());
            // verify
            assertThrows(EntityNotFoundException.class,
                    () -> orderItemService.getOrderItemById(TEST_ORDER_ITEM_ID), "OrderItem with id=" + TEST_ORDER_ITEM_ID + " doesn't exist");
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
            CartResponse cartResponse = orderItemService.viewCart(TEST_USER_ID);

            // verify
            assertNotNull(cartResponse);
            assertEquals(3, cartResponse.getCartItems().size());
            assertEquals(CART_ITEM_1_PRICE.add(CART_ITEM_2_PRICE).add(CART_ITEM_3_PRICE), cartResponse.getTotalPrice());
        }

        @DisplayName("Then empty cart is shown if there aren't any cart items")
        @Test
        public void testViewCartSuccessWhenCartIsEmpty() {
            // prepare
            Long TEST_USER_ID = 10L;
            when(orderItemRepository.findAllCartItemsWithProductDataByUserId(TEST_USER_ID)).thenReturn(Sets.newSet());

            // exercise
            CartResponse cartResponse = orderItemService.viewCart(TEST_USER_ID);

            // verify
            assertNotNull(cartResponse);
            assertEquals(0, cartResponse.getCartItems().size());
            assertEquals(BigDecimal.ZERO, cartResponse.getTotalPrice());
        }

        @DisplayName("Then an order summary is created")
        @Test
        public void testGetOrderSummarySuccess() {
            // prepare
            Long TEST_USER_ID = 10L;
            BigDecimal TOTAL_PRICE = new BigDecimal("60.00");

            OrderItem orderItem1 = TestDataProvider.getOrderItems().get("orderItem1");
            OrderItem orderItem2 = TestDataProvider.getOrderItems().get("orderItem2");

            Set<OrderItem> orderItems = Sets.newSet(orderItem1, orderItem2);
            when(orderItemRepository.findAllCartItemsByUserId(TEST_USER_ID)).thenReturn(orderItems);

            // exercise
            OrderSummary orderSummary = orderItemService.getOrderSummary(TEST_USER_ID);

            // verify
            assertAll("Verify order summary",
                    () -> assertNotNull(orderSummary),
                    () -> assertEquals(2, orderSummary.getOrderItems().size()),
                    () -> assertNotNull(orderSummary.getProductNames()),
                    () -> assertEquals("Casual dress, Casual dress", orderSummary.getProductNames()),
                    () -> assertEquals(TOTAL_PRICE, orderSummary.getTotalPrice())
            );
        }

        @DisplayName("Then an error is thrown when no order items are found in cart")
        @Test
        public void testGetOrderSummaryFailsWhenThereAreNoCartItems() {
            // prepare
            Long TEST_USER_ID = 10L;
            Set<OrderItem> orderItems = Sets.newSet();
            when(orderItemRepository.findAllCartItemsByUserId(TEST_USER_ID)).thenReturn(orderItems);

            // verify
            assertThrows(InvalidCartSizeException.class, () -> orderItemService.getOrderSummary(TEST_USER_ID));
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
            AddToCartResponse addToCartResponse = orderItemService.addItemsToCart(TEST_USER_ID, cartRequest);

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
            AddToCartResponse addToCartResponse = orderItemService.addItemsToCart(TEST_USER_ID, cartRequest);

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
                    () -> orderItemService.addItemsToCart(TEST_USER_ID, cartRequest), "Cart is empty.");

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
                    () -> orderItemService.addItemsToCart(TEST_USER_ID, cartRequest), "Cart has more than 10 items.");

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

            when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItemMock));
            when(orderItemMock.getUser()).thenReturn(userMock);
            when(userMock.getId()).thenReturn(TEST_USER_ID);
            when(orderItemMock.getOrderState()).thenReturn(OrderItemState.CART);

            // exercise
            boolean isRemoved = orderItemService.removeItemFromCart(TEST_USER_ID, TEST_CART_ITEM_ID);
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

            when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItemMock));
            when(orderItemMock.getUser()).thenReturn(userMock);
            when(userMock.getId()).thenReturn(TEST_ACTUAL_USER_ID);

            // verify
            assertThrows(ActionNotAllowedException.class,
                    () -> orderItemService.removeItemFromCart(TEST_USER_ID, TEST_ORDER_ITEM_ID), "This item is not in your cart");
        }

        @DisplayName("Then error is thrown when OrderItem is not a cart item")
        @Test
        public void testRemoveItemFromCartFailsWhenOrderItemIsNotInUsersCart() {
            // prepare
            Long TEST_USER_ID = 10L;
            Long TEST_ORDER_ITEM_ID = 9999L;
            OrderItem orderItemMock = mock(OrderItem.class);
            User userMock = mock(User.class);

            when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItemMock));
            when(orderItemMock.getUser()).thenReturn(userMock);
            when(userMock.getId()).thenReturn(TEST_USER_ID);
            when(orderItemMock.getOrderState()).thenReturn(OrderItemState.ORDER);

            // verify
            assertThrows(ActionNotAllowedException.class,
                    () -> orderItemService.removeItemFromCart(TEST_USER_ID, TEST_ORDER_ITEM_ID), "This item is not in your cart");
        }
    }
    @DisplayName("When we want to remove an item from cart")
    @Nested
    class UpdateCartItemsToOrderItemsTest {

        @DisplayName("Then OrderItem is deleted from database if it is user's cart item")
        @Test
        public void testRemoveItemFromCartSuccess() {
            // prepare
            Order orderMock = mock(Order.class);
            OrderItem orderItemMock1 = mock(OrderItem.class);
            OrderItem orderItemMock2 = mock(OrderItem.class);
            Set<OrderItem> orderItemsToUpdate = Sets.newSet(orderItemMock1, orderItemMock2);

            OrderItem orderItemMock11 = mock(OrderItem.class);
            OrderItem orderItemMock21 = mock(OrderItem.class);
            List<OrderItem> orderItemsUpdated = Lists.newArrayList(orderItemMock11, orderItemMock21);

            when(orderItemMock11.getOrderState()).thenReturn(OrderItemState.ORDER);
            when(orderItemMock11.getOrder()).thenReturn(orderMock);
            when(orderItemMock21.getOrderState()).thenReturn(OrderItemState.ORDER);
            when(orderItemMock21.getOrder()).thenReturn(orderMock);

            when(orderItemRepository.saveAll(anySet())).thenReturn(orderItemsUpdated);

            // exercise
            List<OrderItem> orderItems = orderItemService.updateCartItemsToOrderItems(orderItemsToUpdate, orderMock);

            // verify
            assertAll("Verify updated order items",
                    () -> assertNotNull(orderItems),
                    () -> assertEquals(2, orderItems.size()),
                    () -> assertEquals(OrderItemState.ORDER, orderItems.get(0).getOrderState()),
                    () -> assertEquals(orderMock, orderItems.get(0).getOrder()),
                    () -> assertEquals(OrderItemState.ORDER, orderItems.get(1).getOrderState()),
                    () -> assertEquals(orderMock, orderItems.get(1).getOrder())
            );
        }
    }

}
