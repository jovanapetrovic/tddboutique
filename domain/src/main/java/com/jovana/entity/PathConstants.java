package com.jovana.entity;

public final class PathConstants {

    // API path placeholders (PH)
    public static final String PH_USER_ID = "{userId}";

    // Syntax
    public static final String SEPARATOR = "/";

    // API paths
    public static final String API = "/api";

    public static final String REGISTER = "/register";

    public static final String SHIPPING_ADDRESS = "/shipping-address";
    public static final String SHIPPING_ADDRESS_ADD = SHIPPING_ADDRESS + SEPARATOR + PH_USER_ID;

    private PathConstants() {
    }

}
