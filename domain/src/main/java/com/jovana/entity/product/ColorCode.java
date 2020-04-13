package com.jovana.entity.product;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 * Created by jovana on 07.04.2020
 */
public enum ColorCode {

    BLACK("Black"),
    WHITE("White"),
    GRAY("Gray"),
    RED("Red"),
    PINK("Pink"),
    PURPLE("Purple"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow"),
    BEIGE("Beige"),
    ORANGE("Orange"),
    BROWN("Brown"),
    COLORFUL("Colorful"),
    OTHER("Other");

    private String code;

    ColorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Set<ColorCode> getColorCodesFromColorStrings(List<String> colorStrings) {
        Set<ColorCode> colors = Sets.newHashSet();
        for (String s : colorStrings) {
            ColorCode colorCode = ColorCode.valueOf(s);
            colors.add(colorCode);
        }
        return colors;
    }

}
