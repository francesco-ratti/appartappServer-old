package com.polimi.mrf.appartapp.beans;

import com.polimi.mrf.appartapp.entities.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Stateless(name="ApartmentServiceBean")
public class ApartmentServiceBean {

    public static final String apartmentImagesFolderPath = System.getProperty("user.home")+"\\uploadedImages\\apartments\\";

    @EJB(name = "com.polimi.mrf.appartapp.beans/ImgIdServiceBean")
    ImgIdServiceBean imgIdServiceBean;

    @PersistenceContext(unitName = "appartapp")
    private EntityManager em;

    public void likeApartment(User user, Long apartmentId) {
        Apartment apartmentToLike=new Apartment();
        //avoiding useless lookup with find, since equals method of Apartment class has been overridden, returns true if ids are the same
        apartmentToLike.setId(apartmentId);
        if (!user.getLikedApartments().contains(apartmentToLike)) {
            user.addLikedApartment(em.find(Apartment.class, apartmentId));
            em.merge(user);
            em.flush();
        }
    }
    public void ignoreApartment(User user, Long apartmentId) {
        Apartment apartmentToIgnore=new Apartment();
        //avoiding useless lookup with find, since equals method of Apartment class has been overridden, returns true if ids are the same
        apartmentToIgnore.setId(apartmentId);
        if (!user.getIgnoredApartments().contains(apartmentToIgnore)) {
            user.addIgnoredApartment(em.find(Apartment.class, apartmentId));
            em.merge(user);
            em.flush();
        }
    }

    public void likeUser(Apartment apartment, Long userId) {
        User userToLike=new User();
        //avoiding useless lookup with find, since equals method of Apartment class has been overridden, returns true if ids are the same
        userToLike.setId(userId);
        //if (!apartment.getMatches().containsKey(userToLike)) {
        //TODO fix dupli
            Match match = new Match(em.find(User.class, userId), new Date());
            apartment.addMatch(match);
        try {
            em.persist(match);
            em.merge(apartment);
        } catch (PersistenceException ignored) {
            //duplicate entity
        }
        em.flush();
       // }
    }

    public void ignoreUser(Apartment apartment, Long userId) {
        User userToIgnore=new User();
        //avoiding useless lookup with find, since equals method of Apartment class has been overridden, returns true if ids are the same
        userToIgnore.setId(userId);
        if (!apartment.getIgnoredUsers().contains(userToIgnore)) {
            apartment.addIgnoredUser(em.find(User.class, userId));
            em.merge(apartment);
            em.flush();
        }
    }

    private Apartment appendImages(Apartment apartment, List<InputStream> images) throws IOException {
        for (InputStream image: images) {
            long currId=imgIdServiceBean.getNewApartmentImageId();

            Path path=Path.of(apartmentImagesFolderPath + currId+".jpg");
            Files.copy(image, path, StandardCopyOption.REPLACE_EXISTING);
            ApartmentImage apartmentImage=new ApartmentImage();
            apartmentImage.setId(currId);

            apartment.addImage(apartmentImage);
        }
        return apartment;
    }

    public Apartment createApartment(User user, String listingTitle, String description, int price, String address, String additionalExpenseDetail, List<InputStream> images) throws IOException {
        Apartment apartment=new Apartment();
        apartment.setListingTitle(listingTitle);
        apartment.setDescription(description);
        apartment.setPrice(price);
        apartment.setAddress(address);
        apartment.setAdditionalExpenseDetail(additionalExpenseDetail);
        apartment.setOwner(user);
        apartment=appendImages(apartment, images);
        em.persist(apartment);
        em.merge(user);
        return apartment;
    }

    public Apartment addImage(Apartment apartment, List<InputStream> images) throws IOException {
        apartment=appendImages(apartment, images);
        em.merge(apartment);
        return apartment;
    }

    public Apartment getApartment(long id) {
        return em.find(Apartment.class, id);
    }

    public boolean deleteImage(Apartment apartment, long imageId) throws IOException {
        if (apartment.removeImage(imageId)) {
            em.merge(apartment);
            Path path=Path.of(apartmentImagesFolderPath + imageId+".jpg");
            Files.delete(path);
            return true;
        } else {
            return false;
        }
    }

    public void updateApartment(Apartment apartment) {
        em.merge(apartment);
    }
}
