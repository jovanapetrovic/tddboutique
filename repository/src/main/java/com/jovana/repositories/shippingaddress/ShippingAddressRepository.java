package com.jovana.repositories.shippingaddress;

import com.jovana.entity.shippingaddress.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by jovana on 31.03.2020
 */
@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {

    ShippingAddress findByUserId(Long userId);

    Set<ShippingAddress> findAllByUserId(Long userId);

}
