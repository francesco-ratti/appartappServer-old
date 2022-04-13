package com.polimi.mrf.appartapp.beans;


import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

//SINGLETON in charge of assigning ids to images

/*
if it's the first run retrive MAX IDs from data store;
next calls wont't require query to data store since id will be handed by this singleton
 */
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Singleton(name="ImgIdServiceBean")
public class ImgIdServiceBean {
    @PersistenceContext(unitName = "appartapp")
    private EntityManager em;

    private long currentUserImageId;
    private long currentApartmentImageId;

    public ImgIdServiceBean() {    }

    //first run, retrieve max ids from data store
    @PostConstruct
    private void fetchFromDataStore() {
        List<Long> apResultList=em.createNamedQuery("ApartmentImage.findMaxId", long.class).getResultList();
        currentApartmentImageId=apResultList.size()!=0 && apResultList.get(0)!=null ? apResultList.get(0) : (long)0;

        List<Long> uResultList= em.createNamedQuery("UserImage.findMaxId", long.class).getResultList();
        currentUserImageId=uResultList.size()!=0 && uResultList.get(0)!=null ? uResultList.get(0) : (long)0;
    }

    @Lock(LockType.READ)
    public long getCurrentUserImageId() {
        return currentUserImageId;
    }

    @Lock(LockType.READ)
    public long getCurrentApartmentImageId() {
        return currentApartmentImageId;
    }

    @Lock(LockType.WRITE)
    public long getNewApartmentImageId() {
        currentApartmentImageId++;
        return currentApartmentImageId;
    }

    @Lock(LockType.WRITE)
    public long getNewUserImageId() {
        currentUserImageId++;
        return currentUserImageId;
    }
}