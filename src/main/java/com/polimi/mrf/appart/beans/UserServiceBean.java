package com.polimi.mrf.appart.beans;

import com.polimi.mrf.appart.enums.Gender;
import com.polimi.mrf.appart.entities.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.commons.io.IOUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Stateless(name = "UserServiceBean")
public class UserServiceBean {
    public static final String userImagesFolderPath = System.getProperty("user.home")+"\\uploadedImages\\users\\";

    @EJB(name = "com.polimi.mrf.appart.beans/ImgServiceBean")
    ImgServiceBean ImgServiceBean;

    @PersistenceContext(unitName = "appart")
    private EntityManager em;
    public UserServiceBean() {
    }

    public User getUser(String email, String password) {
        List<User> res=em.createNamedQuery("User.checkCredentials", User.class).setParameter("email", email).setParameter("password", password).getResultList();
        if (res.size()>0)
            return res.get(0);
        else
            return null;
    }

    public List<Apartment> getOwnedApartments(User user) {
        return em.createNamedQuery("User.getOwnedApartments", Apartment.class).setParameter("user", user).getResultList();
    }

    public User createGoogleUser(String googleId, String email, String name, String surname, Date birthday, Gender gender) {
        GoogleUser u=new GoogleUser();
        u.setGoogleId(googleId);
        u.setEmail(email);
        u.setName(name);
        u.setSurname(surname);
        u.setBirthday(birthday);
        u.setGender(gender);

        em.persist(u);
        return u;
    }

    public User updateGoogleUser(GoogleUser googleUser) {
        em.merge(googleUser);
        return googleUser;
    }

    public User createCredentialsUser (String email, String password, String name, String surname, Date birthday, Gender gender){
        CredentialsUser u=new CredentialsUser();
        u.setEmail(email);
        u.setPassword(password);
        u.setName(name);
        u.setSurname(surname);
        u.setBirthday(birthday);
        u.setGender(gender);

        em.persist(u);
        return u;
    }
    public boolean UserExists(String email) {
        List<User> users=em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getResultList();
        return (users!=null && !users.isEmpty());
    }

    private User appendImages(User user, List<InputStream> images) throws IOException {
        for (InputStream image: images) {
//            long currId=ImgServiceBean.getNewUserImageId();

//            Path path=Path.of(userImagesFolderPath + currId+".jpg");
//            Files.copy(image, path, StandardCopyOption.REPLACE_EXISTING);
            UserImage userImage=new UserImage();
            userImage.setImageBytes(IOUtils.toByteArray(image));
//            userImage.setId(currId);

            user.addImage(userImage);
        }
        return user;
    }

    public User addImage(User user, List<InputStream> images) throws IOException {
        user=appendImages(user, images);
        em.merge(user);
        return user;
    }

    public boolean deleteImage(User user, long imageId) throws IOException {
        if (user.removeImage(imageId)) {
            em.merge(user);
            Path path=Path.of(userImagesFolderPath + imageId+".jpg");
            Files.delete(path);
            return true;
        } else {
            return false;
        }
    }

    public List<Match> getMatchedApartments(User user) {
        System.out.println(user.getId());
        return em.createNamedQuery("User.findMatchedApartments", Match.class).setParameter("user", user).getResultList();
    }

    public List<Apartment> getMatchedApartmentsFromDate(User user, Date date) {
        return em.createNamedQuery("User.findMatchedApartmentsFromDate", Apartment.class).setParameter("user", user).setParameter("date", date).getResultList();
    }

    public void updateUser(User user) {
        em.merge(user);
    }

}