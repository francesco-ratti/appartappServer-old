package com.polimi.mrf.appartapp.beans;

import com.polimi.mrf.appartapp.entities.Appartment;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateful(name="AppartmentsServiceBean")
public class AppartmentsServiceBean {
    @PersistenceContext(unitName = "appartapp")
    private EntityManager em;

    private List<Appartment> appartmentList=null;
    private int resNumToFetch =0;

    public AppartmentsServiceBean() {
    }

    public void SearchNewAppartments(User userToLookup) {
        User user=em.find(User.class, userToLookup);
        appartmentList=em.createNamedQuery("User.getNewAppartments", Appartment.class).getResultList();
    }

    public List<Appartment> getNewAppartmentList() {
        return appartmentList;
    }

    public Appartment getNewAppartmentNextResult() {
        if (resNumToFetch<appartmentList.size()) {
            Appartment appartment=appartmentList.get(resNumToFetch);
            resNumToFetch++;
            return appartment;
        } else return null;
    }

}
