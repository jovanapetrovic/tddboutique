package com.jovana.repositories.product;

import com.jovana.entity.product.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jovana on 10.04.2020
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {



}
