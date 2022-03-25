package com.polimi.mrf.appartapp.beans;

import com.polimi.mrf.appartapp.Gender;
import com.polimi.mrf.appartapp.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Stateless(name = "UserServiceBean")
public class UserServiceBean {
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
        User user=em.find(User.class, email);
        return user!=null;
    }
}