package com.polimi.mrf.appartapp.beans;

import com.polimi.mrf.appartapp.Gender;
import com.polimi.mrf.appartapp.entities.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Stateless(name = "UserServiceBean")
public class UserServiceBean {
    public static final String userImagesFolderPath = System.getProperty("user.home")+"\\uploadedImages\\users\\";

    @EJB(name = "com.polimi.mrf.appartapp.beans/ImgIdServiceBean")
    ImgIdServiceBean imgIdServiceBean;

    @PersistenceContext(unitName = "appartapp")
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

    public User createUser(String email, String password, String name, String surname, Date birthday, Gender gender) {
        User u=new User();
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
        return (users!=null && users.size()>0);
    }

    private User appendImages(User user, List<InputStream> images) throws IOException {
        for (InputStream image: images) {
            long currId=imgIdServiceBean.getNewUserImageId();

            Path path=Path.of(userImagesFolderPath + currId+".jpg");
            Files.copy(image, path);
            UserImage userImage=new UserImage();
            userImage.setId(currId);

            user.addImage(userImage);
        }
        return user;
    }

    public User addImage(User user, List<InputStream> images) throws IOException {
        user=appendImages(user, images);
        em.merge(user);
        return user;
    }

    public boolean deleteImage(User user, long imageId) {
        if (user.removeImage(imageId)) {
            em.merge(user);
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