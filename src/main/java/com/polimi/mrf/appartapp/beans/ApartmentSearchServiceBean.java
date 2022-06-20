package com.polimi.mrf.appartapp.beans;

import com.polimi.mrf.appartapp.entities.Apartment;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Stateful(name="ApartmentSearchServiceBean")
//@StatefulTimeout(value = 5, unit = TimeUnit.MINUTES)
public class ApartmentSearchServiceBean {
    @PersistenceContext(unitName = "appartapp")
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
