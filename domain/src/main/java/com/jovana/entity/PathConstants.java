package com.jovana.entity;

public final class PathConstants {

    // API path placeholders (PH)
    public static final String PH_USER_ID = "{userId}";
    public static final String PH_SHIPPING_ADDRESS_ID = "{shippingAddressId}";
    public static final String PH_PRODUCT_ID = "{productId}";
    public static final String PH_IMAGE_NAME = "{imageName:.+}";
    public static final String PH_ORDER_ITEM_ID = "{orderItemId}";

    // Syntax
    public static final String SEPARATOR = "/";

    // API paths
    public static final String API = "/api";

    // user
    public static final String REGISTER = "/register";
    public static final String USER = "/user";
    public static final String USER_CHANGE_EMAIL_ADDRESS = USER + "/change-email-address/" + PH_USER_ID;
    public static final String USER_CHANGE_USERNAME = USER + "/change-username/" + PH_USER_ID;
    public static final String USER_CHANGE_PASSWORD = USER + "/change-password/" + PH_USER_ID;

    // shipping address
    public static final String SHIPPING_ADDRESS = "/shipping-address";
    public static final String SHIPPING_ADDRESS_ADD = SHIPPING_ADDRESS + SEPARATOR + PH_USER_ID;
    public static final String SHIPPING_ADDRESS_UPDATE = SHIPPING_ADDRESS + "/update/" + PH_SHIPPING_ADDRESS_ID;
    public static final String SHIPPING_ADDRESS_VIEW_ALL = "/shipping-addresses/" + PH_USER_ID;

    // product
    public static final String PRODUCT = "/product";
    public static final String PRODUCT_ADD = PRODUCT;
    public static final String PRODUCT_UPDATE = PRODUCT + "/update/" + PH_PRODUCT_ID;
    public static final String PRODUCT_DELETE = PRODUCT + "/delete/" + PH_PRODUCT_ID;
    public static final String PRODUCT_STOCK_UPDATE = PRODUCT + "/update-stock/" + PH_PRODUCT_ID;
    public static final String PRODUCT_VIEW_ALL = "/products";
    public static final String PRODUCT_VIEW_ONE = PRODUCT + SEPARATOR + PH_PRODUCT_ID;

    // product images
    public static final String UPLOAD_IMAGE = "/upload-image/" + PH_PRODUCT_ID;
    public static final String UPLOAD_MULTIPLE_IMAGES = "/upload-multiple-images/" + PH_PRODUCT_ID;
    public static final String DOWNLOAD_IMAGE = "/download-image/";
    public static final String DOWNLOAD_IMAGE_BY_NAME = DOWNLOAD_IMAGE + PH_PRODUCT_ID + SEPARATOR + PH_IMAGE_NAME;

    // coupon
    public static final String COUPON = "/coupon";
    public static final String COUPON_ADD = COUPON;
    public static final String COUPON_VIEW_ALL = "/coupons/" + PH_USER_ID;

    // cart
    public static final String CART = "/cart";
    public static final String CART_ADD = "/add-to-cart/" + PH_USER_ID;
    public static final String CART_REMOVE_ITEM = CART + "/remove-item/" + PH_USER_ID + SEPARATOR + PH_ORDER_ITEM_ID;
    public static final String CART_VIEW_ALL = CART + "/view/" + PH_USER_ID;
    public static final String CART_CHECKOUT = CART + "/checkout/" + PH_USER_ID;

    private PathConstants() {
    }

}
