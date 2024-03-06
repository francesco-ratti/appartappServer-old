package com.polimi.mrf.appart.beans;

import com.polimi.mrf.appart.entities.User;
import com.polimi.mrf.appart.entities.UserApartmentContainer;


import jakarta.ejb.Stateful;
import jakarta.ejb.StatefulTimeout;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Stateful(name="UserSearchServiceBean")
@StatefulTimeout(value = 4, unit = TimeUnit.MINUTES)
public class UserSearchServiceBean {
    @PersistenceContext(unitName = "appart")
    private EntityManager em;

    private List<UserApartmentContainer> userList=null;
    private int resNumToFetch =0;

    public UserSearchServiceBean() {
    }

    public void SearchNewUsers(User ownerOfApartmentsToLookup) {
        if (!ownerOfApartmentsToLookup.getOwnedApartments().isEmpty()) {
            /*List<Long> OwnedApartmentsIds=new ArrayList<Long>(ownerOfApartmentsToLookup.getOwnedApartments().size());
            for (Apartment a : ownerOfApartmentsToLookup.getOwnedApartments()) {
                OwnedApartmentsIds.add(a.getId());
            }*/
            userList = em.createNamedQuery("Apartment.getNewUsersWhoLikedMyApartments", UserApartmentContainer.class).setParameter("ownedApartmentList", ownerOfApartmentsToLookup.getOwnedApartments()).getResultList();
        } else {
            userList = new ArrayList<>();
        }
    }

    public List<UserApartmentContainer> getNewUserList() {
        return userList;
    }

    public UserApartmentContainer getNewApartmentNextResult() {
        if (resNumToFetch<userList.size()) {
            UserApartmentContainer apartment=userList.get(resNumToFetch);
            resNumToFetch++;
            return apartment;
        } else return null;
    }
}
