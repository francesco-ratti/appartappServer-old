package com.polimi.mrf.appartapp.beans;

import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateful(name="UserSearchServiceBean")
public class UserSearchServiceBean {
    @PersistenceContext(unitName = "appartapp")
    private EntityManager em;

    private List<User> userList=null;
    private int resNumToFetch =0;

    public UserSearchServiceBean() {
    }

    public void SearchNewUsers(User ownerOfApartmentsToLookup) {
        userList=em.createNamedQuery("Apartment.getNewUsersWhoLikedMyApartments", User.class).setParameter("ownedApartmentList", ownerOfApartmentsToLookup.getOwnedApartments()).getResultList();
    }

    public List<User> getNewUserList() {
        return userList;
    }

    public User getNewApartmentNextResult() {
        if (resNumToFetch<userList.size()) {
            User apartment=userList.get(resNumToFetch);
            resNumToFetch++;
            return apartment;
        } else return null;
    }
}
