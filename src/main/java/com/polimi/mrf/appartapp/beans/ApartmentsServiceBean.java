package com.polimi.mrf.appartapp.beans;

import com.polimi.mrf.appartapp.entities.Apartment;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateful(name="ApartmentsServiceBean")
public class ApartmentsServiceBean {
    @PersistenceContext(unitName = "appartapp")
    private EntityManager em;

    private List<Apartment> apartmentList=null;
    private int resNumToFetch =0;

    public ApartmentsServiceBean() {
    }

    public void SearchNewApartments(User userToLookup) {
        User user=em.find(User.class, userToLookup);
        apartmentList=em.createNamedQuery("User.getNewApartments", Apartment.class).getResultList();
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
