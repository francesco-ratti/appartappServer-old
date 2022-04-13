package com.polimi.mrf.appartapp.entities;

import javax.persistence.*;

@NamedQuery(name="ApartmentImage.findMaxId",
        query="SELECT MAX(ai.id) FROM ApartmentImage ai"
)
@Entity
public class ApartmentImage extends Image {
    @ManyToOne
    private Apartment apartment;

    public void setApartment(Apartment apartment) {
        this.apartment=apartment;
    }

    public Apartment getApartment() {
        return apartment;
    }
}
