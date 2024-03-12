package com.polimi.mrf.appart.beans;


import com.polimi.mrf.appart.entities.Apartment;
import com.polimi.mrf.appart.entities.ApartmentImage;
import com.polimi.mrf.appart.entities.Image;
import com.polimi.mrf.appart.entities.UserImage;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

//SINGLETON in charge of assigning ids to images

/*
if it's the first run retrive MAX IDs from data store;
next calls wont't require query to data store since id will be handed by this singleton
 */
//@Startup
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
//@Singleton(name="ImgServiceBean")
@Stateless(name="ApartmentServiceBean")
public class ImgServiceBean {
    @PersistenceContext(unitName = "appart")
    private EntityManager em;

//    private long currentUserImageId;
//    private long currentApartmentImageId;

    public ImgServiceBean() {    }

    //first run, retrieve max ids from data store

    public byte[] getApartmentImageBytesById(long imgId) {
        ApartmentImage aImg=em.find(ApartmentImage.class, imgId);
        if (aImg==null)
            return null;
        else
            return aImg.getImageBytes();
    }
    public Image getApartmentImageById(long imgId) {
        return em.find(ApartmentImage.class, imgId);
    }

    public byte[] getUserImageBytesById(long imgId) {
        UserImage uImg = em.find(UserImage.class, imgId);
        if (uImg==null)
            return null;
        else
            return uImg.getImageBytes();
    }
    public Image getUserImageById(long imgId) {
        return em.find(UserImage.class, imgId);
    }

//    @PostConstruct
//    private void fetchFromDataStore() {
//        List<Long> apResultList=em.createNamedQuery("ApartmentImage.findMaxId", long.class).getResultList();
//        currentApartmentImageId=apResultList.size()!=0 && apResultList.get(0)!=null ? apResultList.get(0) : (long)0;
//
//        List<Long> uResultList= em.createNamedQuery("UserImage.findMaxId", long.class).getResultList();
//        currentUserImageId=uResultList.size()!=0 && uResultList.get(0)!=null ? uResultList.get(0) : (long)0;
//    }
//
//    @Lock(LockType.READ)
//    public long getCurrentUserImageId() {
//        return currentUserImageId;
//    }
//
//    @Lock(LockType.READ)
//    public long getCurrentApartmentImageId() {
//        return currentApartmentImageId;
//    }
//
//    @Lock(LockType.WRITE)
//    public long getNewApartmentImageId() {
//        currentApartmentImageId++;
//        return currentApartmentImageId;
//    }
//
//    @Lock(LockType.WRITE)
//    public long getNewUserImageId() {
//        currentUserImageId++;
//        return currentUserImageId;
//    }
}