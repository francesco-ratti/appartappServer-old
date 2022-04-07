package com.polimi.mrf.appartapp.entities;

import com.polimi.mrf.appartapp.Gender;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NamedQuery(name="User.checkCredentials",
        query="SELECT u FROM User u WHERE u.email=:email AND u.password=:password"
)
@NamedQuery(name = "User.getNewApartments", query = "SELECT h FROM Apartment h, User u WHERE u.id=:userId AND h.id NOT IN (SELECT lh.id FROM u.likedApartmentList lh) AND h.id NOT IN (SELECT ih.id FROM u.ignoredApartmentList ih)")
//@NamedQuery(name = "User.getNewApartments", query = "SELECT h FROM Apartment h WHERE h.id NOT IN (SELECT lh.id FROM User u JOIN u.likedApartmentList lh WHERE u.id=:userId) AND h.id NOT IN (SELECT ih.id FROM User u JOIN u.ignoredApartmentList ih WHERE u.id=:userId)")
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name="user_apartment_disliked",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="apartment_id"))
    private List<Apartment> ignoredApartmentList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name="user_apartment_liked",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="apartment_id"))
    private List<Apartment> likedApartmentList;

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

    public List<Apartment> getIgnoredApartmentList() {
        return ignoredApartmentList;
    }

    public void setIgnoredApartmentList(List<Apartment> apartmentList) {
        this.ignoredApartmentList = apartmentList;
    }

    public void addIgnoredApartment(Apartment apartment) {
        this.ignoredApartmentList.add(apartment);
    }

    public void addLikedApartment(Apartment apartment) {
        this.likedApartmentList.add(apartment);
    }
}
