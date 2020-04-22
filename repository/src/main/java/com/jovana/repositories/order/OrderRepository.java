package com.jovana.repositories.order;

import com.jovana.entity.order.Order;
import com.jovana.entity.order.dto.OrderFullResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by jovana on 20.04.2020
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Set<Order> findAllByUserId(Long userId);

    @Query("select new com.jovana.entity.order.dto.OrderFullResponse(o) " +
            "from Order o " +
            "where o.id = :orderId " +
            "and o.user.id = :userId ")
    OrderFullResponse findOneWithOrderItemsByOrderIdAndUserId(@Param("userId") Long userId, @Param("orderId") Long orderId);

}
