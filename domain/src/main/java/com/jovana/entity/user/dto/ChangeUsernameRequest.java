package com.jovana.entity.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jovana on 05.04.2020
 */
public class ChangeUsernameRequest {

    @NotNull
    @Size(min = 6, max = 30)
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
