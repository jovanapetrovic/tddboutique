package com.jovana.entity.shippingaddress;

import javax.persistence.Embeddable;

/**
 * Created by jovana on 31.03.2020
 */
@Embeddable
public class Phone {

    private String phoneNumber;

    private Phone() {
    }

    public static Phone createPhoneNumber(String phoneNumber) {
        Phone phone = new Phone();
        phone.phoneNumber = phoneNumber;
        return phone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
