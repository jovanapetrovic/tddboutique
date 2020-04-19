package com.jovana.entity.order;

import com.jovana.entity.AbstractAuditingEntity;
import com.jovana.entity.product.ColorCode;
import com.jovana.entity.product.Product;
import com.jovana.entity.product.SizeCode;
import com.jovana.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

/**
 * Created by jovana on 13.04.2020
 *
 */
@Entity
@Table(name = "order_item")
public class OrderItem extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private SizeCode productSize;

    @Enumerated(EnumType.STRING)
    private ColorCode productColor;

    @PositiveOrZero
    @NotNull
    private Long quantity;

    @Enumerated(EnumType.STRING)
    private OrderState orderState = OrderState.CART;

    public OrderItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public SizeCode getProductSize() {
        return productSize;
    }

    public void setProductSize(SizeCode productSize) {
        this.productSize = productSize;
    }

    public ColorCode getProductColor() {
        return productColor;
    }

    public void setProductColor(ColorCode productColor) {
        this.productColor = productColor;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

}
