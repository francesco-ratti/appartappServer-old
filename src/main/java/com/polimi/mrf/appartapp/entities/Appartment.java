package com.polimi.mrf.appartapp.entities;

import javax.persistence.*;

@Entity
public class Apartment {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
