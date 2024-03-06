package com.polimi.mrf.appart.entities;

import com.google.gson.annotations.Expose;

public class UserApartmentContainer {
    @Expose
    public User user;
    @Expose
    public Apartment apartment;

    public UserApartmentContainer(User user, Apartment apartment) {
        this.user = user;
        this.apartment = apartment;
    }

    public UserApartmentContainer() {

    }

    public Apartment getApartment() {
        return apartment;
    }

    public User getUser() {
        return user;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
