package com.jovana.entity.product.image;

/**
 * Created by jovana on 11.04.2020
 */
public enum ImageType {

    PNG("image/png"),
    JPG("image/jpg"),
    JPEG("image/jpeg"),
    GIF("image/gif");

    private String type;

    ImageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ImageType getImageTypeFromType(String type) {
        for (ImageType imageType : ImageType.values()) {
            if (type.equals(imageType.getType())) {
                return imageType;
            }
        }
        return null;
    }

}
