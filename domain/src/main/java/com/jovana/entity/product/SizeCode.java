package com.jovana.entity.product;

/**
 * Created by jovana on 07.04.2020
 */
public enum SizeCode {

    S("Small"),
    M("Medium"),
    L("Large"),
    XL("Extra large");

    private String size;

    SizeCode(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

}
