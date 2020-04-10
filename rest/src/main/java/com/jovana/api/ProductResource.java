package com.jovana.api;

import com.jovana.entity.PathConstants;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.service.impl.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Created by jovana on 07.04.2020
 */
@RestController
@RequestMapping(value = PathConstants.API)
public class ProductResource {

    @Autowired
    private ProductService productService;

    @PostMapping(value = PathConstants.PRODUCT_ADD)
    public ResponseEntity<Void> addProductPOST(@Valid @RequestBody ProductRequest productRequest) {

        productService.addProduct(productRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .build()
                .toUri();

        return ResponseEntity.created(location).build();
    }

}
