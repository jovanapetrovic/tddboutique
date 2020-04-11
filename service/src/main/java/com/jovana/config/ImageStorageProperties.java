package com.jovana.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by jovana on 11.04.2020
 */
@ConfigurationProperties(prefix = "file")
public class ImageStorageProperties {

    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

}
