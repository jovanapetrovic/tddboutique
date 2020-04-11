package com.jovana.service.impl.product.image;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by jovana on 11.04.2020
 */
public interface ImageStorageService {

    String addAndStoreImage(Long productId, MultipartFile imageFile);

    Resource getAndLoadImageAsResource(Long productId, String imageName);

}
