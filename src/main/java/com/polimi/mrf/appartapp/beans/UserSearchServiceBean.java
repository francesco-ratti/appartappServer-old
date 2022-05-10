package com.polimi.mrf.appartapp.beans;

import com.polimi.mrf.appartapp.entities.User;
import com.polimi.mrf.appartapp.entities.UserApartmentContainer;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateful(name="UserSearchServiceBean")
public class UserSearchServiceBean {
    @PersistenceContext(unitName = "appartapp")
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
