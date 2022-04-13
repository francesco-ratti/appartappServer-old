package com.polimi.mrf.appartapp.entities;

import javax.persistence.*;


public class ApartmentImage extends Image {
    @ManyToOne
    private Apartment apartment;
}
