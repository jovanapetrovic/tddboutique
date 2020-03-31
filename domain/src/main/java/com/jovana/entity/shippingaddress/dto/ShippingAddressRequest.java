package com.jovana.entity.shippingaddress.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jovana on 31.03.2020
 */
public class ShippingAddressRequest {

    private Boolean useFirstAndLastNameFromUser = true;
    private String firstName;
    private String lastName;

    @NotNull
    @Size(min = 2, max = 30)
    private String address;

    @NotNull
    @Size(min = 5, max = 10)
    private Long zipCode;

    @NotNull
    @Size(min = 2, max = 30)
    private String city;

    @NotNull
    @Size(min = 4, max = 30)
    private String country;

    @NotNull
    private String phoneNumber;

    public ShippingAddressRequest() {
    }

    public Boolean getUseFirstAndLastNameFromUser() {
        return useFirstAndLastNameFromUser;
    }

    public void setUseFirstAndLastNameFromUser(Boolean useFirstAndLastNameFromUser) {
        this.useFirstAndLastNameFromUser = useFirstAndLastNameFromUser;
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
