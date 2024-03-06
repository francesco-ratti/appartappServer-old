package com.polimi.mrf.appart.beans;

import com.polimi.mrf.appart.entities.GoogleUser;
import com.polimi.mrf.appart.entities.User;
import com.polimi.mrf.appart.entities.UserAuthToken;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless(name="UserAuthServiceBean")
public class UserAuthServiceBean {

    @PersistenceContext(unitName = "appart")
    private EntityManager em;

    public GoogleUser findGoogleUserByGoogleId(String googleId) {
        List<GoogleUser> resultList=em.createNamedQuery("GoogleUser.findByGoogleId", GoogleUser.class).setParameter("googleId", googleId).getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    public UserAuthToken findAuthTokenBySelector(String selector) {
        List<UserAuthToken> list = em.createNamedQuery("UserAuthToken.findBySelector", UserAuthToken.class).setParameter("selector", selector).getResultList();

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    public void create(UserAuthToken newToken) {
        em.persist(newToken);
    }

    public void update(UserAuthToken newToken) {
        em.merge(newToken);
    }
}
