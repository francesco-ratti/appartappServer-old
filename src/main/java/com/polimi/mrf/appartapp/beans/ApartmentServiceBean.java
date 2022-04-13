package com.polimi.mrf.appartapp.beans;

import com.polimi.mrf.appartapp.ImgIdService;
import com.polimi.mrf.appartapp.entities.Apartment;
import com.polimi.mrf.appartapp.entities.ApartmentImage;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Stateless(name="ApartmentServiceBean")
public class ApartmentServiceBean {

    public static final String apartmentImageFolder = "C:\\uploadedImages\\apartments\\";

    @PersistenceContext(unitName = "appartapp")
    private EntityManager em;
    public void likeApartment(User user, Long apartmentId) {
        Apartment apartmentToLike=new Apartment();
        //avoiding useless lookup with find, since equals method of Apartment class has been overridden, returns true if ids are the same
        apartmentToLike.setId(apartmentId);
        if (!user.getLikedApartmentList().contains(apartmentToLike)) {
            user.addLikedApartment(em.find(Apartment.class, apartmentId));
            em.merge(user);
        }
    }

    public void ignoreApartment(User user, Long apartmentId) {
        Apartment apartmentToIgnore=new Apartment();
        //avoiding useless lookup with find, since equals method of Apartment class has been overridden, returns true if ids are the same
        apartmentToIgnore.setId(apartmentId);
        if (!user.getIgnoredApartmentList().contains(apartmentToIgnore)) {
            user.addIgnoredApartment(em.find(Apartment.class, apartmentId));
            em.merge(user);
        }
    }

    public Apartment createApartment(User user, String listingTitle, String description, int price, String address, String additionalExpenseDetail, List<InputStream> images) throws IOException {
        Apartment apartment=new Apartment();
        apartment.setListingTitle(listingTitle);
        apartment.setDescription(description);
        apartment.setPrice(price);
        apartment.setAddress(address);
        apartment.setAdditionalExpenseDetail(additionalExpenseDetail);
        apartment.setOwner(user);

        for (InputStream image: images) {
            long currId=ImgIdService.getInstance().getNewApartmentImageId();
            Files.copy(image, Path.of(apartmentImageFolder + currId+".jpg"));
            ApartmentImage apartmentImage=new ApartmentImage();
            apartmentImage.setId(currId);

            apartment.addImage(apartmentImage);
        }

        em.persist(apartment);
        return apartment;
    }
}
