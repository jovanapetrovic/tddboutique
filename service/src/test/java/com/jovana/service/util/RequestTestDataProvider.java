package com.jovana.service.util;

import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jovana on 31.03.2020
 */
public class RequestTestDataProvider {

    public static Map<String, ShippingAddressRequest> getShippingAddressRequests() {
        Map<String, ShippingAddressRequest> shippingAddressRequests = new HashMap<>();

        ShippingAddressRequest validRequest = new ShippingAddressRequest();
        validRequest.setUseFirstAndLastNameFromUser(false);
        validRequest.setFirstName("John");
        validRequest.setLastName("Doe");
        validRequest.setAddress("Pobedina 1");
        validRequest.setZipCode(18000L);
        validRequest.setCity("Nis");
        validRequest.setCountry("Serbia");
        validRequest.setPhoneNumber("+38164123456");

        ShippingAddressRequest requestWithoutFirstAndLastName = new ShippingAddressRequest();
        requestWithoutFirstAndLastName.setUseFirstAndLastNameFromUser(true);
        requestWithoutFirstAndLastName.setFirstName(null);
        requestWithoutFirstAndLastName.setLastName(null);
        requestWithoutFirstAndLastName.setAddress("Pobedina 1");
        requestWithoutFirstAndLastName.setZipCode(18000L);
        requestWithoutFirstAndLastName.setCity("Nis");
        requestWithoutFirstAndLastName.setCountry("Serbia");
        requestWithoutFirstAndLastName.setPhoneNumber("+38164123456");

        shippingAddressRequests.put("johnRequest", validRequest);
        shippingAddressRequests.put("noNamesRequest", requestWithoutFirstAndLastName);

        return shippingAddressRequests;
    }

}
