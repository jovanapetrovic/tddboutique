package com.jovana.repositories.order;

import com.jovana.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by jovana on 20.04.2020
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Set<Order> findAllByUserId(Long userId);

}
