package com.jovana.entity.product;

import com.jovana.entity.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

/**
 * Created by jovana on 10.04.2020
 */
@Entity
@Table(name = "stock")
public class Stock extends AbstractAuditingEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    private Product product;

    @PositiveOrZero
    @Column(name = "units")
    private Long numberOfUnitsInStock = 1L;

    public Stock() {
    }

    public Stock(Product product, Long numberOfUnitsInStock) {
        this.product = product;
        setNumberOfUnitsInStock(numberOfUnitsInStock);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getNumberOfUnitsInStock() {
        return numberOfUnitsInStock;
    }

    public void setNumberOfUnitsInStock(Long numberOfUnitsInStock) {
        if (numberOfUnitsInStock != null && numberOfUnitsInStock >= 0 && numberOfUnitsInStock <= 3000) {
            this.numberOfUnitsInStock = numberOfUnitsInStock;
        }
    }

}
