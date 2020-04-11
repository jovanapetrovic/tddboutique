package com.jovana.repositories.product;

import com.jovana.entity.product.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jovana on 11.04.2020
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByProductIdAndName(Long productId, String name);

}
