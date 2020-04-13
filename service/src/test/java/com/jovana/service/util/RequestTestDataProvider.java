package com.jovana.service.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.entity.product.dto.UpdateStockRequest;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.entity.user.dto.RegisterUserRequest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jovana on 31.03.2020
 */
public class RequestTestDataProvider {

    public static Map<String, RegisterUserRequest> getRegisterUserRequests() {
        Map<String, RegisterUserRequest> registerUserRequests = new HashMap<>();

        RegisterUserRequest registerJohnRequest = new RegisterUserRequest();
        registerJohnRequest.setFirstName("John");
        registerJohnRequest.setLastName("Doe");
        registerJohnRequest.setEmail("johndoe@test.com");
        registerJohnRequest.setUsername("johndoe");
        registerJohnRequest.setPassword("123456");
        registerJohnRequest.setConfirmPassword("123456");

        registerUserRequests.put("john", registerJohnRequest);

        return registerUserRequests;
    }


    public static Map<String, ShippingAddressRequest> getShippingAddressRequests() {
        Map<String, ShippingAddressRequest> shippingAddressRequests = new HashMap<>();

        ShippingAddressRequest johnRequest = new ShippingAddressRequest();
        johnRequest.setUseFirstAndLastNameFromUser(false);
        johnRequest.setFirstName("John");
        johnRequest.setLastName("Doe");
        johnRequest.setAddress("Pobedina 1");
        johnRequest.setZipCode(18000L);
        johnRequest.setCity("Nis");
        johnRequest.setCountry("Serbia");
        johnRequest.setPhoneNumber("+38164123456");

        ShippingAddressRequest noNamesRequest = new ShippingAddressRequest();
        noNamesRequest.setUseFirstAndLastNameFromUser(true);
        noNamesRequest.setFirstName(null);
        noNamesRequest.setLastName(null);
        noNamesRequest.setAddress("Pobedina 1");
        noNamesRequest.setZipCode(18000L);
        noNamesRequest.setCity("Nis");
        noNamesRequest.setCountry("Serbia");
        noNamesRequest.setPhoneNumber("+38164123456");

        ShippingAddressRequest johnUpdate = new ShippingAddressRequest();
        johnUpdate.setUseFirstAndLastNameFromUser(false);
        johnUpdate.setFirstName("John");
        johnUpdate.setLastName("Doe");
        johnUpdate.setAddress("Knez Mihajlova 1");
        johnUpdate.setZipCode(11000L);
        johnUpdate.setCity("Belgrade");
        johnUpdate.setCountry("Serbia");
        johnUpdate.setPhoneNumber("+38164123456");

        shippingAddressRequests.put("john", johnRequest);
        shippingAddressRequests.put("noNames", noNamesRequest);
        shippingAddressRequests.put("updateRequest", johnUpdate);

        return shippingAddressRequests;
    }

    public static Map<String, ProductRequest> getProductRequests() {
        Map<String, ProductRequest> productRequests = new HashMap<>();

        ProductRequest casualDressRequest = new ProductRequest();
        casualDressRequest.setName("Casual dress");
        casualDressRequest.setMaterial("viscose, cotton, polyester");
        casualDressRequest.setDescription("A beautiful everyday dress");
        casualDressRequest.setPrice(new BigDecimal("30.00"));
        casualDressRequest.setSizes(Lists.newArrayList("S", "M", "L", "XL"));
        casualDressRequest.setColors(Lists.newArrayList("BLACK", "RED"));
        casualDressRequest.setNumberOfUnitsInStock(70L);

        productRequests.put("casualDress", casualDressRequest);

        return productRequests;
    }

    public static Map<String, UpdateStockRequest> getStockRequests() {
        Map<String, UpdateStockRequest> stockRequests = new HashMap<>();

        UpdateStockRequest updateStockRequest = new UpdateStockRequest();
        updateStockRequest.setNumberOfUnitsInStock(50L);

        stockRequests.put("updateStockRequest", updateStockRequest);

        return stockRequests;
    }

}
