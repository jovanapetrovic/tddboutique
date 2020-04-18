package com.jovana.service.util;

import com.google.common.collect.Sets;
import com.jovana.entity.authority.Authority;
import com.jovana.entity.authority.AuthorityConstants;
import com.jovana.entity.coupon.Coupon;
import com.jovana.entity.coupon.CouponStatus;
import com.jovana.entity.coupon.CouponValue;
import com.jovana.entity.product.ColorCode;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.SizeCode;
import com.jovana.entity.product.Stock;
import com.jovana.entity.product.image.Image;
import com.jovana.entity.product.image.ImageType;
import com.jovana.entity.shippingaddress.ShippingAddress;
import com.jovana.entity.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        userJane.setId(11L);
        userJane.setFirstName("Jane");
        userJane.setLastName("Doe");
        userJane.setEmail("janedoe@test.com");
        userJane.setUsername("janedoe");
        userJane.setPassword(DEFAULT_ENCODED_PASSWORD);
        userJane.setAuthorities(Sets.newHashSet(new Authority(AuthorityConstants.USER)));

        User userApril = new User();
        userApril.setId(12L);
        userApril.setFirstName("April");
        userApril.setLastName("O Neal");
        userApril.setEmail("apriloneal@test.com");
        userApril.setUsername("apriloneal");
        userApril.setPassword(DEFAULT_ENCODED_PASSWORD);
        userApril.setAuthorities(Sets.newHashSet(new Authority(AuthorityConstants.USER)));

        users.put("john", userJohn);
        users.put("jane", userJane);
        users.put("april", userApril);

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

        ShippingAddress johnUpdatedShipAddress = new ShippingAddress();
        johnUpdatedShipAddress.setId(12L);
        johnUpdatedShipAddress.setUser(TestDataProvider.getUsers().get("john"));
        johnUpdatedShipAddress.setFirstName("John");
        johnUpdatedShipAddress.setLastName("Doe");
        johnUpdatedShipAddress.setAddress("Knez Mihajlova 1");
        johnUpdatedShipAddress.setZipCode(11000L);
        johnUpdatedShipAddress.setCity("Belgrade");
        johnUpdatedShipAddress.setCountry("Serbia");
        johnUpdatedShipAddress.setPhone("+38164123456");

        shippingAddresses.put("john", johnShipAddress);
        shippingAddresses.put("jane", janeShipAddress);
        shippingAddresses.put("johnUpdate", johnUpdatedShipAddress);

        return shippingAddresses;
    }

    public static Map<String, Product> getProducts() {
        Map<String, Product> products = new HashMap<>();

        Product casualDressProduct = new Product();
        casualDressProduct.setId(10L);
        casualDressProduct.setName("Casual dress");
        casualDressProduct.setMaterial("viscose, cotton, polyester");
        casualDressProduct.setDescription("A beautiful everyday dress");
        casualDressProduct.setPrice(new BigDecimal("30.00"));
        casualDressProduct.setSizes(Sets.newHashSet(SizeCode.S, SizeCode.M, SizeCode.L, SizeCode.XL));
        casualDressProduct.setColors(Sets.newHashSet(ColorCode.BLACK, ColorCode.RED));
        casualDressProduct.setStock(70L);

        Product eveningDressProduct = new Product();
        eveningDressProduct.setId(11L);
        eveningDressProduct.setName("Evening dress");
        eveningDressProduct.setMaterial("cotton, polyester");
        eveningDressProduct.setDescription("A beautiful evening dress");
        eveningDressProduct.setPrice(new BigDecimal("45.00"));
        eveningDressProduct.setSizes(Sets.newHashSet(SizeCode.S, SizeCode.M, SizeCode.L, SizeCode.XL));
        eveningDressProduct.setColors(Sets.newHashSet(ColorCode.BLACK, ColorCode.COLORFUL));
        eveningDressProduct.setStock(1L);

        Product eveningDressUpdate = new Product();
        eveningDressUpdate.setId(11L);
        eveningDressUpdate.setName("Evening dress");
        eveningDressUpdate.setMaterial("silk");
        eveningDressUpdate.setDescription("An elegant silk dress");
        eveningDressUpdate.setPrice(new BigDecimal("50.00"));
        eveningDressUpdate.setSizes(Sets.newHashSet(SizeCode.S, SizeCode.M, SizeCode.L, SizeCode.XL));
        eveningDressUpdate.setColors(Sets.newHashSet(ColorCode.BLACK, ColorCode.COLORFUL));
        eveningDressUpdate.setStock(100L);

        Product deletedProduct = new Product();
        deletedProduct.setId(12L);
        deletedProduct.setName("Green dress");
        deletedProduct.setMaterial("cotton");
        deletedProduct.setDescription("Simple green dress");
        deletedProduct.setPrice(new BigDecimal("10.00"));
        deletedProduct.setDeleted(true);
        deletedProduct.setSizes(Sets.newHashSet(SizeCode.S, SizeCode.M));
        deletedProduct.setColors(Sets.newHashSet(ColorCode.GREEN));
        deletedProduct.setStock(0L);

        products.put("casualDress", casualDressProduct);
        products.put("eveningDress", eveningDressProduct);
        products.put("eveningDressUpdate", eveningDressUpdate);
        products.put("deletedProduct", deletedProduct);

        return products;
    }

    public static Map<String, Image> getImages() {
        Map<String, Image> images = new HashMap<>();

        Image casualDressImage = new Image();
        casualDressImage.setId(10L);
        casualDressImage.setProduct(TestDataProvider.getProducts().get("casualDress"));
        casualDressImage.setName("10_test.png");
        casualDressImage.setType(ImageType.PNG);
        casualDressImage.setSize(12345L);

        images.put("casualDressImage", casualDressImage);

        return images;
    }

    public static Map<String, Coupon> getCoupons() {
        Map<String, Coupon> coupons = new HashMap<>();

        Coupon johnCoupon = new Coupon();
        johnCoupon.setId(10L);
        johnCoupon.setUser(TestDataProvider.getUsers().get("john"));
        johnCoupon.setCode("ASDF5678asdf");
        johnCoupon.setValue(CouponValue.COUPON_10);
        johnCoupon.setStatus(CouponStatus.ACTIVE);
        johnCoupon.setExpiryDate(LocalDateTime.now().plusDays(5));

        Coupon redeemedCoupon = new Coupon();
        redeemedCoupon.setId(11L);
        redeemedCoupon.setUser(TestDataProvider.getUsers().get("john"));
        redeemedCoupon.setCode("ASDF5678asdf");
        redeemedCoupon.setValue(CouponValue.COUPON_10);
        redeemedCoupon.setStatus(CouponStatus.REDEEMED);
        redeemedCoupon.setExpiryDate(LocalDateTime.now().plusDays(5));

        Coupon expiredCoupon = new Coupon();
        expiredCoupon.setId(12L);
        expiredCoupon.setUser(TestDataProvider.getUsers().get("john"));
        expiredCoupon.setCode("1234aBaB56CD");
        expiredCoupon.setValue(CouponValue.COUPON_20);
        expiredCoupon.setStatus(CouponStatus.ACTIVE);
        expiredCoupon.setExpiryDate(LocalDateTime.now().minusDays(5));

        coupons.put("john", johnCoupon);
        coupons.put("johnRedeemed", redeemedCoupon);
        coupons.put("johnExpired", expiredCoupon);

        return coupons;
    }

}
