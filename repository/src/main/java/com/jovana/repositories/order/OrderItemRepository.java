package com.jovana.repositories.order;

import com.jovana.entity.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jovana on 13.04.2020
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {



}
