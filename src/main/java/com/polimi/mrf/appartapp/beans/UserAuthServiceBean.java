package com.polimi.mrf.appartapp.beans;

import com.polimi.mrf.appartapp.entities.UserAuthToken;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name="UserAuthServiceBean")
public class UserAuthServiceBean {

    @PersistenceContext(unitName = "appartapp")
    private EntityManager em;

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
