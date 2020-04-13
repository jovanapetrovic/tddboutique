package com.jovana.entity.product.image.dto;

import com.jovana.entity.PathConstants;
import com.jovana.entity.product.image.Image;

/**
 * Created by jovana on 11.04.2020
 */
public class ImageResponse {
    private String imageName;
    private String imageDownloadUri;
    private String imageType;
    private long size;

    public ImageResponse() {
    }

    public ImageResponse(String imageName, String imageDownloadUri, String imageType, long size) {
        this.imageName = imageName;
        this.imageDownloadUri = imageDownloadUri;
        this.imageType = imageType;
        this.size = size;
    }

    public static ImageResponse createFromImage(Image image) {
        // http://localhost:8080/api/download-image/1_test.png
        String downloadUri = PathConstants.API + PathConstants.DOWNLOAD_IMAGE + image.getName();
        return new ImageResponse(
                image.getName(),
                downloadUri,
                image.getType().getType(),
                image.getSize()
        );
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageDownloadUri() {
        return imageDownloadUri;
    }

    public void setImageDownloadUri(String imageDownloadUri) {
        this.imageDownloadUri = imageDownloadUri;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
