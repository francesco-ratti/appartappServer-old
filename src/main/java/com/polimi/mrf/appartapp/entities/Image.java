package com.polimi.mrf.appartapp.entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public abstract class Image {
    @Expose
    @Id
    private Long id;
    //@Column(nullable = true)
    //public String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Image)) {
            return false;
        }

        Image i=(Image) o;

        return Long.compare(i.id, this.id)==0;
    }
}
