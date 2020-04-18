package com.jovana.entity.coupon;

import com.jovana.entity.AbstractAuditingEntity;
import com.jovana.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by jovana on 18.04.2020
 */
@Entity
@Table(name = "coupon")
public class Coupon extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Size(min = 12, max = 12)
    private String code;

    @Enumerated(EnumType.STRING)
    private CouponValue value;

    @Enumerated(EnumType.STRING)
    private CouponStatus status = CouponStatus.ACTIVE;

    @NotNull
    @Future
    private LocalDateTime expiryDate;

    public Coupon() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CouponValue getValue() {
        return value;
    }

    public void setValue(CouponValue value) {
        this.value = value;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

}