package com.jovana.api;

import com.jovana.entity.PathConstants;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.service.impl.shippingaddress.ShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Created by jovana on 01.04.2020
 */
@RestController
@RequestMapping(value = PathConstants.API)
public class ShippingAddressResource {

    @Autowired
    private ShippingAddressService shippingAddressService;

    @PostMapping(value = PathConstants.SHIPPING_ADDRESS_ADD)
    public ResponseEntity<Void> addShippingAddressPOST(@PathVariable("userId") Long userId,
                                                       @Valid @RequestBody ShippingAddressRequest shippingAddressRequest) {

        shippingAddressService.addUserShippingAddress(userId, shippingAddressRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .build()
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = PathConstants.SHIPPING_ADDRESS_UPDATE)
    public ResponseEntity<Void> updateShippingAddressPOST(@PathVariable("shippingAddressId") Long shippingAddressId,
                                                          @Valid @RequestBody ShippingAddressRequest shippingAddressRequest) {

        shippingAddressService.updateUserShippingAddress(shippingAddressId, shippingAddressRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .build()
                .toUri();

        return ResponseEntity.created(location).build();
    }

}
