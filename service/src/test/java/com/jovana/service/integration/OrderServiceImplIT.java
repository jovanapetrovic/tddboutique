package com.jovana.service.integration;

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

}
