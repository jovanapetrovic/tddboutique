package com.jovana.entity;

public final class PathConstants {

    // API path placeholders (PH)
    public static final String PH_USER_ID = "{userId}";
    public static final String PH_SHIPPING_ADDRESS_ID = "{shippingAddressId}";

    // Syntax
    public static final String SEPARATOR = "/";

    // API paths
    public static final String API = "/api";

    public static final String REGISTER = "/register";
    public static final String USER = "/user";
    public static final String USER_CHANGE_EMAIL_ADDRESS = USER + "/change-email-address/" + PH_USER_ID;
    public static final String USER_CHANGE_USERNAME = USER + "/change-username/" + PH_USER_ID;
    public static final String USER_CHANGE_PASSWORD = USER + "/change-password/" + PH_USER_ID;

    public static final String SHIPPING_ADDRESS = "/shipping-address";
    public static final String SHIPPING_ADDRESS_ADD = SHIPPING_ADDRESS + SEPARATOR + PH_USER_ID;
    public static final String SHIPPING_ADDRESS_UPDATE = SHIPPING_ADDRESS + "/update/" + PH_SHIPPING_ADDRESS_ID;

    private PathConstants() {
    }

}
