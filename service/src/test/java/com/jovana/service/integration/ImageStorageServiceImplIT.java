package com.jovana.service.integration;

import com.jovana.entity.product.image.Image;
import com.jovana.repositories.product.ImageRepository;
import com.jovana.service.impl.product.image.ImageStorageService;
import com.jovana.service.integration.auth.WithMockCustomUser;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by jovana on 12.04.2020
 */
@FlywayTest(locationsForMigrate = {"db.additional.image"})
public class ImageStorageServiceImplIT extends AbstractTest {

    @Autowired
    private ImageStorageService imageStorageService;
    @Autowired
    private ImageRepository imageRepository;

    @DisplayName("When we want to add a new image")
    @Nested
    class AddProductImageTest {

        private final Long TEST_PRODUCT_ID = 10L;
        private final String IMAGE_NAME = "flowerDress.png";
        private final String IMAGE_TYPE = "image/png";
        private final String IMAGE_PATH = "src\\test\\resources\\images\\test.png";

        @WithMockCustomUser(username = "admin", authorities = {"ROLE_ADMIN"})
        @DisplayName("Then image is created when valid a image is passed")
        @Test
        public void testAddProductImageSuccess() throws Exception {
            // prepare
            MultipartFile imageFile = new MockMultipartFile(
                    IMAGE_NAME,
                    IMAGE_NAME,
                    IMAGE_TYPE,
                    new FileInputStream(new File(IMAGE_PATH).getAbsolutePath())
            );

            // exercise
            String imageName = imageStorageService.addAndStoreImage(TEST_PRODUCT_ID, imageFile);

            // verify
            Resource resource = imageStorageService.getAndLoadImageAsResource(TEST_PRODUCT_ID, imageName);
            Image image = imageRepository.findByProductIdAndName(TEST_PRODUCT_ID, imageName);

            assertAll("Verify resource & new image",
                    () -> assertNotNull(resource),
                    () -> assertNotNull(image),
                    () -> assertNotNull(image.getId()),
                    () -> assertNotNull(image.getProduct()),
                    () -> assertEquals(TEST_PRODUCT_ID, image.getProduct().getId()),
                    () -> assertEquals(TEST_PRODUCT_ID + "_" + IMAGE_NAME, image.getName()),
                    () -> assertEquals(IMAGE_TYPE, image.getType().getType()),
                    () -> assertNotNull(image.getSize()),
                    () -> assertEquals(378611, image.getSize())
            );
        }
    }

}
