package com.jovana.service.integration;

import com.jovana.entity.order.OrderItem;
import com.jovana.service.impl.order.OrderService;
import com.jovana.service.integration.auth.WithMockCustomUser;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by jovana on 13.04.2020
 */
@FlywayTest(locationsForMigrate = {"db.additional.order"})
public class OrderServiceImplIT extends AbstractTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("When we want to find an order item by id")
    @Nested
    class GetOrderItemTest {

        @WithMockCustomUser
        @DisplayName("Then OrderItem is fetched from database when id is valid")
        @Test
        public void testGetOrderItemByIdSuccess() {
            // prepare
            Long TEST_CART_ITEM_ID = 10L;
            // exercise
            OrderItem orderItem = orderService.getOrderItemById(TEST_CART_ITEM_ID);
            // verify
            assertNotNull(orderItem, "OrderItem is null");
        }
    }

}
