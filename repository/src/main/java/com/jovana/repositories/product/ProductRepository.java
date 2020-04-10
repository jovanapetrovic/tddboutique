package com.jovana.repositories.product;

import com.jovana.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jovana on 07.04.2020
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

}
