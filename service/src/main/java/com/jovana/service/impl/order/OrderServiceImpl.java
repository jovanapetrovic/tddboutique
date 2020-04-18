package com.jovana.service.impl.order;

import com.google.common.collect.Sets;
import com.jovana.entity.order.OrderItem;
import com.jovana.entity.order.OrderState;
import com.jovana.entity.order.dto.CartItemDTO;
import com.jovana.entity.order.dto.CartRequest;
import com.jovana.entity.order.dto.CartResponse;
import com.jovana.entity.order.exception.InvalidCartSizeException;
import com.jovana.entity.product.ColorCode;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.SizeCode;
import com.jovana.entity.user.User;
import com.jovana.exception.EntityNotFoundException;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by jovana on 13.04.2020
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

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
    public CartResponse addItemsToCart(Long userId, CartRequest cartRequest) {
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
                orderItem.setOrderState(OrderState.CART);

                cartItems.add(orderItem);
                orderedItems.add(cartItem);
            } else {
                outOfStockItems.add(cartItem);
            }
        }

        List<OrderItem> newCartItems = orderItemRepository.saveAll(cartItems);
        LOGGER.info("Saved {} cart items.", newCartItems.size());
        LOGGER.info("{} items are out of stock.", outOfStockItems.size());
        return new CartResponse(orderedItems, outOfStockItems);
    }

    private void validateCartRequest(CartRequest cartRequest) {
        int cartSize = cartRequest.getCartItems().size();
        if (cartSize <= 0 || cartSize > 10) {
            throw new InvalidCartSizeException(cartSize, "One cart can contain between 1 and 10 items");
        }
    }

}
