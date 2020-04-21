package com.jovana.service.impl.order;

import com.google.common.collect.Sets;
import com.jovana.entity.order.Order;
import com.jovana.entity.order.OrderItem;
import com.jovana.entity.order.OrderItemState;
import com.jovana.entity.order.dto.*;
import com.jovana.entity.order.exception.InvalidCartSizeException;
import com.jovana.entity.product.ColorCode;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.SizeCode;
import com.jovana.entity.user.User;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.exception.ActionNotAllowedException;
import com.jovana.repositories.order.OrderItemRepository;
import com.jovana.service.impl.product.ProductService;
import com.jovana.service.impl.user.UserService;
import com.jovana.service.security.IsAdminOrUser;
import com.jovana.service.security.IsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by jovana on 13.04.2020
 */
@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @IsAdminOrUser
    @Override
    public OrderItem getOrderItemById(Long orderItemId) {
        Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemId);
        if (orderItem.isEmpty()) {
            LOGGER.info("OrderItem with id = {} was not found in the db.", orderItemId);
            throw new EntityNotFoundException("No order item found with id = " + orderItemId);
        }
        return orderItem.get();
    }

    @IsUser
    @Override
    public AddToCartResponse addItemsToCart(Long userId, CartRequest cartRequest) {
        User user = userService.getUserById(userId);
        validateCartRequest(cartRequest);

        Set<CartItemDTO> outOfStockItems = Sets.newHashSet();
        Set<CartItemDTO> orderedItems = Sets.newHashSet();

        Set<OrderItem> cartItems = Sets.newHashSet();
        for (CartItemDTO cartItem : cartRequest.getCartItems()) {
            Product product = productService.getProductById(cartItem.getProductId());
            boolean isCreatingOrderItemAllowed =
                    productService.validateAndProcessProductStock(product, cartItem.getQuantity());

            if (isCreatingOrderItemAllowed) {
                OrderItem orderItem = new OrderItem();
                orderItem.setUser(user);
                orderItem.setProduct(product);
                orderItem.setProductSize(SizeCode.valueOf(cartItem.getProductSize()));
                orderItem.setProductColor(ColorCode.valueOf(cartItem.getProductColor()));
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setOrderState(OrderItemState.CART);

                cartItems.add(orderItem);
                orderedItems.add(cartItem);
            } else {
                outOfStockItems.add(cartItem);
            }
        }

        List<OrderItem> newCartItems = orderItemRepository.saveAll(cartItems);
        LOGGER.info("Saved {} cart items.", newCartItems.size());
        LOGGER.info("{} items are out of stock.", outOfStockItems.size());
        return new AddToCartResponse(orderedItems, outOfStockItems);
    }

    @IsUser
    @Override
    public boolean removeItemFromCart(Long userId, Long orderItemId) {
        OrderItem orderItem = getOrderItemById(orderItemId);
        if (userId == orderItem.getUser().getId() && OrderItemState.CART.equals(orderItem.getOrderState())) {
            orderItemRepository.delete(orderItem);
            return true;
        }
        throw new ActionNotAllowedException("This item is not in your current cart.");
    }

    @IsUser
    @Override
    public CartResponse viewCart(Long userId) {
        Set<CartItemResponse> cartItems = orderItemRepository.findAllCartItemsWithProductDataByUserId(userId);
        LOGGER.info("Found {} items in cart.", cartItems.size());

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItemResponse item : cartItems) {
            totalPrice = totalPrice.add(item.getTotalPricePerProduct());
        }
        LOGGER.info("Cart total price = {} EUR.", totalPrice);

        return new CartResponse(cartItems, totalPrice);
    }

    @IsUser
    @Override
    public OrderSummary getOrderSummary(Long userId) {
        Set<OrderItem> orderItems = orderItemRepository.findAllCartItemsByUserId(userId);
        LOGGER.info("Found {} items in cart ready for order.", orderItems.size());

        if (orderItems.isEmpty()) {
            throw new InvalidCartSizeException(orderItems.size(), "Cannot checkout an empty cart.");
        }

        String productNames = "";
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            totalPrice = totalPrice.add(item.getTotalPricePerProduct());
            productNames = productNames.concat(item.getProduct().getName() + ", ");
        }
        if (productNames.endsWith(", ")) {
            productNames = productNames.substring(0, productNames.length() - 2);
        }
        LOGGER.info("Order total price = {} EUR.", totalPrice);

        return new OrderSummary(orderItems, productNames, totalPrice);
    }

    @IsUser
    @Override
    public List<OrderItem> updateCartItemsToOrderItems(Set<OrderItem> orderItems, Order order) {
        for (OrderItem item : orderItems) {
            item.setOrderState(OrderItemState.ORDER);
            item.setOrder(order);
        }
        return orderItemRepository.saveAll(orderItems);
    }

    private void validateCartRequest(CartRequest cartRequest) {
        int cartSize = cartRequest.getCartItems().size();
        if (cartSize <= 0 || cartSize > 10) {
            throw new InvalidCartSizeException(cartSize, "One cart can contain between 1 and 10 items.");
        }
    }

}
