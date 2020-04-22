package com.jovana.service.integration;

import com.jovana.repositories.order.OrderRepository;
import com.jovana.service.impl.coupon.CouponService;
import com.jovana.service.impl.order.OrderItemService;
import com.jovana.service.impl.order.OrderService;
import org.flywaydb.test.annotation.FlywayTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jovana on 21.04.2020
 */
@FlywayTest(locationsForMigrate = {"db.additional.order"})
public class OrderServiceImplIT extends AbstractTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CouponService couponService;
    @Autowired
    private OrderItemService orderItemService;

}
