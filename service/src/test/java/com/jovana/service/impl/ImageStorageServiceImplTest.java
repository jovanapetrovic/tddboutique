package com.jovana.service.impl;

import com.jovana.entity.product.Product;
import com.jovana.entity.product.image.Image;
import com.jovana.entity.product.image.exception.ImageNotFoundException;
import com.jovana.entity.product.image.exception.ImageStorageException;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.product.ImageRepository;
import com.jovana.service.impl.product.ProductService;
import com.jovana.service.impl.product.image.ImageStorageService;
import com.jovana.service.impl.product.image.ImageStorageServiceImpl;
import com.jovana.service.util.TestDataProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Created by jovana on 11.04.2020
 */
@ExtendWith(MockitoExtension.class)
public class ImageStorageServiceImplTest {

    private static final String IMAGE_UPLOAD_DIR = "C:\\Users\\User\\AppData\\Local\\Temp\\tddboutique\\images";

    @InjectMocks
    private ImageStorageService imageStorageService = new ImageStorageServiceImpl();
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ProductService productService;

    @DisplayName("When we want to add a new product image")
    @Nested
    class AddProductImageTest {

        private final Long TEST_PRODUCT_ID = 10L;
        private final Long PRODUCT_ID_NOT_EXISTS = 9999L;
        private final Long IMAGE_ID = 10L;
        private final String IMAGE_NAME = "test.png";
        private final String IMAGE_NAME_WRONG = "te..st.png";
        private final String IMAGE_CONTENT_TYPE = "image/png";
        private final String IMAGE_CONTENT_TYPE_WRONG = "text/csv";
        private final Long IMAGE_SIZE = 12345L;

        private Product productMock;
        private MultipartFile imageFileMock;
        private InputStream inputStream;

        private Image image;

        @BeforeEach
        void setUp() {
            initPrivateVariablesForImageStorageService();

            productMock = mock(Product.class);
            imageFileMock = mock(MultipartFile.class);
            inputStream = mock(InputStream.class);

            image = TestDataProvider.getImages().get("casualDressImage");
        }

        @DisplayName("Then image is created when valid image file is passed")
        @Test
        public void testAddImageSuccess() throws Exception {
            // prepare
            when(productService.getProductById(TEST_PRODUCT_ID)).thenReturn(productMock);

            when(imageFileMock.getOriginalFilename()).thenReturn(IMAGE_NAME);
            when(imageFileMock.getContentType()).thenReturn(IMAGE_CONTENT_TYPE);
            when(imageFileMock.getInputStream()).thenReturn(inputStream); // mock image persisting

            when(imageRepository.findByProductIdAndName(anyLong(), anyString())).thenReturn(null);
            when(imageRepository.save(any(Image.class))).thenReturn(image);

            when(imageFileMock.getSize()).thenReturn(IMAGE_SIZE);

            when(imageRepository.findById(anyLong())).thenReturn(Optional.of(image));

            // exercise
            imageStorageService.addAndStoreImage(TEST_PRODUCT_ID, imageFileMock);

            // verify
            Image newImage = imageRepository.findById(IMAGE_ID).get();

            assertAll("Verify new image",
                    () -> assertNotNull(newImage),
                    () -> assertNotNull(newImage.getId()),
                    () -> assertNotNull(newImage.getProduct()),
                    () -> assertEquals(TEST_PRODUCT_ID, image.getProduct().getId()),
                    () -> assertEquals(TEST_PRODUCT_ID + "_" + IMAGE_NAME, image.getName()),
                    () -> assertEquals(IMAGE_CONTENT_TYPE, image.getType().getType()),
                    () -> assertNotNull(image.getSize())
            );
            verify(imageRepository, times(1)).save(any(Image.class));
        }

        @DisplayName("Then image is created when valid image file is passed")
        @Test
        public void testAddImageSuccessAndImageEntityAlreadyExists() throws Exception {
            // prepare
            when(productService.getProductById(TEST_PRODUCT_ID)).thenReturn(productMock);

            when(imageFileMock.getOriginalFilename()).thenReturn(IMAGE_NAME);
            when(imageFileMock.getContentType()).thenReturn(IMAGE_CONTENT_TYPE);
            when(imageFileMock.getInputStream()).thenReturn(inputStream); // mock image persisting

            when(imageRepository.findByProductIdAndName(anyLong(), anyString())).thenReturn(image);

            when(imageRepository.findById(anyLong())).thenReturn(Optional.of(image));

            // exercise
            imageStorageService.addAndStoreImage(TEST_PRODUCT_ID, imageFileMock);

            // verify
            Image newImage = imageRepository.findById(IMAGE_ID).get();

            assertAll("Verify new image",
                    () -> assertNotNull(newImage),
                    () -> assertNotNull(newImage.getId()),
                    () -> assertNotNull(newImage.getProduct()),
                    () -> assertEquals(TEST_PRODUCT_ID, image.getProduct().getId()),
                    () -> assertEquals(TEST_PRODUCT_ID + "_" + IMAGE_NAME, image.getName()),
                    () -> assertEquals(IMAGE_CONTENT_TYPE, image.getType().getType()),
                    () -> assertNotNull(image.getSize())
            );
            verify(imageRepository, times(0)).save(any(Image.class));
        }

        @DisplayName("Then error is thrown when product doesn't exist")
        @Test
        public void testAddImageFailsWhenProductDoesntExist() throws Exception {
            // prepare
            when(productService.getProductById(PRODUCT_ID_NOT_EXISTS)).thenThrow(EntityNotFoundException.class);

            // verify
            assertThrows(EntityNotFoundException.class,
                    () -> imageStorageService.addAndStoreImage(PRODUCT_ID_NOT_EXISTS, imageFileMock), "Product doesn't exist");
            verify(imageRepository, times(0)).save(any(Image.class));
        }

