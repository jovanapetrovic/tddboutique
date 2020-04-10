package com.jovana.entity.product;

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
    ORANGE("Orange"),
    BROWN("Brown"),
    COLORFUL("Colorful");

    private String colorCode;

    ColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }

}
