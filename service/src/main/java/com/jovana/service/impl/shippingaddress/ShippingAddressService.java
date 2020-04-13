package com.jovana.service.impl.shippingaddress;

import com.jovana.entity.shippingaddress.ShippingAddress;
import com.jovana.entity.shippingaddress.dto.ShippingAddressRequest;
import com.jovana.entity.shippingaddress.dto.ShippingAddressResponse;

import java.util.Set;

/**
 * Created by jovana on 31.03.2020
 */
public interface ShippingAddressService {

    ShippingAddress getUserShippingAddressById(Long shippingAddressId);
    
    Long addUserShippingAddress(Long userId, ShippingAddressRequest shippingAddressRequest);

    Long updateUserShippingAddress(Long shippingAddressId, ShippingAddressRequest shippingAddressRequest);

    Set<ShippingAddressResponse> viewAllShippingAddresses(Long userId);

}
