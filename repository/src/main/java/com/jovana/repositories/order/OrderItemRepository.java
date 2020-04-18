package com.jovana.repositories.order;

import com.jovana.entity.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by jovana on 13.04.2020
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select it from OrderItem it " +
            "where it.user.id = :userId " +
            "and it.orderState = 'CART' ")
    Set<OrderItem> findAllCartItemsByUserId(@Param("userId") Long userId);

}
