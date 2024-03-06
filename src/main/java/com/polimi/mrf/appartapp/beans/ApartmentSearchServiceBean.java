package com.polimi.mrf.appart.beans;

import com.polimi.mrf.appart.entities.Apartment;
import com.polimi.mrf.appart.entities.User;


import jakarta.ejb.Stateful;
import jakarta.ejb.StatefulTimeout;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Stateful(name="ApartmentSearchServiceBean")
@StatefulTimeout(value = 4, unit = TimeUnit.MINUTES)
public class ApartmentSearchServiceBean {
    @PersistenceContext(unitName = "appart")
    private EntityManager em;

    private List<Apartment> apartmentList=null;
    private int resNumToFetch=0;

    public ApartmentSearchServiceBean() {
    }

    public void SearchNewApartments(User userToLookup) {
        apartmentList=em.createNamedQuery("User.getNewApartments", Apartment.class).setParameter("userId", userToLookup.getId()).getResultList();
    }

    public List<Apartment> getNewApartmentList() {
        return apartmentList;
    }

    public Apartment getNewApartmentNextResult() {
        if (resNumToFetch<apartmentList.size()) {
            Apartment apartment=apartmentList.get(resNumToFetch);
            resNumToFetch++;
            return apartment;
        } else return null;
    }
}
