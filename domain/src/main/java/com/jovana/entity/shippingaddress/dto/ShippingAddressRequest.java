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

    private ShippingAddressRequest() {
    }

    public static ShippingAddressRequest createUserShippingAddressRequest(Boolean useUsersFirstAndLastName, String firstName,
                                                                          String lastName, String address, Long zipCode,
                                                                          String city, String country, String phoneNumber) {
        ShippingAddressRequest shippingAddressRequest = new ShippingAddressRequest();
        shippingAddressRequest.useFirstAndLastNameFromUser = useUsersFirstAndLastName;
        shippingAddressRequest.firstName = firstName;
        shippingAddressRequest.lastName = lastName;
        shippingAddressRequest.address = address;
        shippingAddressRequest.zipCode = zipCode;
        shippingAddressRequest.city = city;
        shippingAddressRequest.country = country;
        shippingAddressRequest.phoneNumber = phoneNumber;
        return shippingAddressRequest;
    }

    public Boolean getUseFirstAndLastNameFromUser() {
        return useFirstAndLastNameFromUser;
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

    public Long getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
