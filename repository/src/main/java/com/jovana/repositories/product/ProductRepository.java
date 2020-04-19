package com.jovana.repositories.product;

import com.jovana.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by jovana on 07.04.2020
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    @Query("select p from Product p " +
            "left join p.images i " +
            "where p.id = :productId " +
            "and p.deleted = false ")
    Product findOneWithImages(@Param("productId") Long productId);

    @Query("select p from Product p " +
            "left join p.images i " +
            "where p.deleted = false ")
    Set<Product> findAllWithImages();

}
