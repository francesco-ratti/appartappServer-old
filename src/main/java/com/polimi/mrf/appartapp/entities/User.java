package com.polimi.mrf.appartapp.entities;

import com.polimi.mrf.appartapp.Gender;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NamedQuery(name="User.checkCredentials",
        query="SELECT u FROM User u WHERE u.email=:email AND u.password=:password"
)
@NamedQuery(name = "User.getNewAppartments", query = "SELECT h FROM Appartment h, User u WHERE u.id=:userId AND h.id NOT IN (SELECT lh.id FROM u.likedAppartmentList lh) AND h.id NOT IN (SELECT ih.id FROM u.ignoredAppartmentList ih)")
//@NamedQuery(name = "User.getNewAppartments", query = "SELECT h FROM Appartment h WHERE h.id NOT IN (SELECT lh.id FROM User u JOIN u.likedAppartmentList lh WHERE u.id=:userId) AND h.id NOT IN (SELECT ih.id FROM User u JOIN u.ignoredAppartmentList ih WHERE u.id=:userId)")
@Entity
public class User {
    @Id
    private Long id;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String name;
    private String surname;
    private Date birthday;
    private Gender gender;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="user_appartment_disliked",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="appartment_id"))
    private List<Appartment> ignoredAppartmentList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="user_appartment_liked",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="appartment_id"))
    private List<Appartment> likedAppartmentList;

    public Date getBirthday() {
        return birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Appartment> getIgnoredAppartmentList() {
        return ignoredAppartmentList;
    }

    public void setIgnoredAppartmentList(List<Appartment> appartmentList) {
        this.ignoredAppartmentList = appartmentList;
    }
}
