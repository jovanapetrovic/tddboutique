package com.jovana.entity.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jovana on 05.04.2020
 */
public class ChangeEmailAddressRequest {

    @NotNull
    @Size(min = 6, max = 50)
    @Email(regexp = "^[a-zA-Z0-9_+]+(?:\\.[a-zA-Z0-9_+]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
    private String newEmailAddress;

    public String getNewEmailAddress() {
        return newEmailAddress;
    }

    public void setNewEmailAddress(String newEmailAddress) {
        this.newEmailAddress = newEmailAddress;
    }

}
