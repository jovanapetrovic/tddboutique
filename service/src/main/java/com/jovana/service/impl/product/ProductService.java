package com.jovana.service.impl.product;

import com.jovana.entity.product.Product;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.entity.product.dto.UpdateStockRequest;

/**
 * Created by jovana on 07.04.2020
 */
public interface ProductService {

    Product getProductById(Long productId);

    Long addProduct(ProductRequest productRequest);

    Long updateProductStock(Long productId, UpdateStockRequest updateStockRequest);

}
