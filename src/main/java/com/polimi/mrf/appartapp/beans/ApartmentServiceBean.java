package com.polimi.mrf.appartapp.beans;

import com.polimi.mrf.appartapp.entities.Apartment;
import com.polimi.mrf.appartapp.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ApartmentServiceBean {
    @PersistenceContext(unitName = "appartapp")
    private EntityManager em;
    public void likeApartment(User user, Long apartmentId) {
        user.addLikedApartment(em.find(Apartment.class, apartmentId));
        em.merge(user);
    }

    public void ignoreApartment(User user, Long apartmentId) {
        user.addIgnoredApartment(em.find(Apartment.class, apartmentId));
        em.merge(user);
    }

    public Apartment createApartment(User user, String listingTitle, String description, int price, String address, String additionalExpenseDetail) {
        Apartment apartment=new Apartment();
        apartment.setListingTitle(listingTitle);
        apartment.setDescription(description);
        apartment.setPrice(price);
        apartment.setAddress(address);
        apartment.setAdditionalExpenseDetail(additionalExpenseDetail);
        apartment.setOwner(user);

        em.persist(apartment);
        return apartment;
    }
}
