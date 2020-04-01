package com.jovana.entity.shippingaddress;

import com.jovana.entity.AbstractAuditingEntity;
import com.jovana.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by jovana on 31.03.2020
 */
@Entity
@Table(name = "shipping_address")
public class ShippingAddress extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Size(min = 2, max = 30)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30)
    private String lastName;

    @NotNull
    @Size(min = 2, max = 30)
    private String address;

    @NotNull
    @Positive
    private Long zipCode;

    @NotNull
    @Size(min = 2, max = 30)
    private String city;

    @NotNull
    @Size(min = 4, max = 30)
    private String country;

    @Embedded
    private Phone phone;

    public ShippingAddress() {
    }

    public static ShippingAddress createUserShippingAddress(String firstName, String lastName, String address,
                                                            Long zipCode, String city, String country, String phoneNumber) {
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.firstName = firstName;
        shippingAddress.lastName = lastName;
        shippingAddress.address = address;
        shippingAddress.zipCode = zipCode;
        shippingAddress.city = city;
        shippingAddress.country = country;
        shippingAddress.phone = Phone.createPhoneNumber(phoneNumber);
        return shippingAddress;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getZipCode() {
        return zipCode;
    }

    public void setZipCode(Long zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(String phoneNumber) {
        this.phone = Phone.createPhoneNumber(phoneNumber);
    }

}
