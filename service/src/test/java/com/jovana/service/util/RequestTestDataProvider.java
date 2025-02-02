package com.jovana.service.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jovana.entity.coupon.CouponValue;
import com.jovana.entity.coupon.dto.CouponRequest;
import com.jovana.entity.order.dto.CheckoutCartRequest;
import com.jovana.entity.product.ColorCode;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.SizeCode;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.entity.product.dto.UpdateStockRequest;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.entity.user.dto.RegisterUserRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

        ProductRequest eveningDressUpdate = new ProductRequest();
        eveningDressUpdate.setName("Evening dress");
        eveningDressUpdate.setMaterial("silk");
        eveningDressUpdate.setDescription("An elegant silk dress");
        eveningDressUpdate.setPrice(new BigDecimal("50.00"));
        eveningDressUpdate.setSizes(Lists.newArrayList("S", "M", "L", "XL"));
        eveningDressUpdate.setColors(Lists.newArrayList("BLACK", "RED"));
        eveningDressUpdate.setNumberOfUnitsInStock(100L);

        productRequests.put("casualDress", casualDressRequest);
        productRequests.put("updateRequest", eveningDressUpdate);

        return productRequests;
    }

    public static Map<String, UpdateStockRequest> getStockRequests() {
        Map<String, UpdateStockRequest> stockRequests = new HashMap<>();

        UpdateStockRequest updateStockRequest = new UpdateStockRequest();
        updateStockRequest.setNumberOfUnitsInStock(50L);

        stockRequests.put("updateStockRequest", updateStockRequest);

        return stockRequests;
    }

    public static Map<String, CouponRequest> getCouponRequests() {
        Map<String, CouponRequest> couponRequests = new HashMap<>();

        CouponRequest johnCoupon = new CouponRequest();
        johnCoupon.setUserId(10L);
        johnCoupon.setValue(CouponValue.COUPON_10.name());
        johnCoupon.setExpiryDate(LocalDateTime.now().plusDays(5));

        couponRequests.put("john", johnCoupon);

        return couponRequests;
    }

    public static Map<String, CheckoutCartRequest> getCheckoutCartRequests() {
        Map<String, CheckoutCartRequest> checkoutCartRequests = new HashMap<>();

        CheckoutCartRequest cardRequest = new CheckoutCartRequest();
        cardRequest.setNote("Some note for seller");
        cardRequest.setCouponCode("ASDF5678asdf");
        cardRequest.setPaymentMethod("CARD");
        cardRequest.setShippingAddressId(10L);

        CheckoutCartRequest deliveryRequest = new CheckoutCartRequest();
        deliveryRequest.setNote("Call my phone before delivery");
        deliveryRequest.setCouponCode("7gHZfVo5gELe");
        deliveryRequest.setPaymentMethod("DELIVERY");
        deliveryRequest.setShippingAddressId(10L);

        CheckoutCartRequest noCouponDeliveryRequest = new CheckoutCartRequest();
        noCouponDeliveryRequest.setNote(null);
        noCouponDeliveryRequest.setCouponCode(null);
        noCouponDeliveryRequest.setPaymentMethod("DELIVERY");
        noCouponDeliveryRequest.setShippingAddressId(10L);

        CheckoutCartRequest emptyCouponDeliveryRequest = new CheckoutCartRequest();
        emptyCouponDeliveryRequest.setNote(null);
        emptyCouponDeliveryRequest.setCouponCode("");
        emptyCouponDeliveryRequest.setPaymentMethod("DELIVERY");
        emptyCouponDeliveryRequest.setShippingAddressId(10L);

        checkoutCartRequests.put("card", cardRequest);
        checkoutCartRequests.put("delivery", deliveryRequest);
        checkoutCartRequests.put("deliveryNoCoupon", noCouponDeliveryRequest);
        checkoutCartRequests.put("emptyCouponDeliveryRequest", emptyCouponDeliveryRequest);

        return checkoutCartRequests;
    }

}
