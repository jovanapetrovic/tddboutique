package com.jovana.service.impl.product.image;

import com.jovana.config.ImageStorageProperties;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.image.Image;
import com.jovana.entity.product.image.ImageType;
import com.jovana.entity.product.image.exception.ImageNotFoundException;
import com.jovana.entity.product.image.exception.ImageStorageException;
import com.jovana.exception.ValueMustNotBeNullOrEmptyException;
import com.jovana.repositories.product.ImageRepository;
import com.jovana.service.impl.product.ProductService;
import com.jovana.service.security.IsAdmin;
import com.jovana.service.security.IsAdminOrUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by jovana on 11.04.2020
 */
@Service
@Transactional
public class ImageStorageServiceImpl implements ImageStorageService {

    private final Path imageStorageLocation;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ProductService productService;

    // Use for test purposes only
    @Deprecated
    public ImageStorageServiceImpl() {
        imageStorageLocation = null;
    }

    @Autowired
    public ImageStorageServiceImpl(ImageStorageProperties imageStorageProperties) {
        this.imageStorageLocation = Paths.get(imageStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.imageStorageLocation);
        } catch (Exception ex) {
            throw new ImageStorageException("Could not create the directory where the uploaded images will be stored.");
        }
    }

    @IsAdmin
    @Override
    public String addAndStoreImage(Long productId, MultipartFile imageFile) {
        Product product = productService.getProductById(productId); // check if product exists

        String newImageName = validateAndResolveImageName(productId, imageFile.getOriginalFilename());
        String contentType = validateExtension(imageFile.getContentType());

        try {
            // If image file already exists in storage, it will be replaced with new file
            Path targetLocation = this.imageStorageLocation.resolve(newImageName);
            Files.copy(imageFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Add new image to db only if it's not there already; no need for update
            Image newImage = getImageByProductIdAndImageName(productId, newImageName);
            if (newImage == null) {
                newImage = new Image();
                newImage.setProduct(product);
                newImage.setName(newImageName);
                newImage.setType(ImageType.getImageTypeFromType(contentType));
                newImage.setSize(imageFile.getSize());
                imageRepository.save(newImage);
            }

            return newImageName;
        } catch (IOException ex) {
            throw new ImageStorageException("Could not store image " + newImageName + ". Please try again!");
        }
    }

    @IsAdminOrUser
    @Override
    public Resource getAndLoadImageAsResource(Long productId, String imageName) {
        productService.getProductById(productId); // check if product exists
        if (getImageByProductIdAndImageName(productId, imageName) == null) {
            throw new ImageNotFoundException("Image " + imageName + " doesn't exist for productId = " + productId);
        }

        try {
            Path imagePath = this.imageStorageLocation.resolve(imageName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ImageNotFoundException("Product image not found " + imageName);
            }
        } catch (MalformedURLException ex) {
            throw new ImageNotFoundException("MalformedURLException: Product Image not found " + imageName);
        }
    }

    private Image getImageByProductIdAndImageName(Long productId, String imageName) {
        return imageRepository.findByProductIdAndName(productId, imageName);
    }

    private String validateAndResolveImageName(Long productId, String originalName) {
        String imageName = StringUtils.cleanPath(originalName);
        if (imageName.contains("..")) {
            throw new ImageStorageException("Image name contains invalid path sequence " + imageName);
        }
        return resolveImageNameWithProductId(productId, imageName);
    }

    private String resolveImageNameWithProductId(Long productId, String imageName) {
        // Add productId to image name (eg. 1_myTestImage.jpg)
        return productId + "_" + imageName;
    }

    private String validateExtension(String contentType) {
        for (ImageType imageType : ImageType.values()) {
            if (contentType.equals(imageType.getType())) {
                return contentType;
            }
        }
        throw new ImageStorageException("Only image files (png, jpg, jpeg, gif) can be uploaded!");
    }

}
