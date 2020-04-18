package com.jovana.api;

import com.jovana.entity.order.OrderItem;
import com.jovana.service.impl.coupon.CouponService;
import com.jovana.service.impl.order.OrderService;
import com.jovana.service.impl.product.ProductService;
import com.jovana.service.impl.product.image.ImageStorageService;
import com.jovana.service.impl.shippingaddress.ShippingAddressService;
import com.jovana.service.impl.user.UserService;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Created by jovana on 11.04.2020
 */
public class ServiceBeanMocks {

    @MockBean
    private UserService userService;
    @MockBean
    private ShippingAddressService service;
    @MockBean
    private ProductService productService;
    @MockBean
    private ImageStorageService imageStorageService;
    @MockBean
    private CouponService couponService;
    @MockBean
    private OrderService orderService;

}
