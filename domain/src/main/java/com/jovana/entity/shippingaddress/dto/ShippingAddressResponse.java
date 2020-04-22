package com.jovana.entity.shippingaddress.dto;

import com.jovana.entity.shippingaddress.ShippingAddress;

/**
 * Created by jovana on 13.04.2020
 */
public class ShippingAddressResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private Long zipCode;
    private String city;
    private String country;
    private String phoneNumber;

    public ShippingAddressResponse() {
    }

    public static ShippingAddressResponse createFromShippingAddress(ShippingAddress shippingAddress) {
        ShippingAddressResponse response = new ShippingAddressResponse();
        response.setId(shippingAddress.getId());
        response.setFirstName(shippingAddress.getFirstName());
        response.setLastName(shippingAddress.getLastName());
        response.setAddress(shippingAddress.getAddress());
        response.setZipCode(shippingAddress.getZipCode());
        response.setCity(shippingAddress.getCity());
        response.setCountry(shippingAddress.getCountry());
        response.setPhoneNumber(shippingAddress.getPhone().getPhoneNumber());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
