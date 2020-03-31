package com.jovana.service.util;

import com.google.common.collect.Sets;
import com.jovana.entity.authority.Authority;
import com.jovana.entity.authority.AuthorityConstants;
import com.jovana.entity.shippingaddress.ShippingAddress;
import com.jovana.entity.user.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jovana on 31.03.2020
 */
public class TestDataProvider {

    private static final String DEFAULT_ENCODED_PASSWORD = "$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366";

    public static Map<String, User> getUsers() {
        Map<String, User> users = new HashMap<>();

        User userJohn = new User();
        userJohn.setId(10L);
        userJohn.setFirstName("John");
        userJohn.setLastName("Doe");
        userJohn.setEmail("johndoe@test.com");
        userJohn.setUsername("johndoe");
        userJohn.setPassword(DEFAULT_ENCODED_PASSWORD);
        userJohn.setAuthorities(Sets.newHashSet(new Authority(AuthorityConstants.USER)));

        User userJane = new User();
        userJane.setId(10L);
        userJane.setFirstName("Jane");
        userJane.setLastName("Doe");
        userJane.setEmail("janedoe@test.com");
        userJane.setUsername("janedoe");
        userJane.setPassword(DEFAULT_ENCODED_PASSWORD);
        userJane.setAuthorities(Sets.newHashSet(new Authority(AuthorityConstants.USER)));

        users.put("john", userJohn);
        users.put("jane", userJane);

        return users;
    }

    public static Map<String, ShippingAddress> getShippingAddresses() {
        Map<String, ShippingAddress> shippingAddresses = new HashMap<>();

        ShippingAddress johnShipAddress = new ShippingAddress();
        johnShipAddress.setId(10L);
        johnShipAddress.setUser(TestDataProvider.getUsers().get("john"));
        johnShipAddress.setFirstName("John");
        johnShipAddress.setLastName("Doe");
        johnShipAddress.setAddress("Pobedina 1");
        johnShipAddress.setZipCode(18000L);
        johnShipAddress.setCity("Nis");
        johnShipAddress.setCountry("Serbia");
        johnShipAddress.setPhone("+38164123456");

        ShippingAddress janeShipAddress = new ShippingAddress();
        janeShipAddress.setId(11L);
        janeShipAddress.setUser(TestDataProvider.getUsers().get("jane"));
        janeShipAddress.setFirstName("Jane");
        janeShipAddress.setLastName("Doe");
        janeShipAddress.setAddress("Pobedina 1");
        janeShipAddress.setZipCode(18000L);
        janeShipAddress.setCity("Nis");
        janeShipAddress.setCountry("Serbia");
        janeShipAddress.setPhone("+38164123456");

        shippingAddresses.put("john", johnShipAddress);
        shippingAddresses.put("jane", janeShipAddress);

        return shippingAddresses;
    }

}
