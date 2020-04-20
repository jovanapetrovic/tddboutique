package com.jovana.repositories.order;

import com.jovana.entity.order.OrderItem;
import com.jovana.entity.order.dto.CartItemResponse;
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

//    @Query("select it from OrderItem it, Product p " +
//            "left join p.images i " +
//            "where it.product.id = p.id " +
//            "and it.user.id = :userId " +
//            "and it.orderState = 'CART' " +
//            "and p.deleted = false ")
    @Query("select new com.jovana.entity.order.dto.CartItemResponse(it, p) " +
            "from OrderItem it, Product p " +
            "where it.product.id = p.id " +
            "and it.user.id = :userId " +
            "and it.orderState = 'CART' " +
            "and p.deleted = false ")
    Set<CartItemResponse> findAllCartItemsWithProductDataByUserId(@Param("userId") Long userId);

}