        @DisplayName("Then error is thrown when image name contains invalid path sequence")
        @Test
        public void testAddImageFailsWhenImageNameIsInvalid() throws Exception {
            // prepare
            when(productService.getProductById(TEST_PRODUCT_ID)).thenReturn(productMock);
            when(imageFileMock.getOriginalFilename()).thenReturn(IMAGE_NAME_WRONG);

            // verify
            assertThrows(ImageStorageException.class,
                    () -> imageStorageService.addAndStoreImage(TEST_PRODUCT_ID, imageFileMock), "Image name is invalid");
            verify(imageRepository, times(0)).save(any(Image.class));
        }

        @DisplayName("Then error is thrown when non-image file is passed")
        @Test
        public void testAddImageFailsWhenNonImageFileIsPassed() throws Exception {
            // prepare
            when(productService.getProductById(TEST_PRODUCT_ID)).thenReturn(productMock);
            when(imageFileMock.getOriginalFilename()).thenReturn(IMAGE_NAME);
            when(imageFileMock.getContentType()).thenReturn(IMAGE_CONTENT_TYPE_WRONG);

            // verify
            assertThrows(ImageStorageException.class,
                    () -> imageStorageService.addAndStoreImage(TEST_PRODUCT_ID, imageFileMock), "Only image files allowed");
            verify(imageRepository, times(0)).save(any(Image.class));
        }

        @DisplayName("Then error is thrown when storing image fails first")
        @Test
        public void testAddImageFailsWhenStoreImageFails() throws Exception {
            // prepare
            when(productService.getProductById(TEST_PRODUCT_ID)).thenReturn(productMock);

            when(imageFileMock.getOriginalFilename()).thenReturn(IMAGE_NAME);
            when(imageFileMock.getContentType()).thenReturn(IMAGE_CONTENT_TYPE);
            when(imageFileMock.getInputStream()).thenThrow(IOException.class); // fail image persisting

            // verify
            assertThrows(ImageStorageException.class,
                    () -> imageStorageService.addAndStoreImage(TEST_PRODUCT_ID, imageFileMock), "Failed to store image");
            verify(imageRepository, times(0)).save(any(Image.class));
        }

    }

    @DisplayName("When we want to get a product image")
    @Nested
    class GetProductImageTest {

        private final Long TEST_PRODUCT_ID = 11L;
        private final Long PRODUCT_ID_NOT_EXISTS = 9999L;
        private final String IMAGE_NAME = "11_test.png";
        private final String IMAGE_NAME_NOT_EXISTS = "9999_test.png";

        private File fileExists;

        private Product productMock;
        private Image imageMock;

        @BeforeEach
        void setUp() {
            initPrivateVariablesForImageStorageService();

            // TODO: find a better solution
            try {
                String someBytes ="asdf";
                fileExists = new File(IMAGE_UPLOAD_DIR + "\\" + IMAGE_NAME);
                FileOutputStream fos = new FileOutputStream(fileExists);
                fos.write(someBytes.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            productMock = mock(Product.class);
            imageMock = mock(Image.class);
        }

        @DisplayName("Then get image is successful when valid request is passed")
        @Test
        public void testGetImageSuccess() {
            // prepare
            when(productService.getProductById(TEST_PRODUCT_ID)).thenReturn(productMock);
            when(imageRepository.findByProductIdAndName(anyLong(), anyString())).thenReturn(imageMock);

            // exercise
            Resource resource = imageStorageService.getAndLoadImageAsResource(TEST_PRODUCT_ID, IMAGE_NAME);

            // verify
            assertNotNull(resource);
        }

        @DisplayName("Then error is thrown when product doesn't exist")
        @Test
        public void testGetImageFailsWhenProductDoesntExist() throws Exception {
            // prepare
            when(productService.getProductById(PRODUCT_ID_NOT_EXISTS)).thenThrow(EntityNotFoundException.class);

            // verify
            assertThrows(EntityNotFoundException.class,
                    () -> imageStorageService.getAndLoadImageAsResource(PRODUCT_ID_NOT_EXISTS, IMAGE_NAME), "Product doesn't exist");
        }

        @DisplayName("Then error is thrown when image doesn't exist in db for passed product id")
        @Test
        public void testGetImageFailsWhenImageDoesntExistInDb() throws Exception {
            // prepare
            when(productService.getProductById(TEST_PRODUCT_ID)).thenReturn(productMock);
            when(imageRepository.findByProductIdAndName(anyLong(), anyString())).thenReturn(null);

            // verify
            assertThrows(ImageNotFoundException.class,
                    () -> imageStorageService.getAndLoadImageAsResource(TEST_PRODUCT_ID, IMAGE_NAME), "Image doesn't exist in db");
        }

        @DisplayName("Then error is thrown when image file doesn't exist")
        @Test
        public void testGetImageFailsWhenImageFileDoesntExist() {
            // prepare
            when(productService.getProductById(TEST_PRODUCT_ID)).thenReturn(productMock);
            when(imageRepository.findByProductIdAndName(anyLong(), anyString())).thenReturn(imageMock);

            // verify
            assertThrows(ImageNotFoundException.class,
                    () -> imageStorageService.getAndLoadImageAsResource(TEST_PRODUCT_ID, IMAGE_NAME_NOT_EXISTS), "Image file doesn't exist");
        }

    }

    private void initPrivateVariablesForImageStorageService() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(imageStorageService, "getImageUploadDir", IMAGE_UPLOAD_DIR, String.class);
    }

}
