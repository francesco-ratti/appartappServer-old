package com.polimi.mrf.appartapp.beans;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name="ApartmentServiceBean")
public class ImgIdServiceBean {
    @PersistenceContext(unitName = "appartapp")
    private EntityManager em;

    public long getCurrentApartmentImageId() {
        List<Long> resultList=em.createNamedQuery("ApartmentImage.findMaxId", long.class).getResultList();
        return resultList.isEmpty() ? (long)0 : resultList.get(0);
    }

    public long getCurrentUserImageId() {
        List<Long> resultList= em.createNamedQuery("UserImage.findMaxId", long.class).getResultList();
        return resultList.isEmpty() ? (long)0 : resultList.get(0);
    }
}