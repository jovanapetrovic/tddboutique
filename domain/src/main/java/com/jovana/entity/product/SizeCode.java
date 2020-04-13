package com.jovana.entity.product;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 * Created by jovana on 07.04.2020
 */
public enum SizeCode {

    S("Small"),
    M("Medium"),
    L("Large"),
    XL("Extra large");

    private String code;

    SizeCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Set<SizeCode> getSizeCodesFromSizeStrings(List<String> sizeStrings) {
        Set<SizeCode> sizes = Sets.newHashSet();
        for (String s : sizeStrings) {
            SizeCode sizeCode = SizeCode.valueOf(s);
            sizes.add(sizeCode);
        }
        return sizes;
    }

}
