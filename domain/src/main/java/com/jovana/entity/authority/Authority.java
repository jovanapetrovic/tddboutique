package com.jovana.entity.authority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by jovana on 24.02.2020
 */
@Entity
@Table(name = "authority")
public class Authority implements Serializable {

    @NotNull
    @Size(max = 50)
    @Id
    private String name;

    private Authority() {
    }

    public Authority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
