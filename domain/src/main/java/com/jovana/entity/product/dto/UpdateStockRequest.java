package com.jovana.entity.product.dto;

import javax.validation.constraints.PositiveOrZero;

/**
 * Created by jovana on 12.04.2020
 */
public class UpdateStockRequest {

    @PositiveOrZero
    private Long numberOfUnitsInStock;

    public UpdateStockRequest() {
    }

    public Long getNumberOfUnitsInStock() {
        return numberOfUnitsInStock;
    }

    public void setNumberOfUnitsInStock(Long numberOfUnitsInStock) {
        this.numberOfUnitsInStock = numberOfUnitsInStock;
    }

}
