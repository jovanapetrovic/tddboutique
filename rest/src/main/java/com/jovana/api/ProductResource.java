package com.jovana.api;

import com.jovana.entity.PathConstants;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.entity.product.dto.UpdateStockRequest;
import com.jovana.entity.product.image.dto.ImageResponse;
import com.jovana.entity.product.image.exception.ImageStorageException;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.service.impl.product.ProductService;
import com.jovana.service.impl.product.image.ImageStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jovana on 07.04.2020
 */
@RestController
@RequestMapping(value = PathConstants.API)
public class ProductResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private ImageStorageService imageStorageService;

    @PostMapping(value = PathConstants.PRODUCT_ADD)
    public ResponseEntity<Void> addProductPOST(@Valid @RequestBody ProductRequest productRequest) {

        productService.addProduct(productRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .build()
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = PathConstants.PRODUCT_STOCK_UPDATE)
    public ResponseEntity<Void> updateProductStockPUT(@PathVariable("productId") Long productId,
                                                      @Valid @RequestBody UpdateStockRequest updateStockRequest) {

        productService.updateProductStock(productId, updateStockRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping(PathConstants.UPLOAD_IMAGE)
    public ImageResponse uploadImage(@PathVariable("productId") Long productId,
                                     @RequestParam("image") MultipartFile image) {
        if (image != null) {
            String imageName = imageStorageService.addAndStoreImage(productId, image);

            String imageDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(PathConstants.API + PathConstants.DOWNLOAD_IMAGE)
                    .path(imageName)
                    .toUriString();

            return new ImageResponse(imageName, imageDownloadUri, image.getContentType(), image.getSize());
        } else {
            throw new ImageStorageException("Image file cannot be null or empty!");
        }
    }

    @PostMapping(PathConstants.UPLOAD_MULTIPLE_IMAGES)
    public List<ImageResponse> uploadMultipleImages(@PathVariable("productId") Long productId,
                                                    @RequestParam("images") MultipartFile[] images) {
        return Arrays.asList(images)
                .stream()
                .map(image -> uploadImage(productId, image))
                .collect(Collectors.toList());
    }

    @GetMapping(PathConstants.DOWNLOAD_IMAGE_BY_NAME)
    public ResponseEntity<Resource> downloadImage(@PathVariable("productId") Long productId,
                                                  @PathVariable String imageName,
                                                  HttpServletRequest request) {
        Resource resource = imageStorageService.getAndLoadImageAsResource(productId, imageName);

        // Try to determine content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            LOGGER.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
