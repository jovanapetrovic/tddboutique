package com.jovana.api;

import com.jovana.service.impl.product.ProductService;
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

}
