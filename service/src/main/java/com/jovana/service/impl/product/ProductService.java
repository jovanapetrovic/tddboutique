package com.jovana.service.impl.product;

import com.jovana.entity.product.Product;
import com.jovana.entity.product.dto.ProductFullResponse;
import com.jovana.entity.product.dto.ProductRequest;
import com.jovana.entity.product.dto.ProductResponse;
import com.jovana.entity.product.dto.UpdateStockRequest;

import java.util.List;
import java.util.Set;

/**
 * Created by jovana on 07.04.2020
 */
public interface ProductService {

    Product getProductById(Long productId);

    Long addProduct(ProductRequest productRequest);

    Long updateProduct(Long productId, ProductRequest productRequest);

    Long updateProductStock(Long productId, UpdateStockRequest updateStockRequest);

    boolean validateAndProcessProductStock(Product product, Long productQuantity);

    boolean deleteProduct(Long productId);

    ProductFullResponse viewOneProduct(Long productId);

    Set<ProductResponse> viewAllProducts();

    Set<ProductResponse> searchProducts(String searchText);

}
