package com.jovana.api;

import com.jovana.entity.PathConstants;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.entity.shippingaddress.dto.ShippingAddressResponse;
import com.jovana.service.impl.shippingaddress.ShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

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
    public ResponseEntity<Void> updateShippingAddressPUT(@PathVariable("shippingAddressId") Long shippingAddressId,
                                                         @Valid @RequestBody ShippingAddressRequest shippingAddressRequest) {
        shippingAddressService.updateUserShippingAddress(shippingAddressId, shippingAddressRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = PathConstants.SHIPPING_ADDRESS_VIEW_ALL)
    public ResponseEntity<Set<ShippingAddressResponse>> viewAllShippingAddressesGET(@PathVariable("userId") Long userId) {
        Set<ShippingAddressResponse> shippingAddresses = shippingAddressService.viewAllShippingAddresses(userId);
        return new ResponseEntity<>(shippingAddresses, HttpStatus.OK);
    }

}
