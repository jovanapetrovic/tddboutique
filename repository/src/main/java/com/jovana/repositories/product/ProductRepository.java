package com.jovana.repositories.product;

import com.jovana.entity.product.Product;
import com.jovana.entity.product.dto.ProductFullResponse;
import com.jovana.entity.product.dto.ProductResponse;
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

    @Query("select new com.jovana.entity.product.dto.ProductFullResponse(p) " +
            "from Product p " +
            "where p.id = :productId " +
            "and p.deleted = false ")
    ProductFullResponse findOneWithImages(@Param("productId") Long productId);

    @Query("select new com.jovana.entity.product.dto.ProductResponse(p) " +
            "from Product p " +
            "where p.deleted = false ")
    Set<ProductResponse> findAllWithImages();

    @Query("select new com.jovana.entity.product.dto.ProductResponse(p) " +
            "from Product p " +
            "where LOWER(p.name) like CONCAT('%', LOWER(:searchText), '%') " +
            "or LOWER(p.description) like CONCAT('%', LOWER(:searchText), '%') " +
            "and p.deleted = false ")
    Set<ProductResponse> findAllWhereNameOrDescriptionContainsText(@Param("searchText") String searchText);

}
